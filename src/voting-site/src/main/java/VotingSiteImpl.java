import java.util.List;

import com.zeroc.Ice.Current;

import Contract.VotingSite;
import Contract.Vote;

public class VotingSiteImpl implements VotingSite{

    private VotingSiteController controller;
    private ReliableMessenger reliableMessenger;

    public VotingSiteImpl(VotingSiteController controller) {
        this.controller = controller;
    }

    public void sendVote(Vote vote, Current current) {
        controller.processVote(vote);
    }

    public void reportVoteBatch(List<Vote> voteBatch) {
        reliableMessenger.sendMessage(voteBatch);
    }
 
}