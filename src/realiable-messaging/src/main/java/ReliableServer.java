import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import communication.Notification;
import reliableMessage.ACKServicePrx;
import services.RMReciever;
import services.RMSender;
import threads.RMJob;

public class ReliableServer {
    
    public static void main(String[] args) {
        Communicator communicator = Util.initialize(args, "rmservice.config");

        Notification notification = new Notification();
        RMJob job = new RMJob(notification);
        RMReciever rec = new RMReciever(job);
        RMSender sender = new RMSender(job, notification);

        ObjectAdapter adapter = communicator.createObjectAdapter("RMService");
        adapter.add(sender, Util.stringToIdentity("Sender"));
        ObjectPrx prx = adapter.add(rec, Util.stringToIdentity("AckCallback"));
        notification.setAckService(ACKServicePrx.checkedCast(prx));
        adapter.activate();
        job.start();

        communicator.waitForShutdown();


        
    }

    
}
