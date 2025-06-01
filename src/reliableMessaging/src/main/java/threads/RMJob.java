package threads;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import communication.Notification;
import model.Message;
import model.ReliableMessage;

public class RMJob extends Thread{

    public static final String PENDING = "Pending";
    public static final String SENDED = "Sended";

    private Map<String,ReliableMessage> messagesPendig = new ConcurrentHashMap<>();
    private Map<String,ReliableMessage> forConfirm = new ConcurrentHashMap<>();


    private Long sequenceNumber = 0l;
    private Object lock = new Object();
    private boolean enable = true;
    private Notification notification;

    public RMJob(Notification notification) {
        this.notification = notification;
    }

    public void add(Message message){
        synchronized (lock) {
            ReliableMessage mes = new ReliableMessage(UUID.randomUUID().toString(), sequenceNumber++, PENDING, message);
            messagesPendig.put(mes.getUuid(),mes);
        }
    }

    public void confirmMessage(String uid){
        forConfirm.remove(uid);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void run(){
        while (enable) { 
            for(Map.Entry<String,ReliableMessage> rm: messagesPendig.entrySet()){
                try {
                    notification.sendMessage(rm.getValue());
                    messagesPendig.remove(rm.getKey());
                    System.out.println("[INFO] [RELIABLE MESSAGING] Message sent: "+ rm.getValue().getUuid());
                    forConfirm.put(rm.getKey(), rm.getValue());
                } catch (Exception e) {
                    System.err.println("[ERROR] [RELIABLE MESSAGING] Failed to send message: " + rm.getValue().getUuid());
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10000);
                System.out.println("[INFO] [RELIABLE MESSAGING] Cicle " + sequenceNumber + " completed. Pending messages: " + messagesPendig.size());
            } catch (Exception e) {
                System.err.println("[ERROR] [RELIABLE MESSAGING] Error in RMJob sleep: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
