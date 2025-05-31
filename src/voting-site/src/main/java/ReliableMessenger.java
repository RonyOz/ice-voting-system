import java.util.List;

import Contract.Vote;
import interfaces.IReliableMessenger;

public class ReliableMessenger implements IReliableMessenger {

    @Override
    public void sendMessage(List<Vote> voteBatch) {
        // RMSourcePrx rm = RMSourcePrx.checkedCast(com.stringToProxy("Sender:tcp -h
        // localhost -p 10010"));
        // RMDestinationPrx dest = RMDestinationPrx
        // .uncheckedCast(com.stringToProxy("Service:tcp -h localhost -p 10012"));

        // rm.setServerProxy(dest);
        // Message msg = new Message();
        // msg.message = "Send with RM";
        // rm.sendMessage(msg);
        // System.out.println("sended");
    }

}
