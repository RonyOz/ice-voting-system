package services;

import com.zeroc.Ice.Communicator;

import Contract.Candidate;
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

  public void setCandidates(Candidate[] candidates) {
    if (candidates == null || candidates.length == 0) {
      System.err.println("[ERROR] No candidates provided to set.");
      return;
    }
    locationRepository.setCandidates(candidates);
  }

  public Candidate[] getCandidates() {
    Candidate[] candidates = locationRepository.findAllCandidates();
    if (candidates == null || candidates.length == 0) {
      System.err.println("[ERROR] No candidates found in the database.");
      return new Candidate[0];
    }
    return candidates;
  }

}
