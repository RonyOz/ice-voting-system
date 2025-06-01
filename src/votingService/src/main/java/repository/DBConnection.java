package repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Properties;

public class DBConnection {

	private Communicator com;
	private Connection connection;

	public DBConnection(Communicator com) {
		this.com = com;
	}

	public String connectDB() {

		try {
			Properties prop = com.getProperties();
			String jdbcURL = prop.getProperty("ConnectionDB.URL");
			String username = prop.getProperty("ConnectionDB.Username");
			String password = prop.getProperty("ConnectionDB.Password");
			String driverClass = prop.getProperty("ConnectionDB.Driver");

			Class.forName(driverClass);
			connection = DriverManager.getConnection(jdbcURL, username,password);

			if (connection == null) {
				return "Connection failed";
			}

			System.out.println("[INFO] [H2] Connected to the database successfully.");
			createSchemaIfNotExists();

		} catch (ClassNotFoundException e) {
			System.err.println("[ERROR] [H2] JDBC Driver not found: " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("[ERROR] [H2] Connection failed: " + e.getMessage());
		}
		return null;
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createSchemaIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS votes ("
				+ "voter_id VARCHAR(255) NOT NULL,"
				+ "candidate_id VARCHAR(255) NOT NULL,"
				+ "PRIMARY KEY (voter_id, candidate_id)"
				+ ")";
		try (var stmt = connection.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("[INFO] [H2] Schema created successfully.");
		} catch (SQLException e) {
			System.err.println("[ERROR] [H2] Failed to create schema: " + e.getMessage());
		}
	}

}
