package interfaces;

import java.util.List;

import Contract.Candidate;
import model.VotingLocationDTO;

public interface ILocationRepository {

    VotingLocationDTO findVotingLocation(String documentNumber);
    List<Candidate> getCandidates();

}
