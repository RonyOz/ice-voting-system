package interfaces;

import Contract.Candidate;
import model.VotingLocationDTO;

public interface ILocationRepository {

    VotingLocationDTO findVotingLocation(String documentNumber);
    Candidate[] findAllCandidates();
    void setCandidates(Candidate[] candidates);

}
