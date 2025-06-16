package repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Properties;

public class DBConnection {
  
  private static DBConnection instance;
  private final Communicator com;
  private final HikariDataSource dataSource;

  private DBConnection(Communicator com) {
    this.com = com;

    Properties prop = this.com.getProperties();
    String jdbcURL = prop.getProperty("ConnectionDB.URL");
    String username = prop.getProperty("ConnectionDB.Username");
    String password = prop.getProperty("ConnectionDB.Password");

    System.out.println("[DEBUG] DBConnection: URL=" + jdbcURL + ", USER=" + username);

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcURL);
    config.setUsername(username);
    config.setPassword(password);

    config.setMaximumPoolSize(50);
    config.setMinimumIdle(10);
    config.setIdleTimeout(30000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(2000);

    this.dataSource = new HikariDataSource(config);
    System.out.println("[DEBUG] HikariDataSource inicializado.");
  }

  public static synchronized DBConnection getInstance(Communicator com) {
    if (instance == null) {
      instance = new DBConnection(com);
    }
    return instance;
  }

  public Connection connect() throws SQLException {
    return dataSource.getConnection();
  }

  public void shutdown() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}
