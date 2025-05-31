package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import Contract.Vote;

public class BatchingController {

    private final BlockingQueue<Vote> tasksQueue = new LinkedBlockingQueue<>();
    private final Thread batchThread;
    private final int executionThreshold;
    private final long timeoutMs;

    public BatchingController(int executionThreshold, long timeoutMs, Consumer<List<Vote>> executionLogic) {
        this.executionThreshold = executionThreshold;
        this.timeoutMs = timeoutMs;
        this.batchThread = new Thread(batchHandling(executionLogic));
        this.batchThread.setDaemon(true);
        this.batchThread.start();
    }

    private Runnable batchHandling(Consumer<List<Vote>> executionLogic) {
        return () -> {
            while (!batchThread.isInterrupted()) {
                try {
                    List<Vote> tasks = new ArrayList<>();

                    // Wait for at least one task
                    Vote firstTask = tasksQueue.poll(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (firstTask != null) {
                        tasks.add(firstTask);

                        // Drain additional tasks up to threshold
                        tasksQueue.drainTo(tasks, executionThreshold - 1);

                        // Process the batch
                        try {
                            executionLogic.accept(tasks);
                        } catch (Exception e) {
                            System.err.println("Error processing batch: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
    }

    public void addTask(Vote task) {
        tasksQueue.offer(task);
    }

}
