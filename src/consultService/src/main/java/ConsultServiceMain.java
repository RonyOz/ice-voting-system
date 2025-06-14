import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;


public class ConsultServiceMain {
        public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("consultServices");

            // DBConnection dbConnection = new DBConnection(communicator);
            // dbConnection.connectDB();

            // VotingServiceController controller = new VotingServiceController(new JdbcVoteRepository(dbConnection.getConnection()));
        
            // VotingServiceImpl votingService = new VotingServiceImpl(controller);

            // adapter.add(votingService, Util.stringToIdentity("votingService"));
            // adapter.activate();

            System.out.println("[INFO] Consult Service is running");

            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
