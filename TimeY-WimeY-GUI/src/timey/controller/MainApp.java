package timey.controller;

import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.view.CategoryEditDialogController;
import timey.controller.view.CategoryRemoveDialogController;
import timey.controller.view.DateViewController;
import timey.controller.view.DateEditDialogController;
import timey.controller.view.TimeEditDialogController;
import timey.controller.model.DatabaseConnection;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static DatabaseConnection dbConn;
	private int userID = 1;

	public MainApp() {

	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TimeY");

		initRootLayout();
		showDateView(null, null, 0);
	}

	public static void main(String[] args) {
		dbConn = new DatabaseConnection(args[0], Integer.parseInt(args[1]),
				Integer.parseInt(args[2]), args[3], args[4], args[5], args[6],
				args[7]);
		launch(args);
		dbConn.getConn().stop();
	}

	public void initRootLayout() {
		try {
			// Load the RootLayout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Showing the scene on the stage
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showDateView(LocalDate toPickerValue, LocalDate fromPickerValue, int selectIndex) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DateView.fxml"));
			Pane DateView = (Pane) loader.load();
			rootLayout.setCenter(DateView);
			
			DateViewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDatePicker(toPickerValue, fromPickerValue);
			controller.setSelectionIndexDateTable(selectIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshDateView(LocalDate toPickerValue, LocalDate fromPickerValue, int selectIndex){
		showDateView(toPickerValue, fromPickerValue, selectIndex);
		
	}

	public boolean showDateEditDialog(Date date) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/DateEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Date");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the date into the controller
			DateEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDate(date);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean showTimeEditDialog(Date date, Time time) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/TimeEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Time");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the date into the controller
			TimeEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setDate(date, time);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.somethingIsChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean showCategoryEditDialog(boolean newCategory) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/CategoryEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			if (newCategory) {
				dialogStage.setTitle("New Category");
			} else {
				dialogStage.setTitle("Edit Category");
			}
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the date into the controller
			CategoryEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setNewDialog(newCategory);
			controller.setData();

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.somethingIsChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean showCategoryRemoveDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/CategoryRemoveDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Remove Category");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the date into the controller
			CategoryRemoveDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setData();

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isRemoveClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	


	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Date> getDates(String id1, String id2, int userID) {
		return dbConn.getDates(id1, id2, userID);
	}

	public DatabaseConnection getDBConn() {
		return dbConn;
	}
	
	public int getUserID(){
		return userID;
	}
}
