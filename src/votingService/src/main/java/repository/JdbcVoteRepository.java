package repository;

import java.sql.Connection;
import java.util.List;

import Contract.Vote;
import interfaces.IVoteRepository;

public class JdbcVoteRepository implements IVoteRepository {

    private Connection H2Connection;
    // private Connection PostgresConnection;

    public JdbcVoteRepository(Connection h2Connection) {
        this.H2Connection = h2Connection;
        // this.PostgresConnection = postgresConnection;
    }

    @Override
    public boolean saveVote(Vote vote) {
        if (vote == null) {
            return false;
        }
        String sql = "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)";
        boolean h2Success = false;

        try (var h2Stmt = H2Connection.prepareStatement(sql)) {
            h2Stmt.setString(1, vote.voterId);
            h2Stmt.setString(2, vote.candidateId);
            h2Success = h2Stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("[ERROR] [H2] Failed saving vote: " + e.getMessage());
        }

        // boolean pgSuccess = false;
        // try (var pgStmt = PostgresConnection.prepareStatement(sql)) {
        //     pgStmt.setString(1, vote.getId());
        //     pgStmt.setString(2, vote.getVoterId());
        //     pgStmt.setString(3, vote.getCandidateId());
        //     pgStmt.setTimestamp(4, vote.getTimestamp());
        //     pgSuccess = pgStmt.executeUpdate() > 0;
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        return h2Success;
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

        if (!allSaved) {
            System.err.println("[ERROR] [H2] Not all votes were saved successfully.");
        } else {
            System.out.println("[INFO] [H2] voteBatch saved successfully.");
        }

        return allSaved;
    }

    @Override
    public boolean exists(String voterId, String candidateId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE voter_id = ? AND candidate_id = ?";
        try (var stmt = H2Connection.prepareStatement(sql)) {
            stmt.setString(1, voterId);
            stmt.setString(2, candidateId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] [H2] Failed checking vote existence: " + e.getMessage());
        }
        return false;
    }

    
}