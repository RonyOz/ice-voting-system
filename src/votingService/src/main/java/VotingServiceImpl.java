import com.zeroc.Ice.Current;

import model.ReliableMessage;
import reliableMessage.ACKServicePrx;
import reliableMessage.RMDestination;

public class VotingServiceImpl implements RMDestination {

    @Override
    public void receiveMessage(ReliableMessage rmessage, ACKServicePrx prx, Current current) {
        System.out.println("Received message: " + rmessage.getMessage());
        System.out.println("Batch size: " + rmessage.getMessage().voteBatch.size());
        prx.ack(rmessage.getUuid());
    }

}

