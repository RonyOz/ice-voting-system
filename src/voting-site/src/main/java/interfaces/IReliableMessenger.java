package interfaces;

import java.util.List;

import Contract.Vote;

public interface IReliableMessenger {
    void sendMessage(List<Vote> voteBatch);
}
