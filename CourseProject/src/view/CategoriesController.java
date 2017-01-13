package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Bill;
import model.Category;
import utils.Utils;

public class CategoriesController {
	@FXML private TableView<Category> categoryTableView;
	@FXML private TableColumn<Category, Integer> categoriIdTableColumn;
	@FXML private TableColumn<Category, String> categoryNameTableColumn;
	@FXML private TextField newCategoryNameTxt;
	@FXML private Button submitCategoryBtn;
	@FXML private Button deleteCategoryBtn;
	
	public static ObservableList<Category> categorys = FXCollections.observableArrayList();
	
	@FXML void initialize() {
		categoryTableView.itemsProperty().set(categorys);
		categoriIdTableColumn.setCellValueFactory(x -> x.getValue().categoryIdProperty().asObject());
		categoryNameTableColumn.setCellValueFactory(x -> x.getValue().categoryNameProperty());
		Category.getCategorys();
		categoryTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			newCategoryNameTxt.setText(categoryTableView.getSelectionModel().getSelectedItem().categoryNameProperty().get());
		});
	}
	
	@FXML void onCategoryAdd() {
		boolean result = new Category(newCategoryNameTxt.getText()).insertDataIntoDB();
		categorys.clear();
		BillsController.clearCategorys();
		Category.getCategorys();
	}
	
	@FXML void onCategoryDelete() {
		int selectedIndex = categoryTableView.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	        Category category = categoryTableView.getSelectionModel().getSelectedItem();
	        boolean result = category.deleteDataFromDB();
	        categorys.clear();
			BillsController.clearCategorys();
			Category.getCategorys();
	        if(!result) errorMessageOnDelete();
	    } else {
	        errorMessageOnNotSelected();
	    }
	}
	
	public void errorMessageOnDelete() {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Removing error!");
        alert.setHeaderText("Can't remove the category.");
        alert.setContentText("Please verify, if this category is not used anywhere.");
        alert.showAndWait();
	}
	
	public void errorMessageOnNotSelected() {
		 Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Category Selected");
	        alert.setContentText("Please select a category in the table.");
	        alert.showAndWait();
	}
}
