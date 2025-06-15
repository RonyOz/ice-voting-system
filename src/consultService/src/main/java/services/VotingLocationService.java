package services;

import com.zeroc.Ice.Communicator;

import repository.DBConnection;
import repository.LocationRepository;

public class VotingLocationService {

  private DBConnection databaseConnector;
  private LocationRepository locationRepository;

  public VotingLocationService(Communicator comm) {
    databaseConnector = DBConnection.getInstance(comm);

    try {
      locationRepository = new LocationRepository(databaseConnector.connect());
    } catch (Exception e) {
      System.err.println("[ERROR] Failed to connect to the database: " + e.getMessage());
    }

  }

  public String findVotingLocation(String voterId) {
    return locationRepository.findVotingLocation(voterId).toString();
  }

}
