
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
  public static void main(String[] args) throws Exception {

    new Thread(() -> {
      System.out.println("[INFO] [RELIABLE MESSAGING] Starting Reliable Server");
      ReliableMessaging.main(new String[0]);
    }).start();

    Thread.sleep(2000);

    try (Communicator com = Util.initialize(args, "properties.cfg")) {

      ObjectAdapter adapter = com.createObjectAdapter("VotingSiteAdapter");

      RMDestinationPrx dest = null;
      QueryPrx query = QueryPrx.checkedCast(com.stringToProxy("IceVotingSystem/Query"));
      dest = RMDestinationPrx.checkedCast(query.findObjectByType("::Contract::VotingService"));

      RMSourcePrx rm = RMSourcePrx.checkedCast(com.propertyToProxy("RMSource.Proxy"));
      rm.setServerProxy(dest);

      AuthServicePrx authServicePrx = AuthServicePrx.checkedCast(query.findObjectByType("::Contract::AuthService"));

      VotingSiteController controller = new VotingSiteController(authServicePrx);
      VotingSiteImpl votingSiteInterface = new VotingSiteImpl(controller, rm);
      controller.setVotingSiteImpl(votingSiteInterface);

      adapter.add(votingSiteInterface, Util.stringToIdentity("VotingSiteInterface"));

      // Hasta aqui estoy exponinedo mis interfaces
      adapter.activate();

      System.out.println("[INFO] Voting Site is running");
      com.waitForShutdown();

    }

  }
}
