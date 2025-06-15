package controller;

import com.zeroc.Ice.Communicator;

import Contract.Candidate;
import services.VotingLocationService;

public class ConsultServiceController {

  private VotingLocationService votingLocationService;

  public ConsultServiceController(Communicator comm) {
    votingLocationService = new VotingLocationService(comm);
  }

  public String getVotingLocation(String voterId) {
    return votingLocationService.findVotingLocation(voterId);
  }

  public void setCandidates(Candidate[] candidates) {
    votingLocationService.setCandidates(candidates);
  }

  public Candidate[] getCandidates() {
    return votingLocationService.getCandidates();
  }

}
