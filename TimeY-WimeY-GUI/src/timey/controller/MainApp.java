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
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.view.AnalyzeCategoryDialogController;
import timey.controller.view.CategoryEditDialogController;
import timey.controller.view.CategoryRemoveDialogController;
import timey.controller.view.DatabaseLoginDialogController;
import timey.controller.view.DateViewController;
import timey.controller.view.DateEditDialogController;
import timey.controller.view.PlanHolidayDialogController;
import timey.controller.view.ServerLoginDialogController;
import timey.controller.view.TimeEditDialogController;
import timey.controller.model.DatabaseConnection;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller
 * </p>
 * <p>
 * <b>File:</b> MainApp.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 10:44:58
 * </p>
 * <b>Description:</b>
 * <p>
 * TimeY-WimeY is a Time Tracker developed for private purposes and training in
 * JavaFX. The app offers a GUI and a connection to a remote SQL Database to
 * store the Dates and Times of a user. The Main App is the controller of this
 * app.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class MainApp extends Application {
	/**
	 * primary stage to show the scenes in
	 */
	private Stage primaryStage;
	/**
	 * root pane of the gui
	 */
	private BorderPane rootLayout;
	/**
	 * connection to the remote sql database
	 */
	private static DatabaseConnection dbConn;
	/**
	 * user id of the current active user of this app
	 */
	private int userID;
	/**
	 * loader for fxml files for the gui
	 */
	private FXMLLoader dateViewLoader;
	/**
	 * main frame of the gui
	 */
	private Pane dateView;
	/**
	 * controller of the main view
	 */
	private DateViewController controller;
	/**
	 * path to style sheet
	 */
	private String cssScene = this.getClass().getResource("style.css")
			.toExternalForm();
	/**
	 * path to the configuration file of the remote connection to database TODO:
	 * Hash password
	 */
	private File config = new File(System.getProperty("user.dir")
			+ File.separator + "config" + File.separator + "config.txt");

	/**
	 * Constructor
	 */
	public MainApp() {

	}

	/**
	 * start method for creating a stage with root pane. Also reads the config
	 * file or shows a dialog to enter new login details. Deletes the config
	 * file if there is an exception connecting to the database.
	 */
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TimeY");
		boolean okClicked = false;
		if (config.exists()) {
			boolean configRead = readConfig();
			// connection established
			if (configRead == true) {
				initRootLayout();
				showDateView(null, null, 0);

			}
			// connection not possible
			else {
				config.delete();
				Platform.exit();
			}

		}
		// there is no configuration file
		else {
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

	/**
	 * Main Method to launch the gui and stop the database connection if the
	 * programm will be closed
	 * 
	 * @param args
	 *            is not used
	 */
	public static void main(String[] args) {
		launch(args);
		if (dbConn != null) {
			dbConn.getConn().stop();
		}

	}

	/**
	 * Reads the config file and return true if a connection to the database
	 * could be established or false if not
	 * 
	 * @return true if connection is established, false if not
	 */
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

	/**
	 * Initializes the root pane
	 */
	public void initRootLayout() {
		try {
			// Load the RootLayout from fxml file
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

	/**
	 * Shows the DateView Scene
	 * 
	 * @param toPickerValue
	 *            is the value of the date picker for the end date
	 * @param fromPickerValue
	 *            is the value of the date picker for the start date
	 * @param selectIndex
	 *            is the index in the date table
	 */
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

	/**
	 * Refreshes the date view scene
	 * 
	 * @param toPickerValue
	 *            is the value of the date picker for the end date
	 * @param fromPickerValue
	 *            is the value of the date picker for the start date
	 * @param selectIndex
	 *            is the index of the selected date
	 */
	public void refreshDateView(LocalDate toPickerValue,
			LocalDate fromPickerValue, int selectIndex) {
		showDateView(toPickerValue, fromPickerValue, selectIndex);

	}

	/**
	 * Shows the DateEdit Dialog
	 * 
	 * @param date
	 *            is the date that shall be edited
	 * @return true if there was an update, false if not
	 */
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
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
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

	/**
	 * Shows the TimeEditDialog
	 * 
	 * @param date
	 *            is the date which contains the time
	 * @param time
	 *            is the time that shall be edited
	 * @return true if there was a change, false if not
	 */
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
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
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

	/**
	 * Shows the CategoryEdit Dialog
	 * 
	 * @param newCategory
	 *            is the category which shall be edited
	 * @return true if there was an update, false if not
	 */
	public boolean showCategoryEditDialog(boolean newCategory) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/CategoryEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
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

	/**
	 * Shows the CategoryRemoveDialog
	 * 
	 * @return true if the category was removed, false if not
	 */
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
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
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

	/**
	 * Shows the PlanHolidayDialog
	 * 
	 * @param from
	 *            is the start date
	 * @param to
	 *            is the end date
	 * @return true if there were updates, false if not
	 */
	public boolean showPlanHolidayDialog(LocalDate from, LocalDate to) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/PlanHolidayDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
			dialogStage.setTitle("Plan Holidays");

			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			PlanHolidayDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setData(from, to);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isSomethingChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Shows the AnalyzeCategoryDialog
	 * 
	 * @param from
	 *            is the start date
	 * @param to
	 *            is the end date
	 */
	public void showAnalyzeCategoryDialog(LocalDate from, LocalDate to) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/AnalyzeCategoryDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create DiagloStage
			Stage dialogStage = new Stage();
			dialogStage.getStyle().getClass().getResource(cssScene);
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
			dialogStage.setTitle("Analyze Category");

			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			AnalyzeCategoryDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setData(from, to);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Shows the LoginDialog for the server details
	 * 
	 * @return true if ok is clicked, false if not
	 */
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
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
			Scene scene = new Scene(page);
			scene.getStylesheets().add(cssScene);
			dialogStage.setScene(scene);

			// Set the date into the controller
			ServerLoginDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show dialog and wait until user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Shows the LoginDialog for the database details
	 * 
	 * @return true if ok is clicked, false if not
	 */
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
			dialogStage.getIcons().add(
					new Image(MainApp.class.getResourceAsStream("icon.png")));
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

	/**
	 * Getter for the primary stage
	 * 
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Gets the Dates of the database
	 * 
	 * @param id1
	 *            is the id of the start date
	 * @param id2
	 *            is the id of the end date
	 * @param userID
	 *            is the userID
	 * @return a list of dates
	 */
	public ObservableList<Date> getDates(String id1, String id2, int userID) {
		return dbConn.getDates(id1, id2, userID);
	}

	/**
	 * Getter of the database connection
	 * 
	 * @return the database connection object
	 */
	public DatabaseConnection getDBConn() {
		return dbConn;
	}

	/**
	 * Getter of the user id of the current active user in this app
	 * 
	 * @return the user id as an integer
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Creates a id of a date in the database from a value of a date picker
	 * 
	 * @param datePicker
	 *            from which a id shall be created
	 * @return a id of a date in the database as String
	 */
	public String createCalendarIDFromDatePicker(DatePicker datePicker) {
		String id = "";
		if (datePicker.getValue() != null) {
			String year = datePicker.getValue().getYear() + "";
			String month = datePicker.getValue().getMonthValue() + "";
			if (Integer.parseInt(month) < 10)
				month = 0 + month;
			String day = datePicker.getValue().getDayOfMonth() + "";
			if (Integer.parseInt(day) < 10)
				day = 0 + day;
			id = year + month + day;
		}
		return id;
	}
}
