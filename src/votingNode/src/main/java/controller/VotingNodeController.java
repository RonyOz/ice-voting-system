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
  public int vote(String voterId, String candidateId) {

    // TODO: Ver donde se genera el mesaId
    String mesaId = java.util.UUID.randomUUID().toString();

    Vote vote = new Vote(voterId, candidateId);
    votingSitePrx.sendVote(vote, mesaId);

    System.out.println("[INFO] Vote Sended - VOTERID " + voterId + " - CANDIDATEID" + candidateId + " - MESAID " + mesaId);

    return 0;
  }
}
