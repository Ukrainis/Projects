package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Utils {
	private static final String connectionString = "jdbc:h2:./db/db";
	private static final String connectionStringDbExists = "jdbc:h2:./db/db;IFEXISTS=TRUE";
	
	
	public static Connection getDbConnection() {
		Connection conn = null;
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Can't find H2 library.");
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(connectionStringDbExists);
			System.out.println("The database was found. Connection was successfull.");
		} catch (SQLException e) {
			System.out.println("Database file not found. Creating a new Database in folder db.");
			try {
				conn = DriverManager.getConnection(connectionString);
				System.out.println("The new database was successfully created. Connection was successfull.");
				createDatabaseStructure();
			} catch (SQLException e1) {
				System.out.println("Can't create a new Database in folder db.");
			}
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
			conn = null;
			System.out.println("Connection to Database was closed.");
		} catch (SQLException e) {
			System.out.println("Can't close connection to Database.");
		}
	}
	
	public static void createDatabaseStructure() {
		Statement stmt = null;
		Connection conn = Utils.getDbConnection();
		try {
			stmt = conn.createStatement();
			stmt.addBatch("CREATE TABLE PROVIDER(PROV_ID INT AUTO_INCREMENT PRIMARY KEY, PROVIDER_NAME VARCHAR(100))");
			stmt.addBatch("CREATE TABLE CATEGORY(CAT_ID INT AUTO_INCREMENT PRIMARY KEY, CATEGORY_NAME VARCHAR(100))");
			stmt.addBatch("CREATE TABLE BILL(BILL_ID INT AUTO_INCREMENT, BILL_NUMBER VARCHAR(50), BILL_DATE DATE, PROVIDER_ID INT NOT NULL, CATEGORY_ID INT NOT NULL, SUMM DECIMAL(20,2), IS_PAID BOOLEAN DEFAULT FALSE, PAY_DATE DATE DEFAULT NULL)");
			stmt.addBatch("ALTER TABLE BILL ADD CONSTRAINT \"PROVIDERID\" FOREIGN KEY (PROVIDER_ID) REFERENCES PROVIDER (PROV_ID) ON DELETE RESTRICT");
			stmt.addBatch("ALTER TABLE BILL ADD CONSTRAINT \"CATEGORYID\" FOREIGN KEY (CATEGORY_ID ) REFERENCES CATEGORY (CAT_ID) ON DELETE RESTRICT");
			stmt.addBatch("ALTER TABLE BILL ADD CHECK PAY_DATE > BILL_DATE CHECK");
			stmt.addBatch("INSERT INTO CATEGORY ( CATEGORY_NAME) VALUES ('Phone')");
			stmt.addBatch("INSERT INTO PROVIDER ( PROVIDER_NAME ) VALUES ('Rnp')");
			stmt.addBatch("INSERT INTO BILL ( BILL_NUMBER , BILL_DATE , CATEGORY_ID , PROVIDER_ID , SUMM) VALUES  ('TEST', '2016-09-21', 1, 1, 20.25)");
			int[] count = stmt.executeBatch();
			stmt = null;
			closeConnection(conn);
			System.out.println("Should be inserted " + count.length + " records for Database structure creation.");
		} catch (SQLException e) {
			System.out.println("Failed to create a structure of the Database.");
		}
		
	}
}
