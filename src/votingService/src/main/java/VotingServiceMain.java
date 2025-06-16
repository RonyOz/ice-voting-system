import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;
import controller.VotingServiceController;
import repository.DBConnection;
import repository.JdbcVoteRepository;
public class VotingServiceMain {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("votingServiceAdapter");
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));
            Properties properties = communicator.getProperties();
            Identity identity = Util.stringToIdentity(properties.getProperty("Identity"));

            DBConnection dbConnection = new DBConnection(communicator);
            dbConnection.connectDB();

            VotingServiceController controller = new VotingServiceController(new JdbcVoteRepository(dbConnection.getConnection()));
        
            VotingServiceImpl votingService = new VotingServiceImpl(controller);

            adapter.add(votingService, identity);
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