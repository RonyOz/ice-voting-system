package communication;
import com.zeroc.Ice.Current;

import controller.VotingServiceController;
import model.ReliableMessage;
import reliableMessage.ACKServicePrx;
import reliableMessage.RMDestination;

public class VotingServiceImpl implements RMDestination {  //VotingServicePrx

    private VotingServiceController controller;

    public VotingServiceImpl(VotingServiceController controller) {
        this.controller = controller;
    }

    @Override
    public void receiveMessage(ReliableMessage rmessage, ACKServicePrx prx, Current current) {

        prx.ack(rmessage.getUuid());

        System.out.println("[INFO] Received batch with size: " + rmessage.getMessage().voteBatch.size());

        controller.registerVotesFromSite(rmessage.getMessage().voteBatch);
    }

}
