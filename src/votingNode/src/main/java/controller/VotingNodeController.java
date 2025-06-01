package controller;
import interfaces.IVotingNodeController;
import Contract.Vote;
import Contract.VotingSitePrx;

public class VotingNodeController implements IVotingNodeController {

    private final VotingSitePrx votingSitePrx;

    public VotingNodeController(VotingSitePrx votingSitePrx) {
        this.votingSitePrx = votingSitePrx;
    }

    @Override
    public void vote(String voterId, String candidateId) {
        Vote vote = new Vote(voterId, candidateId);
        votingSitePrx.sendVote(vote);

        String mesaId = java.util.UUID.randomUUID().toString();
        System.out.println("[INFO] [VOTE SENDED]: " + voterId + " " + candidateId + " " + mesaId);
    }
}
