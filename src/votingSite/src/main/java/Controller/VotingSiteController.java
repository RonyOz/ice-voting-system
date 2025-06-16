package Controller;

import java.util.List;

import Comunication.VotingSiteImpl;
import Contract.AuthServicePrx;
import Contract.Vote;
import interfaces.IVotingSiteController;

public class VotingSiteController implements IVotingSiteController {

  private VotingSiteImpl votingSiteImpl;
  private BatchingController voteBatcher;
  private AuthServicePrx authServicePrx;

  // Batch configuration
  private static final int BATCH_SIZE = 1000; // Process batches of 1000 votes
  private static final long TIMEOUT_MS = 500; // Wait max 500ms for batch to fill
                                              //

  public VotingSiteController(AuthServicePrx authServicePrx) {
    this.authServicePrx = authServicePrx;
    startBatchProcessor();
  }

  @Override
  public void processVote(Vote vote, String votingNodeID) {
    if (authServicePrx.authenticate(vote.voterId, votingNodeID) == 0) {
      voteBatcher.addTask(vote);
      System.out.println("[INFO] [AUTH] Vote from " + vote.voterId + " authenticated successfully.");
    }
  }

  public void setVotingSiteImpl(VotingSiteImpl votingSiteImpl) {
    this.votingSiteImpl = votingSiteImpl;
  }

  private void startBatchProcessor() {
    // Initialize the smart batcher with batch processing logic
    voteBatcher = new BatchingController(
        BATCH_SIZE,
        TIMEOUT_MS,
        this::processBatch);
  }

  private void processBatch(List<Vote> voteBatch) {
    try {
      if (!voteBatch.isEmpty()) {
        System.out.println("[INFO] Processing batch of " + voteBatch.size() + " votes");

        Thread.sleep(10); // Sleep, so server doesn't crash

        votingSiteImpl.reportVoteBatch(voteBatch);
      }
    } catch (Exception e) {
      System.err.println("[ERROR] Failed to processing vote batch: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
