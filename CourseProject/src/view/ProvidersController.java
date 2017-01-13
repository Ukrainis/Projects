package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.Category;
import model.Provider;
import utils.Utils;

public class ProvidersController {	
	@FXML private TableView<Provider> providerTableView;
	@FXML private TableColumn<Provider, Integer> providerIdColumn;
	@FXML private TableColumn<Provider, String> providerNameColumn;
	@FXML private TextField newProviderName;
	@FXML private Button addProviderBtn;
	@FXML private Button deleteProviderBtn;
	
	public static ObservableList<Provider> providers = FXCollections.observableArrayList();
	
	@FXML void initialize() {
		providerTableView.itemsProperty().set(providers);
		providerIdColumn.cellValueFactoryProperty().set(x -> x.getValue().providerIdProperty().asObject());
		providerNameColumn.cellValueFactoryProperty().set(x -> x.getValue().providerNameProperty());
		Provider.getProviders();
		providerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			newProviderName.setText(providerTableView.getSelectionModel().getSelectedItem().providerNameProperty().get());
		});
	}
	
	@FXML void onProviderAdd() {
		boolean result = true;
		int selectedIndex = providerTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			// If no row is selected in Table view
			result = new Provider(newProviderName.getText()).insertDataIntoDB();
			System.out.println(result);
			providers.clear();
			BillsController.clearProviders();
			Provider.getProviders();
		} else {
			result = providerTableView.getSelectionModel().getSelectedItem().updateDataInDB();
		}
	}
	
	@FXML void onProviderDelete() {
		int selectedIndex = providerTableView.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	        boolean result = providerTableView.getSelectionModel().getSelectedItem().deleteDataFromDB();
	        providers.clear();
			BillsController.clearProviders();
			Provider.getProviders();
	        if(!result) errorMessageOnDelete();
	    } else {
	        errorMessageOnNotSelected();
	    }
	}
	
	public void errorMessageOnDelete() {
		Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Removing error!");
        alert.setHeaderText("Can't remove the provider.");
        alert.setContentText("Please verify, if this provider is not used anywhere in bills.");
        alert.showAndWait();
	}
	
	public void errorMessageOnNotSelected() {
		 Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Provider Selected");
	        alert.setContentText("Please select a provider in the table.");
	        alert.showAndWait();
	}
}
