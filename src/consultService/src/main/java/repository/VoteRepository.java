package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VoteRepository {
    private final Connection conn;

    public VoteRepository(Connection conn) {
        this.conn = conn;
    }

    public Map<String, Integer> countVotesByCandidate(String mesaId) {
        String sql = "SELECT candidate_id, COUNT(*) FROM votes WHERE mesa_id = ? GROUP BY candidate_id";
        Map<String, Integer> result = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mesaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to count votes for mesa " + mesaId + ": " + e.getMessage());
        }   
        return result;
    }
}
