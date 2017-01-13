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
import view.CategoriesController;

public class Category implements DataBaseMethods {
	private SimpleIntegerProperty categoryId;
	private SimpleStringProperty categoryName;
	// Constructor for returning data from DB
	public Category(int categoryId, String categoryName) {
		this.categoryId = new SimpleIntegerProperty(categoryId);
		this.categoryName = new SimpleStringProperty(categoryName);
	}
	// Constructor for adding data in DB
	public Category(String categoryName) {
		this.categoryName = new SimpleStringProperty(categoryName);
	}
	
	public int getCategoryId() {
		return categoryId.get();
	}
	
	public SimpleIntegerProperty categoryIdProperty() {
		return categoryId;
	}
	
	public String getCategoryName() {
		return categoryName.get();
	}
	
	public SimpleStringProperty categoryNameProperty() {
		return categoryName;
	}
	
	@Override
	public boolean insertDataIntoDB() {
		boolean result = false;
		Connection conn = Utils.getDbConnection();
		PreparedStatement stmt = null;
		String query = "INSERT INTO CATEGORY (CATEGORY_NAME) VALUES (?)";
		try {
			System.out.println("Begining.");
			stmt = conn.prepareStatement(query);
			System.out.println("Prepared statement created.");
			System.out.println("Setting parameter as - " + this.getCategoryName());
			stmt.setString(1, this.getCategoryName());
			stmt.executeUpdate();
			System.out.println("Query is executed.");
			result = true;
			Utils.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("Can't create prepared statement or execute query: " + query);
			return result;
		}
		return result;
	}
	
	@Override
	public boolean deleteDataFromDB() {
		boolean result = false;
		Connection conn = Utils.getDbConnection();
		PreparedStatement pstmt = null;
		String query = "DELETE FROM CATEGORY WHERE CAT_ID = ?";
		try {
			System.out.println("Begining.");
			pstmt = conn.prepareStatement(query);
			System.out.println("Prepared statement created.");
			System.out.println("Setting parameter as - " + this.getCategoryId());
			pstmt.setInt(1, this.getCategoryId());
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
	
	public static void getCategorys() {
		Connection conn = Utils.getDbConnection();
		String query = "SELECT * FROM CATEGORY";
		try {
			Statement stmt = conn.createStatement();
			System.out.println("Statement was created.");
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Query is executed.");
			while(rs.next()) {
				CategoriesController.categorys.add(new Category(rs.getInt("cat_id"), rs.getString("category_name")));
				BillsController.fillCategories(rs.getString("category_name"));
				System.out.println(rs.getInt("cat_id") + "\t" + rs.getString("category_name"));
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
		// TODO Auto-generated method stub
		return false;
	}

}
