import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class VotingService {
    public static void main(String[] args) {

        try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("votingServices");
            VotingServiceImpl votingService = new VotingServiceImpl();
            adapter.add(votingService, Util.stringToIdentity("votingService"));
            adapter.activate();
            System.out.println("Voting Service is running...");
            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println("Error initializing communicator: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}