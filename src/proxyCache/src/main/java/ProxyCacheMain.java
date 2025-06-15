import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import communication.ProxyCacheImpl;

public class ProxyCacheMain {

  public static void main(String[] args) {
    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      ObjectAdapter adapter = communicator.createObjectAdapter("ProxyCache");

      ProxyCacheImpl proxyCache = new ProxyCacheImpl(communicator);

      adapter.add(proxyCache, Util.stringToIdentity("ProxyCache"));
      adapter.activate();

      System.out.println("[INFO] Proxy Cache is running");

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
