import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import controller.ConsultServiceController;
import impl.ConsultServiceImpl;

public class ConsultServiceMain {

  public static void main(String[] args) {

    try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

      ObjectAdapter adapter = communicator.createObjectAdapter("ConsultService");

      ConsultServiceImpl consultService = new ConsultServiceImpl();

      // IDK if the namming being the same as the adapter matters
      adapter.add(consultService, Util.stringToIdentity("ConsultService"));

      System.out.println("[INFO] Consult Service is running");

      // FOR TESTING ===
      ConsultServiceController controller = new ConsultServiceController();
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
