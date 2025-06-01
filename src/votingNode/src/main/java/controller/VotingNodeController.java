package controller;
import interfaces.IVotingNodeController;
import Contract.Vote;
import Contract.VotingSitePrx;

public class VotingNodeController implements IVotingNodeController {

    private final VotingSitePrx votingSitePrx;

    public VotingNodeController(VotingSitePrx votingSitePrx) {
        this.votingSitePrx = votingSitePrx;
    }

    // aqui debe implementarse la logica : Retorna 0 si puede votar; 1 si no es su mesa; 2 si está tratando de votar por segunda vez; 3 si no aparece en toda la bd

    @Override
    public int vote(String voterId, String candidateId) {

        Vote vote = new Vote(voterId, candidateId);
        votingSitePrx.sendVote(vote);

        String mesaId = java.util.UUID.randomUUID().toString();
        
        System.out.println("[INFO] [VOTE SENDED]: " + voterId + " " + candidateId + " " + mesaId);

        return 0;
    }
}
