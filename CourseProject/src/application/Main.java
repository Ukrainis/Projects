package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Accordion accord = new Accordion();
			// Adding bills titled pane fxml file
			TitledPane bills = (TitledPane)FXMLLoader.load(getClass().getResource("/view/bills.fxml"));
			// Adding providers titled pane fxml file
			TitledPane providers = (TitledPane)FXMLLoader.load(getClass().getResource("/view/providers.fxml"));
			// Adding categories titled pane from fxml file
			TitledPane categories = (TitledPane)FXMLLoader.load(getClass().getResource("/view/categories.fxml"));
			accord.getPanes().addAll(bills, providers, categories);
			StackPane rootPane = new StackPane();
			rootPane.getChildren().add(accord);
			Scene scene = new Scene(rootPane,620,450);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Program for bills accounting");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
