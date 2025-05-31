package Controller;

import java.util.List;

import Comunication.VotingSiteImpl;
import Contract.Vote;
import interfaces.IVotingSiteController;

public class VotingSiteController implements IVotingSiteController {

    private VotingSiteImpl votingSiteImpl;
    private BatchingController voteBatcher;

    // Batch configuration
    private static final int BATCH_SIZE = 10000; // Process batches of 50 votes
    private static final long TIMEOUT_MS = 500; // Wait max 500ms for batch to fill

    @Override
    public void processVote(Vote vote) {
        voteBatcher.addTask(vote);
    }

    public void setVotingSiteImpl(VotingSiteImpl votingSiteImpl) {
        this.votingSiteImpl = votingSiteImpl;
        startBatchProcessor();
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
                System.out.println("Processing batch of " + voteBatch.size() + " votes");

                Thread.sleep(10); //Sleep, so server doesn't crash

                votingSiteImpl.reportVoteBatch(voteBatch);
            }
        } catch (Exception e) {
            System.err.println("Error processing vote batch: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
