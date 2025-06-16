import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import controller.VotingServiceController;
import repository.DBConnection;
import repository.JdbcVoteRepository;
public class VotingServiceMain {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("votingServiceAdapter");

            DBConnection dbConnection = new DBConnection(communicator);
            dbConnection.connectDB();

            VotingServiceController controller = new VotingServiceController(new JdbcVoteRepository(dbConnection.getConnection()));
        
            VotingServiceImpl votingService = new VotingServiceImpl(controller);

            adapter.add(votingService, Util.stringToIdentity("votingService"));
            adapter.activate();

            System.out.println("[INFO] Voting Service is running");

            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}