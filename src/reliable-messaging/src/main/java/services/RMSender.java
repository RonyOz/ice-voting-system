package services;

import com.zeroc.Ice.Current;

import communication.Notification;
import model.Message;
import reliableMessage.RMDestinationPrx;
import reliableMessage.RMSource;
import threads.RMJob;

public class RMSender implements RMSource{

    private RMJob jobM;
    private Notification notification;

    
    public RMSender(RMJob job, Notification not) {
        notification = not;
        jobM = job;
    }

    @Override
    public void sendMessage(Message msg, Current current) {
        jobM.add(msg);
    }
    @Override
    public void setServerProxy(RMDestinationPrx destination, Current current){
        notification.setService(destination);
    }


    
}
