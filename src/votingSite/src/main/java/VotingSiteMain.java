import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.IceGrid.QueryPrx;

import Comunication.VotingSiteImpl;
import Contract.AuthServicePrx;
import Controller.VotingSiteController;
import reliableMessage.RMDestinationPrx;
import reliableMessage.RMSourcePrx;

public class VotingSiteMain {
  public static void main(String[] args) throws java.lang.Exception {
    int status = 0;
    java.util.List<String> extraArgs = new java.util.ArrayList<String>();

    new Thread(() -> {
      System.out.println("[INFO] [RELIABLE MESSAGING] Starting Reliable Server");
      ReliableMessaging.main(new String[0]);
    }).start();

    Thread.sleep(2000);

    Communicator communicator = Util.initialize(args, "properties.cfg", extraArgs);
    Thread destroyHook = new Thread(() -> communicator.destroy());
    Runtime.getRuntime().addShutdownHook(destroyHook);

    try {
      status = run(communicator, destroyHook, extraArgs.toArray(new String[extraArgs.size()]));
    } catch (java.lang.Exception ex) {
      ex.printStackTrace();
      status = 1;
    }

    if (status != 0) {
      System.exit(status);
    }
  }

  private static int run(Communicator communicator, Thread destroyHook, String[] args) throws java.lang.Exception {
    String topicName = "ControlCommandTopic";

    // Configuración original del VotingSite
    ObjectAdapter adapter = communicator.createObjectAdapter("VotingSiteAdapter");

    RMDestinationPrx dest = null;
    QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("IceVotingSystem/Query"));
    dest = RMDestinationPrx.checkedCast(query.findObjectByType("::Contract::VotingService"));

    RMSourcePrx rm = RMSourcePrx.checkedCast(communicator.propertyToProxy("RMSource.Proxy"));
    rm.setServerProxy(dest);

    AuthServicePrx authServicePrx = AuthServicePrx.checkedCast(query.findObjectByType("::Contract::AuthService"));

    VotingSiteController controller = new VotingSiteController(authServicePrx);
    VotingSiteImpl votingSiteInterface = new VotingSiteImpl(controller, rm);
    controller.setVotingSiteImpl(votingSiteInterface);

    adapter.add(votingSiteInterface, Util.stringToIdentity("VotingSiteInterface"));

    // Configuración de Ice Storm
    com.zeroc.IceStorm.TopicManagerPrx manager = com.zeroc.IceStorm.TopicManagerPrx.checkedCast(
        communicator.propertyToProxy("TopicManager.Proxy"));
    if (manager == null) {
      System.err.println("invalid proxy");
      return 1;
    }

    com.zeroc.IceStorm.TopicPrx topic;
    try {
      topic = manager.retrieve(topicName);
    } catch (com.zeroc.IceStorm.NoSuchTopic e) {
      try {
        topic = manager.create(topicName);
      } catch (com.zeroc.IceStorm.TopicExists ex) {
        System.err.println("temporary failure, try again.");
        return 1;
      }
    }

    String randId = java.util.UUID.randomUUID().toString();
    com.zeroc.Ice.Identity subId = Util.stringToIdentity(randId);
    com.zeroc.Ice.ObjectPrx subscriber = adapter.add(new ControlCommandImpl(), subId);

    adapter.activate();

    java.util.Map<String, String> qos = new java.util.HashMap<>();

    try {
      topic.subscribeAndGetPublisher(qos, subscriber);
    } catch (com.zeroc.IceStorm.AlreadySubscribed e) {
      System.out.println("reactivating persistent subscriber");
    } catch (com.zeroc.IceStorm.InvalidSubscriber e) {
      e.printStackTrace();
      return 1;
    } catch (com.zeroc.IceStorm.BadQoS e) {
      e.printStackTrace();
      return 1;
    }

    final com.zeroc.IceStorm.TopicPrx topicF = topic;
    final com.zeroc.Ice.ObjectPrx subscriberF = subscriber;
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        topicF.unsubscribe(subscriberF);
      } finally {
        communicator.destroy();
      }
    }));
    Runtime.getRuntime().removeShutdownHook(destroyHook);

    System.out.println("[INFO] Voting Site is running");
    System.out.println("[INFO] Ice Storm subscriber is active");
    communicator.waitForShutdown();

    return 0;
  }
}
