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
    // TODO: Single point of failure
    return votingLocationService.findVotingLocation(voterId);
  }

  public void setCandidates(Candidate[] candidates) {
    votingLocationService.setCandidates(candidates);
  }

  public Candidate[] getCandidates() {
    // TODO: Single point of failure - Proxy Cache
    return votingLocationService.getCandidates();
  }

}
