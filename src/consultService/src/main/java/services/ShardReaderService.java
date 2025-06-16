package services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import repository.DBConnection;
import repository.VoteRepository;

public class ShardReaderService {
    private final DBConnection dbConnection;
    private final ExecutorService readerPool;

    public ShardReaderService(DBConnection dbConnection, int poolSize) {
        this.dbConnection = dbConnection;
        this.readerPool = Executors.newFixedThreadPool(poolSize);
    }

    public List<Future<List<VoteRepository.CandidateVoteResult>>> fetchPartialResults(List<String> mesaIds) {
        List<Future<List<VoteRepository.CandidateVoteResult>>> tasks = new ArrayList<>();

        for (String mesaId : mesaIds) {
            tasks.add(readerPool.submit(() -> {
                try (Connection conn = dbConnection.connect()) {
                    VoteRepository repo = new VoteRepository(conn);
                    return repo.countVotesByCandidate();
                }
            }));
        }
        return tasks;
    }

    public void shutdown() {
        readerPool.shutdown();
    }
}
