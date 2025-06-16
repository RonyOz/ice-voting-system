package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthRepository {
    private final Connection dbConnection;

    public AuthRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Boolean hasVoted(String documento) {
        System.out.println("[DEBUG] Consultando ha_votado para documento: " + documento);
        String sql = "SELECT ha_votado FROM ciudadano WHERE documento = ?;";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean result = rs.getBoolean("ha_votado");
                System.out.println("[DEBUG] ha_votado: " + result);
                return result;
            } else {
                System.out.println("[DEBUG] No existe el ciudadano.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error en hasVoted: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
