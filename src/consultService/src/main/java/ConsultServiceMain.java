import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import communication.ConsultServiceImpl;

public class ConsultServiceMain {

  public static void main(String[] args) {

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");

      ObjectAdapter adapter = communicator.createObjectAdapter("ConsultServiceAdapter");

      Properties properties = communicator.getProperties();
      Identity id = com.zeroc.Ice.Util.stringToIdentity(properties.getProperty("Identity"));

      ConsultServiceImpl consultService = new ConsultServiceImpl(communicator);

      // IDK if the namming being the same as the adapter matters
      adapter.add(consultService, id);

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
