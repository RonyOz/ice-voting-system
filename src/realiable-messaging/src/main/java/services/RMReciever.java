package services;

import com.zeroc.Ice.Current;

import reliableMessage.ACKService;
import threads.RMJob;

public class  RMReciever implements  ACKService {

    private RMJob jobM;

    

    public RMReciever(RMJob job) {
        this.jobM = job;
    }

    @Override
    public void ack(String messageId, Current current) {
        jobM.confirmMessage(messageId);
    }

    
    
}
