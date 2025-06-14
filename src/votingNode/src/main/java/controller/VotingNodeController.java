package controller;
import interfaces.IVotingNodeController;
import Contract.AuthServicePrx;
import Contract.Vote;
import Contract.VotingSitePrx;

public class VotingNodeController implements IVotingNodeController {

    private final VotingSitePrx votingSitePrx;
    private final AuthServicePrx authServicePrx;

    public VotingNodeController(VotingSitePrx votingSitePrx, AuthServicePrx authServicePrx) {
        this.votingSitePrx = votingSitePrx;
        this.authServicePrx = authServicePrx;
    }


    @Override
    public int vote(String voterId, String candidateId) {

        //TODO: Ver donde se genera el mesaId
        String mesaId = java.util.UUID.randomUUID().toString();

        int authResult = authServicePrx.authenticate(voterId, mesaId);

        Vote vote = new Vote(voterId, candidateId);
        votingSitePrx.sendVote(vote);

        
        System.out.println("[INFO] [VOTE SENDED]: " + voterId + " " + candidateId + " " + mesaId);

        return 0;
    }
}
