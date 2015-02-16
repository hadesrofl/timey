package timey.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
import timey.controller.view.DateViewController;
import timey.controller.view.DateEditDialogController;
import timey.controller.view.TimeEditDialogController;
import timey.controller.model.DatabaseConnection;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ObservableList<Date> dates = FXCollections.observableArrayList();
	private static DatabaseConnection dbConn;

	public MainApp() {
		
		dates = dbConn.getDates(20150210, 20150310);
//		Date d1 = new Date(20150210, 10, 2, 8, 2015, "Dienstag", "Februar",
//				false, false, null);
//		d1.addTime(new Time(d1.getCompleteDate(), LocalTime.of(10, 00),
//				LocalTime.of(12, 00), 2.0, "Arbeiten", "FHL Stuff", 20150210));
//		d1.addTime(new Time(d1.getCompleteDate(), LocalTime.of(13, 00),
//				LocalTime.of(15, 00), 2.0, "Zocken", "LoL", 20150210));
//		d1.addTime(new Time(d1.getCompleteDate(), LocalTime.of(16, 00),
//				LocalTime.of(18, 00), 2.0, "Arbeiten", "FHL Stuff", 20150210));
//		dates.add(d1);
//		Date d2 = new Date(20150211, 11, 2, 8, 2015, "Mittwoch", "Februar",
//				false, false, null);
//		d2.addTime(new Time(d1.getCompleteDate(), LocalTime.of(8, 00),
//				LocalTime.of(16, 00), 8.0, "Arbeiten", "FHL Stuff", 20150211));
//		dates.add(d2);
	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TimeY");

		initRootLayout();
		showDateView();
	}

	public static void main(String[] args) {
		dbConn = new DatabaseConnection(args[0],
				Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3],
				args[4], args[5], args[6], args[7]);
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

	public void showDateView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DateView.fxml"));
			Pane DateView;
			DateView = (Pane) loader.load();
			rootLayout.setCenter(DateView);

			DateViewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			controller.setDate(date, time);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Date> getDates() {
		return dates;
	}
	public DatabaseConnection getDBConn(){
		return dbConn;
	}
}
