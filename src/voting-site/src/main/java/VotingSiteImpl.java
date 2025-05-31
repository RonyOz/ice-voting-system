import java.util.List;

import com.zeroc.Ice.Current;

import Contract.VotingSite;
import reliableMessage.RMSourcePrx;
import Contract.Vote;

public class VotingSiteImpl implements VotingSite{

    private VotingSiteController controller;
    private RMSourcePrx rm;

    public VotingSiteImpl(VotingSiteController controller, RMSourcePrx rm) {
        this.controller = controller;
    }

    public void sendVote(Vote vote, Current current) {
        controller.processVote(vote);
    }

    public void reportVoteBatch(List<Vote> voteBatch) {
        rm.sendMessage(voteBatch);
    }
 
}