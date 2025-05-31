package communication;

import model.ReliableMessage;
import reliableMessage.RMDestinationPrx;
import reliableMessage.ACKServicePrx;

public class Notification {

    private RMDestinationPrx service;

    private ACKServicePrx ackService;

    public void setAckService(ACKServicePrx ackService) {
        this.ackService = ackService;
    }

    public void setService(RMDestinationPrx service) {
        this.service = service;
    }

    

    public void sendMessage(ReliableMessage message){
        service.reciveMessage(message, ackService);
    }
}
