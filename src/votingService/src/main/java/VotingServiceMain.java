import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Properties;
import com.zeroc.Ice.Util;

import communication.VotingServiceImpl;
import controller.VotingServiceController;
import repository.DBConnection;
import repository.VoteRepository;
public class VotingServiceMain {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("votingServiceAdapter");
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));
            Properties properties = communicator.getProperties();
            Identity identity = Util.stringToIdentity(properties.getProperty("Identity"));

            DBConnection dbConnection = DBConnection.getInstance(communicator);

            VotingServiceController controller = new VotingServiceController(new VoteRepository(dbConnection.connect()));
        
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