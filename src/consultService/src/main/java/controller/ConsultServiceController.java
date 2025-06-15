package controller;

import services.VotingLocationService;

public class ConsultServiceController {

  private VotingLocationService votingLocationService;

  public ConsultServiceController() {
    votingLocationService = new VotingLocationService();
  }

  public String getVotingLocation(String voterId) {
    return votingLocationService.findVotingLocation(voterId);
  }

}
