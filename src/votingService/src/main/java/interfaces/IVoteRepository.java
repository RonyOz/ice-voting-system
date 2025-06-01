package interfaces;

import java.util.List;

import Contract.Vote;

public interface IVoteRepository {
    boolean saveVote(Vote vote);
    boolean saveAll(List<Vote> votes);
    boolean exists(String voterId, String candidateId);

}
