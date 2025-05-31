import java.util.List;

import Contract.Vote;
import interfaces.IVotingServiceController;

public class VotingServiceController implements IVotingServiceController {

    @Override
    public void registerVotesFromSite(List<Vote> voteBatch) {
        System.out.println("[INFO] Saving votes from site...");
    }
    
}
