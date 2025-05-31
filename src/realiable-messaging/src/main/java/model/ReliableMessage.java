package model;

import java.io.Serializable;

public class ReliableMessage implements Serializable{
    
    private String uuid;
    private long numberMessage;
    private String state;

    private Message message;

    public ReliableMessage(String uuid, long numberMessage, String state, Message message) {
        this.uuid = uuid;
        this.numberMessage = numberMessage;
        this.state = state;
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getNumberMessage() {
        return numberMessage;
    }

    public void setNumberMessage(long numberMessage) {
        this.numberMessage = numberMessage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    
    
    
}
