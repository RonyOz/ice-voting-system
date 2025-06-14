import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import communication.ConsultServiceImpl;


public class ConsultServiceMain {
        public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("consultServices");

            // DBConnection dbConnection = new DBConnection(communicator);
            // dbConnection.connectDB();

            // VotingServiceController controller = new VotingServiceController(new JdbcVoteRepository(dbConnection.getConnection()));
        
            ConsultServiceImpl votingService = new ConsultServiceImpl();

            adapter.add(votingService, Util.stringToIdentity("consultService"));
            adapter.activate();

            System.out.println("[INFO] Consult Service is running");

            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
