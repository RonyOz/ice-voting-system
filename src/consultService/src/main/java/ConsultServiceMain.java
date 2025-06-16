import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import controller.ConsultServiceController;
import communication.ConsultServiceImpl;

public class ConsultServiceMain {

  public static void main(String[] args) {

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      ObjectAdapter adapter = communicator.createObjectAdapter("ConsultService");

      ConsultServiceImpl consultService = new ConsultServiceImpl(communicator);

      // IDK if the namming being the same as the adapter matters
      adapter.add(consultService, Util.stringToIdentity("ConsultService"));

      // Activar el adaptador
      adapter.activate();

      System.out.println("[INFO] Consult Service is running");
      System.out.println("[INFO] Endpoint: " + adapter.getEndpoints()[0].toString());

      // probar con el 711674049

      // Esperar por shutdown
      communicator.waitForShutdown();

    } catch (Exception e) {
      System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
