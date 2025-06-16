package interfaces;

import java.util.List;

import Contract.Vote;

public interface IVotingServiceController {
    void registerVotesFromSite(List<Vote> voteBatch);
}
