package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Contract.Candidate;
import interfaces.ILocationRepository;
import model.VotingLocationDTO;

public class LocationRepository implements ILocationRepository {

    private final Connection dbConnection;

    public LocationRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public VotingLocationDTO findVotingLocation(String voterId) {
        String sql = "SELECT * " +
                "FROM ciudadano_vista_lookup WHERE documento = ? ;";

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, voterId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new VotingLocationDTO(
                        rs.getString("nombre_puesto"),
                        rs.getString("direccion"),
                        rs.getString("ciudad"),
                        rs.getString("departamento"),
                        rs.getInt("numero_mesa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Candidate[] findAllCandidates() {
        String sql = "SELECT * FROM candidates";
        
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Candidate> candidates = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                candidates.add(new Candidate(id, name));
            }
            return candidates.toArray(new Candidate[0]);
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve candidates: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void setCandidates(Candidate[] candidates) {
        // Clean table before inserting new candidates
        String deleteSql = "DELETE FROM candidates";
        try (PreparedStatement deleteStmt = dbConnection.prepareStatement(deleteSql)) {
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to clear candidates table: " + e.getMessage());
        }

        String sql = "INSERT INTO candidates (id, name) VALUES (?, ?)";

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            for (Candidate candidate : candidates) {
                stmt.setString(1, candidate.candidateId);
                stmt.setString(2, candidate.candidateId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to set candidates: " + e.getMessage());
        }
    }

}
