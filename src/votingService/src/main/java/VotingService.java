import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class VotingService {
    public static void main(String[] args) {

        Communicator com = Util.initialize();
        VotingServiceImpl imp = new VotingServiceImpl();
        ObjectAdapter adapter = com.createObjectAdapterWithEndpoints("VotingService", "tcp -h localhost -p 10012");
        adapter.add(imp, Util.stringToIdentity("VotingService"));
        System.out.println("Voting Service is running...");
        adapter.activate();
        com.waitForShutdown();

        // try(Communicator communicator = Util.initialize(args, "properties.cfg")) {
        //     ObjectAdapter adapter = communicator.createObjectAdapter("VotingService.Endpoints");
        //     VotingServiceImpl votingService = new VotingServiceImpl();
        //     adapter.add(votingService, Util.stringToIdentity("VotingService"));
        //     adapter.activate();
        //     System.out.println("Voting Service is running...");
        //     communicator.waitForShutdown();

        // } catch (Exception e) {
        //     System.err.println("Error initializing communicator: " + e.getMessage());
        //     e.printStackTrace();
        //     System.exit(1);
        // }
    }
}
