package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoteRepository {
    private final Connection conn;

    public static class CandidateVoteResult {
        public final String candidateId;
        public final String name;
        public final int votes;
        public CandidateVoteResult(String candidateId, String name, int votes) {
            this.candidateId = candidateId;
            this.name = name;
            this.votes = votes;
        }
    }

    public VoteRepository(Connection conn) {
        this.conn = conn;
    }

    public List<CandidateVoteResult> countVotesByCandidate() {
        String sql = "SELECT c.candidate_id, c.nombre, COUNT(v.id) as votos " +
                     "FROM candidatos c LEFT JOIN votes v ON c.candidate_id = v.candidate_id " +
                     "GROUP BY c.candidate_id, c.nombre ORDER BY votos DESC";
        List<CandidateVoteResult> result = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String candidateId = rs.getString("candidate_id");
                String name = rs.getString("nombre");
                int votos = rs.getInt("votos");
                result.add(new CandidateVoteResult(candidateId, name, votos));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to count votes: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
