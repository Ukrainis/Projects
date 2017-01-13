package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Popup;
import model.Bill;
import model.Category;
import model.Provider;
import utils.Utils;

public class BillsController {
	@FXML private DatePicker dateFilterDatePicker;
	@FXML private ComboBox<String> providerFilterComboBox;
	@FXML private ComboBox<String> categoryFilterComboBox;
	@FXML private CheckBox isPaidFilterCheckbox;
	@FXML private Button deleteBillBtn;
	@FXML private TableView<Bill> billTableView;
	@FXML private TableColumn<Bill, Integer> billIdTableColumn;
	@FXML private TableColumn<Bill, String> billNumberTableColumn;
	@FXML private TableColumn<Bill, String> billDateTableColumn;
	@FXML private TableColumn<Bill, String> billProviderTableColumn;
	@FXML private TableColumn<Bill, String> billCategoryTableColumn;
	@FXML private TableColumn<Bill, Boolean> billPaidTableColumn;
	@FXML private TableColumn<Bill, String> billDatePayTableColumn;
	@FXML private TableColumn<Bill, Double> billSummTableColumn;
	@FXML private TextField newBillNumberTxt;
	@FXML private DatePicker newBillDate;
	@FXML private ComboBox<String> newBillProvider;
	@FXML private ComboBox<String> newBillCategory;
	@FXML private TextField newBillSumm;
	@FXML private Button submitBillBtn;
	@FXML private TextField toPayTxt;
	
	public static ObservableList<Bill> bills = FXCollections.observableArrayList();
	public static ObservableList<String> dropdownCategorys = FXCollections.observableArrayList();
	public static ObservableList<String> dropdownProviders = FXCollections.observableArrayList();
	
	@FXML void initialize() {
		billTableView.itemsProperty().set(bills);
		newBillCategory.itemsProperty().set(dropdownCategorys);
		newBillProvider.itemsProperty().set(dropdownProviders);
		categoryFilterComboBox.itemsProperty().set(dropdownCategorys);
		providerFilterComboBox.itemsProperty().set(dropdownProviders);
//		providerFilterComboBox.valueProperty().addListener(arg0); TODO for filtering. Create after Insert into DB method will be ready.
		Bill.getBills();
		System.out.println("Bill size - " + bills.size());
		billIdTableColumn.setCellValueFactory(x -> x.getValue().billIdProperty().asObject());
		billNumberTableColumn.setCellValueFactory(x -> x.getValue().billNumberProperty());
		billDateTableColumn.setCellValueFactory(x -> x.getValue().billDateProperty());
		billProviderTableColumn.setCellValueFactory(x -> x.getValue().billProviderProperty());
		billCategoryTableColumn.setCellValueFactory(x -> x.getValue().billCategoryProperty());
		billPaidTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn((i) -> bills.get(i).billPaidProperty()));
		billSummTableColumn.setCellValueFactory(x -> x.getValue().billSummProperty().asObject());
		billDatePayTableColumn.setCellValueFactory(x -> x.getValue().billPayDateProperty());
		newBillDate.setOnAction(event -> {
			LocalDate date = newBillDate.getValue();
			System.out.println("Date: " + date);
			System.out.println("Date: " + date.toString());
		});
		newBillProvider.setOnAction(event -> {
			System.out.println(newBillProvider.getValue());
		});
	}
	
	@FXML
	public void showPopup() {
		new Bill(newBillNumberTxt.getText(), newBillDate.getValue().toString(), 
				newBillProvider.getValue(), 
				newBillCategory.getValue(), 
				Double.parseDouble(newBillSumm.getText())).insertDataIntoDB();
		clearBills();
		Bill.getBills();
/*		Bill selected = billTableView.getSelectionModel().getSelectedItem();
	    System.out.println("CheckBox Action (selected: " + selected.getBillPaid() + ")");
	    selected.billPaidProperty().set(true);
	    System.out.println("CheckBox Action (selected: " + selected.getBillPaid() + ")");*/
	}// TODO Create an Update method if fields for entering a new bill are empty, in other - insert a new bill.
	
	public void clearBills() {
		bills.clear();
	}
	
	public static void clearCategorys() {
		dropdownCategorys.clear();
	}
	
	public static void fillCategories(String category) {
		dropdownCategorys.add(category);
	}
	
	public static void clearProviders() {
		dropdownProviders.clear();
	}
	
	public static void fillProviders(String category) {
		dropdownProviders.add(category);
	}

}
