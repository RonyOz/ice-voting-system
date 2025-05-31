import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Contract.Vote;
import interfaces.IVotingSiteController;

public class VotingSiteController implements IVotingSiteController{

    private VotingSiteImpl votingSiteImpl;

    private List<Vote> votes = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    @Override
    public void processVote(Vote vote) {
        votes.add(vote);
        System.out.println("Vote received: " + vote);
    }

    public void setVotingSiteImpl(VotingSiteImpl votingSiteImpl) {
        this.votingSiteImpl = votingSiteImpl;
        startBatchDispatcher();
    }

    private void startBatchDispatcher() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!votes.isEmpty()) {
                    // Snapshot and clear buffer
                    List<Vote> batch = new ArrayList<>(votes);
                    votes.clear();  // Works because CopyOnWriteArrayList handles it safely

                    System.out.println("Dispatching batch of votes: " + batch.size());
                    // Send batch to the interface
                    votingSiteImpl.reportVoteBatch(batch);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log error
            }
        }, 0, 5, TimeUnit.SECONDS); 
    }

}
