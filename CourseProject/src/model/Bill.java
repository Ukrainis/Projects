package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Utils;
import view.BillsController;

public class Bill implements DataBaseMethods {
	private SimpleIntegerProperty billId;
	private SimpleStringProperty billNumber;
	private SimpleStringProperty billDate;
	private SimpleStringProperty billProvider;
	private SimpleStringProperty billCategory;
	private SimpleBooleanProperty billPaid;
	private SimpleStringProperty billPayDate;
	private SimpleDoubleProperty billSumm;
	
	public Bill(String billNumber, String billDate, String billProvider, String billCategory, double billSumm) {
		this.billNumber = new SimpleStringProperty(billNumber);
		System.out.println("Bill number: " + billNumber);
		this.billDate = new SimpleStringProperty(billDate);
		System.out.println("Bill date: " + billDate);
		this.billProvider = new SimpleStringProperty(billProvider);
		System.out.println("Bill provider: " + billProvider);
		this.billCategory = new SimpleStringProperty(billCategory);
		System.out.println("Bill category: " + billCategory);
		this.billSumm = new SimpleDoubleProperty(billSumm);
		System.out.println("Bill summ: " + billSumm);
	}
	
	public Bill(int billId, String billNumber, String billDate, String billProvider, String billCategory, 
			boolean billPaid, String billPayDate, double billSumm) {
		this.billId = new SimpleIntegerProperty(billId);
		this.billNumber = new SimpleStringProperty(billNumber);
		this.billDate = new SimpleStringProperty(billDate);
		this.billProvider = new SimpleStringProperty(billProvider);
		this.billCategory = new SimpleStringProperty(billCategory);
		this.billPaid = new SimpleBooleanProperty(billPaid);
		this.billPayDate = new SimpleStringProperty(billPayDate);
		this.billSumm = new SimpleDoubleProperty(billSumm);
	}
	
	public int getBillId() {
		return billId.get();
	}
	
	public SimpleIntegerProperty billIdProperty() {
		return billId;
	}
	
	public String getBillNumber() {
		return billNumber.get();
	}
	
	public SimpleStringProperty billNumberProperty() {
		return billNumber;
	}
	
	public String getBillDate() {
		return billDate.get();
	}
	
	public SimpleStringProperty billDateProperty() {
		return billDate;
	}
	
	public String getBillProvider() {
		return billProvider.get();
	}
	
	public SimpleStringProperty billProviderProperty() {
		return billProvider;
	}
	
	public String getBillCategory() {
		return billCategory.get();
	}
	
	public SimpleStringProperty billCategoryProperty() {
		return billCategory;
	}
	
	public boolean getBillPaid() {
		return billPaid.get();
	}
	
	public SimpleBooleanProperty billPaidProperty() {
		return billPaid;
	}
	
	public String getBillPayDate() {
		return billPayDate.get();
	}
	
	public SimpleStringProperty billPayDateProperty() {
		return billPayDate;
	}
	
	public double getBillSumm() {
		return billSumm.get();
	}
	
	public SimpleDoubleProperty billSummProperty() {
		return billSumm;
	}

	@Override
	public boolean insertDataIntoDB() {
		Connection conn = Utils.getDbConnection();
		PreparedStatement stmt = null;
		String query = "INSERT INTO BILL ( BILL_NUMBER , BILL_DATE , PROVIDER_ID , CATEGORY_ID , "
				+ "SUMM) VALUES (?, ?, "
				+ "SELECT PROV_ID FROM PROVIDER WHERE PROVIDER_NAME = ?, "
				+ "SELECT CAT_ID FROM CATEGORY WHERE CATEGORY_NAME = ?, "// TODO insert as Strings
				+ "?)";
		try {
			System.out.println("Begining.");
			System.out.println("Query for Prepared statement:\n" + query);
			stmt = conn.prepareStatement(query);
			System.out.println("Prepared statement created.");
			stmt.setString(1, this.getBillNumber());
			Timestamp date = Timestamp.valueOf(LocalDate.parse(this.getBillDate()).atStartOfDay());
			stmt.setTimestamp(2, date);
			stmt.setString(3, this.getBillProvider());
			stmt.setString(4, this.getBillCategory());
			stmt.setDouble(5, this.getBillSumm());
			stmt.executeUpdate();
			System.out.println("Executing query.");
		} catch (SQLException e) {
			System.out.println("Can't create prepared statement or execute query: " + query);
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteDataFromDB() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static void getBills() {
		Connection conn = Utils.getDbConnection();
		String query = "SELECT BILL_ID, BILL_NUMBER, BILL_DATE, PROV.PROVIDER_NAME AS PROV_NAME, "
				+ "CAT.CATEGORY_NAME AS CAT_NAME, SUMM, IS_PAID, PAY_DATE FROM BILL, PROVIDER PROV, CATEGORY CAT "
				+ "WHERE PROV.PROV_ID=PROVIDER_ID AND CAT.CAT_ID = CATEGORY_ID";
		try {
			Statement stmt = conn.createStatement();
			System.out.println("Statement was created.");
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Executing query.");
			while(rs.next()) {
				BillsController.bills.add(new Bill(rs.getInt("bill_id"), rs.getString("bill_number"), 
						rs.getString("bill_date"), rs.getString("prov_name"), rs.getString("cat_name"), 
						rs.getBoolean("is_paid"), rs.getString("pay_date"), rs.getDouble("summ")));
				System.out.println(rs.getInt("bill_id") + "\t" + rs.getString("bill_number") + "\t" + rs.getString("bill_date") + "\t" +
						"\t" + rs.getString("prov_name") + "\t" + rs.getString("cat_name") + "\t" + rs.getBoolean("is_paid") + "\t" +
			rs.getString("pay_date") + "\t" + rs.getDouble("summ"));
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
