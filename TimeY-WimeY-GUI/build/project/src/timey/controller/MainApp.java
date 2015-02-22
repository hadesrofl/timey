package timey.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.view.CategoryEditDialogController;
import timey.controller.view.CategoryRemoveDialogController;
import timey.controller.view.DatabaseLoginDialogController;
import timey.controller.view.DateViewController;
import timey.controller.view.DateEditDialogController;
import timey.controller.view.ServerLoginDialogController;
import timey.controller.view.TimeEditDialogController;
import timey.controller.model.DatabaseConnection;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static DatabaseConnection dbConn;
	private int userID;
	private FXMLLoader dateViewLoader;
	private Pane dateView;
	private DateViewController controller;
	// private File f = new File(File.separator + "resources" + File.separator +
	// "style.css");
	private String cssScene = this.getClass().getResource("style.css")
			.toExternalForm();
	// private String css = "style.css";
	// private String css = System.getProperty("user.dir") + File.separator +
	// "resources" + File.separator + "style.css";
	private File config = new File(System.getProperty("user.dir")
			+ File.separator + "config" + File.separator + "config.txt");

	public MainApp() {

	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TimeY");
		boolean okClicked = false;
		if (config.exists()) {
			boolean configRead = readConfig();
			if(configRead == true){
				initRootLayout();
				showDateView(null, null, 0);
			}else{
				Platform.exit();
			}
		} else {
			okClicked = showServerLoginDialog();
			if (okClicked == true) {
				okClicked = showDatabaseLoginDialog();
			}
			if (okClicked == true) {
				readConfig();
				initRootLayout();
				showDateView(null, null, 0);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
		if (dbConn != null) {
			dbConn.getConn().stop();
		}

	}

	private boolean readConfig() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(config));
			String text = "";
			String in = "";
			while ((in = br.readLine()) != null) {
				text = text + in;
			}
			br.close();
			String[] input = text.split("\"");
			dbConn = new DatabaseConnection(input[1],
					Integer.parseInt(input[3]), Integer.parseInt(input[5]),
					input[7], input[9], input[11], input[13], input[15]);
			userID = dbConn.getUserID(input[11]);
			return true;
		} catch (Exception e) {
			config.delete();
			e.printStackTrace();
			return false;
		}
	}

	public void initRootLayout() {
		try {
			// Load the RootLayout from fxml file
			System.out.println(cssScene);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Showing the scene on the stage
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(cssScene);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
			primaryStage.getStyle().getClass().getResource(cssScene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showDateView(LocalDate toPickerValue,
			LocalDate fromPickerValue, int selectIndex) {
		try {
			dateViewLoader = new FXMLLoader();
			dateViewLoader.setLocation(MainApp.class
					.getResource("view/DateView.fxml"));
			dateView = (Pane) dateViewLoader.load();
			rootLayout.setCenter(dateView);

			controller = dateViewLoader.getController();
			controller.setMainApp(this);
			controller.setDatePicker(toPickerValue, fromPickerValue);
			controller.setSelectionIndexDateTable(selectIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshDateView(LocalDate toPickerValue,
			LocalDate fromPickerValue, int selectIndex) {
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
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.setTitle("Edit Date");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			DateEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setDate(date);
			controller.setMainApp(this);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isSomethingChanged();
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
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.setTitle("Edit Time");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			TimeEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setDate(date, time);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isSomethingChanged();
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
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			if (newCategory) {
				dialogStage.setTitle("New Category");
			} else {
				dialogStage.setTitle("Edit Category");
			}
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			CategoryEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setNewDialog(newCategory);
			controller.setData();

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isSomethingChanged();
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
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.setTitle("Remove Category");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
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

	public boolean showServerLoginDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/ServerLoginDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.setTitle("Server Login");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			ServerLoginDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean showDatabaseLoginDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/DatabaseLoginDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.setTitle("Database Login");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			DatabaseLoginDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);

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

	public ObservableList<Date> getDates(String id1, String id2, int userID) {
		return dbConn.getDates(id1, id2, userID);
	}

	public DatabaseConnection getDBConn() {
		return dbConn;
	}

	public int getUserID() {
		return userID;
	}
}
