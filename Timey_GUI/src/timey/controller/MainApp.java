package timey.controller;

import java.io.IOException;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.view.DateViewController;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ObservableList<Date> dates = FXCollections.observableArrayList();

	public MainApp(){
		Date d1 = new Date(20150210, 10, 2, 8, 2015, "Dienstag", "Februar", false, false, null);
		d1.addTime(new Time(d1.getCompleteDate(), LocalTime.of(10, 00), LocalTime.of(12,00), 2.0, "Arbeiten", "FHL Stuff", 20150210));
		d1.addTime(new Time(d1.getCompleteDate(), LocalTime.of(13, 00), LocalTime.of(18,00), 5.0, "Zocken", "LoL", 20150210));
		dates.add(d1);
		dates.add(new Date(20150211, 11, 2, 8, 2015, "Mittwoch", "Februar", false, false, null));
	}
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TimeY");

		initRootLayout();
		showDateView();
	}

	public static void main(String[] args) {
		launch(args);
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

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public ObservableList<Date> getDates(){
		return dates;
	}
}
