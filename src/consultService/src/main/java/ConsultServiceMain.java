import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import controller.ConsultServiceController;
import communication.ConsultServiceImpl;

public class ConsultServiceMain {

  public static void main(String[] args) {

    java.util.List<String> extraArgs = new java.util.ArrayList<String>();

    try (Communicator communicator = Util.initialize(args, extraArgs)) {
      communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");

      ObjectAdapter adapter = communicator.createObjectAdapter("ConsultServiceAdapter");

      Properties properties = communicator.getProperties();
      Identity id = com.zeroc.Ice.Util.stringToIdentity(properties.getProperty("Identity"));

      ConsultServiceImpl consultService = new ConsultServiceImpl();

      // IDK if the namming being the same as the adapter matters
      adapter.add(consultService, id);

      System.out.println("[INFO] Consult Service is running");

      // FOR TESTING ===
      ConsultServiceController controller = new ConsultServiceController(communicator);

      long startTime = System.currentTimeMillis();

      System.out.println(controller.getVotingLocation("711674049"));

      long endTime = System.currentTimeMillis();

      double duration = (double) (endTime - startTime);

      System.out.println("Duration: " + duration);

      communicator.waitForShutdown();

    } catch (Exception e) {
      System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
