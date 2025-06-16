import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import communication.ProxyCacheImpl;

public class ProxyCacheMain {

  public static void main(String[] args) {
    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");
      ObjectAdapter adapter = communicator.createObjectAdapter("ProxyCacheAdapter");
      com.zeroc.Ice.Properties properties = communicator.getProperties();
      com.zeroc.Ice.Identity id = com.zeroc.Ice.Util.stringToIdentity(properties.getProperty("Identity"));

      System.out.println("going to create the proxy");
      ProxyCacheImpl proxyCache = new ProxyCacheImpl(communicator);
      System.out.println("proxy created");

      adapter.add(proxyCache, id);
      adapter.activate();
      System.out.println("proxy added to the adapter and activated");
      System.out.println(id.name);

      System.out.println("[INFO] Proxy Cache is running");
      communicator.waitForShutdown();

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
