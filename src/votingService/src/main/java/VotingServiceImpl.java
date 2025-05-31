import com.zeroc.Ice.Current;

import model.ReliableMessage;
import reliableMessage.ACKServicePrx;
import reliableMessage.RMDestination;

public class VotingServiceImpl implements RMDestination {

    private int messageCount = 0;
    private Object lock = new Object();

    @Override
    public void receiveMessage(ReliableMessage rmessage, ACKServicePrx prx, Current current) {
        System.out.println("Received message: " + rmessage.getMessage());
        System.out.println("Batch size: " + rmessage.getMessage().voteBatch.size());

        synchronized (lock) {
            messageCount += rmessage.getMessage().voteBatch.size();
        }
        System.out.println("Total messages processed: " + messageCount);
        prx.ack(rmessage.getUuid());
    }

}
