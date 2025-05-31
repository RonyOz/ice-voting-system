package model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import Contract.Vote;

@AllArgsConstructor
public class Message implements Serializable {
    
    public List<Vote> voteBatch;
}
