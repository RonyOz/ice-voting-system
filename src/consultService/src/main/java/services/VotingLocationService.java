package services;

import repository.DatabaseConnector;

public class VotingLocationService {

  private DatabaseConnector databaseConnector;

  public VotingLocationService() {
    databaseConnector = DatabaseConnector.getInstance();
  }

  public String findVotingLocation(String voterId) {
    return databaseConnector.findVotingLocation(voterId).toString();
  }

}
