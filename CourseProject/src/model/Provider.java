package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import utils.Utils;
import view.BillsController;
import view.ProvidersController;

public class Provider implements DataBaseMethods {
	private SimpleIntegerProperty providerId;
	private SimpleStringProperty providerName;
	// Constructor for returning data from DB
	public Provider(int categoryId, String categoryName) {
		this.providerId = new SimpleIntegerProperty(categoryId);
		this.providerName = new SimpleStringProperty(categoryName);
	}
	// Constructor for adding data in DB
	public Provider(String categoryName) {
		this.providerName = new SimpleStringProperty(categoryName);
	}
	
	public int getProviderId() {
		return providerId.get();
	}
	
	public SimpleIntegerProperty providerIdProperty() {
		return providerId;
	}
	
	public String getProviderName() {
		return providerName.get();
	}
	
	public SimpleStringProperty providerNameProperty() {
		return providerName;
	}
	@Override
	public boolean insertDataIntoDB() {
		boolean result = true;
		Connection conn = Utils.getDbConnection();
		PreparedStatement stmt = null;
		String query = "INSERT INTO PROVIDER (PROVIDER_NAME) VALUES (?)";
		try {
			System.out.println("Begining.");
			stmt = conn.prepareStatement(query);
			System.out.println("Prepared statement created.");
			System.out.println("Setting parameter as - " + this.getProviderName());
			stmt.setString(1, this.getProviderName());
			stmt.executeUpdate();
			System.out.println("Executing query.");
		} catch (SQLException e) {
			System.out.println("Can't create prepared statement or execute query: " + query);
			return false;
		}
		return result;
	}
	@Override
	public boolean deleteDataFromDB() {
		boolean result = false;
		Connection conn = Utils.getDbConnection();
		PreparedStatement pstmt = null;
		String query = "DELETE FROM PROVIDER WHERE PROV_ID = ?";
		try {
			System.out.println("Begining.");
			pstmt = conn.prepareStatement(query);
			System.out.println("Prepared statement created.");
			System.out.println("Setting parameter as - " + this.getProviderId());
			pstmt.setInt(1, this.getProviderId());
			pstmt.executeUpdate();
			System.out.println("Query is executed.");
			result = true;
			Utils.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("Can't create prepared statement or execute query: " + query);
			return result;
		}
		return result;
	}
	
	public static void getProviders() {
		Connection conn = Utils.getDbConnection();
		String query = "SELECT * FROM PROVIDER";
		try {
			Statement stmt = conn.createStatement();
			System.out.println("Statement was created.");
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Executing query.");
			while(rs.next()) {
				ProvidersController.providers.add(new Provider(rs.getInt("prov_id"), rs.getString("provider_name")));
				BillsController.fillProviders(rs.getString("provider_name"));
				System.out.println(rs.getInt("prov_id") + "\t" + rs.getString("provider_name"));
			}
			Utils.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("Can't create statement or execute query: " + query);
		} finally {
			Utils.closeConnection(conn);
		}
	}
	@Override
	public boolean updateDataInDB() {
		boolean result = false;
		Connection conn = Utils.getDbConnection();
		return result;
	}
}
