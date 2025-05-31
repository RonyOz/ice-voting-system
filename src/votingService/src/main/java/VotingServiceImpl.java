import com.zeroc.Ice.Current;

import model.ReliableMessage;
import reliableMessage.ACKServicePrx;
import reliableMessage.RMDestination;

public class VotingServiceImpl implements RMDestination {  //VotingServicePrx

    private int messageCount = 0;
    private Object lock = new Object();

    private VotingServiceController controller;

    public VotingServiceImpl(VotingServiceController controller) {
        this.controller = controller;
    }

    @Override
    public void receiveMessage(ReliableMessage rmessage, ACKServicePrx prx, Current current) {

        synchronized (lock) {
            messageCount += rmessage.getMessage().voteBatch.size();
        }

        System.out.println("Total messages processed: " + messageCount);

        prx.ack(rmessage.getUuid());

        System.out.println("[INFO] Received batch with size: " + rmessage.getMessage().voteBatch.size());

        controller.registerVotesFromSite(rmessage.getMessage().voteBatch);
    }

}
