package repository;

import java.sql.Connection;
import java.util.List;

import Contract.Vote;
import interfaces.IVoteRepository;

public class VoteRepository implements IVoteRepository {

    private final Connection dbConnection;

    public VoteRepository(Connection connection) {
        this.dbConnection = connection;
    }

    @Override
    public boolean saveVote(Vote vote) {
        boolean success = false;
        String insertSql = "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)";
        String updateSql = "UPDATE ciudadano SET ha_votado = 't' WHERE documento = ?";

        try (var insertStmt = dbConnection.prepareStatement(insertSql)) {
            insertStmt.setString(1, vote.voterId);
            insertStmt.setString(2, vote.candidateId);
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
                try (var updateStmt = dbConnection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, vote.voterId);
                    updateStmt.executeUpdate();
                } catch (Exception e) {
                    System.err.println("[WARN] [DB] No se pudo actualizar ha_votado: " + e.getMessage());
                }
            }
            System.out.println("[DEBUG] Vote saved: " + success);
        } catch (Exception e) {
            System.err.println("[ERROR] [DB] Failed to save vote: " + e.getMessage());
        }

        return success;
    }

    @Override
    public boolean saveAll(List<Vote> votes) {
        if (votes == null || votes.isEmpty()) {
            return false;
        }
        boolean allSaved = true;
        for (Vote vote : votes) {
            boolean saved = saveVote(vote);
            if (!saved) {
                allSaved = false;
            }
        }

        return allSaved;
    }

    @Override
    public boolean exists(String voterId, String candidateId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE voter_id = ? AND candidate_id = ?";
        try (var stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, voterId);
            stmt.setString(2, candidateId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] [DB] Failed checking vote existence: " + e.getMessage());
        }
        return false;
    }

    
}