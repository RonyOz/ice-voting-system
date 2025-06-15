package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import model.VotingLocationDTO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Ejemplo de uso
 *
 *
 * import repository.DatabaseConnector;
 * import dto.VotingLocationDTO;
 * 
 * public class Main {
 * public static void main(String[] args) {
 * DatabaseConnector db = DatabaseConnector.getInstance();
 * VotingLocationDTO location = db.findVotingLocation("123456789");
 * 
 * if (location != null) {
 * System.out.println(location);
 * } else {
 * System.out.println("No se encontró información de votación para ese
 * documento.");
 * }
 * }
 * }
 * 
 */
public class DatabaseConnector {
  private static final String URL = "jdbc:postgresql://10.147.17.101:5432/ice_voting_system_db";
  private static final String USER = "ice_voting_system_user";
  private static final String PASSWORD = "ice_voting_system_user";

  private static DatabaseConnector instance;
  private final HikariDataSource dataSource;

  private DatabaseConnector() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(URL);
    config.setUsername(USER);
    config.setPassword(PASSWORD);

    config.setMaximumPoolSize(50); // max concurrent connections
    config.setMinimumIdle(10);
    config.setIdleTimeout(30000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(2000); // 2 seconds

    this.dataSource = new HikariDataSource(config);
  }

  public static synchronized DatabaseConnector getInstance() {
    if (instance == null) {
      instance = new DatabaseConnector();
    }
    return instance;
  }

  public Connection connect() throws SQLException {
    return dataSource.getConnection();
  }

  public VotingLocationDTO findVotingLocation(String voterId) {
    String sql = "SELECT * " +
        "FROM ciudadano_vista_lookup WHERE documento = ? ;";

    try (
        Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
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

  public void shutdown() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}
