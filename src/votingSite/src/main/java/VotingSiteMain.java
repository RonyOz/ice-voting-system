
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import Contract.Vote;
import reliableMessage.RMDestinationPrx;
import reliableMessage.RMSourcePrx;

public class VotingSiteMain {
    public static void main(String[] args) throws Exception {

        new Thread(() -> {
            System.out.println("Starting Reliable Server...");
            ReliableMessaging.main(new String[0]);
        }).start();

        Thread.sleep(2000);

        try (Communicator com = Util.initialize(args, "properties.cfg")) {

            ObjectAdapter adapter = com.createObjectAdapter("VotingSiteAdapter");

            RMDestinationPrx dest = RMDestinationPrx.checkedCast(com.propertyToProxy("RMDestination.Proxy"));
            RMSourcePrx rm = RMSourcePrx.checkedCast(com.propertyToProxy("RMSource.Proxy"));
            rm.setServerProxy(dest);

            //Cuidado con la dependencia circular PLEASE FIX
            VotingSiteController controller = new VotingSiteController();
            VotingSiteImpl votingSiteInterface = new VotingSiteImpl(controller,rm);
            controller.setVotingSiteImpl(votingSiteInterface);

            adapter.add(votingSiteInterface, Util.stringToIdentity("VotingSiteInterface"));

            // Hasta aqui estoy exponinedo mis interfaces
            adapter.activate();

            for (int i = 0; i < 100; i++) {
                controller.processVote(new Vote("Vote " + i, "Candidate " + i));
            }

            System.out.println("Voting Site is running...");
            com.waitForShutdown();
        } 

    }
}
