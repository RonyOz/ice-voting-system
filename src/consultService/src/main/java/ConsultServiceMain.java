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

      adapter.add(consultService, id);
      adapter.activate();

      System.out.println("[INFO] Consult Service is running");

      // id de ejemplo 711674049
      System.out.println("[INFO] Endpoint: " + adapter.getEndpoints()[0]);

      // Imprimir resultados al iniciar
      System.out.println("\n=== CONSULTANDO RESULTADOS DE VOTACIÓN ===");
      System.out.println("\n=== FIN DE RESULTADOS ===\n");

      communicator.waitForShutdown();

    } catch (Exception e) {
      System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
