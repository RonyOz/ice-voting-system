package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public List<Candidate> getCandidates() {
        throw new UnsupportedOperationException("Unimplemented method 'getCandidates'");
    }

}
