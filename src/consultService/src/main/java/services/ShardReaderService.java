package services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public List<Future<Map<String, Integer>>> fetchPartialResults(List<String> mesaIds) {
        List<Future<Map<String, Integer>>> tasks = new ArrayList<>();

        for (String mesaId : mesaIds) {
            tasks.add(readerPool.submit(() -> {
                try (Connection conn = dbConnection.connect()) {
                    VoteRepository repo = new VoteRepository(conn);
                    return repo.countVotesByCandidate(mesaId);
                }
            }));
        }
        return tasks;
    }

    public void shutdown() {
        readerPool.shutdown();
    }
}
