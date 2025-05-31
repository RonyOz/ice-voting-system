package model;

import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ReliableMessage implements Serializable{
    
    private String uuid;
    private long numberMessage;
    private String state;

    private Message message;
    
}
