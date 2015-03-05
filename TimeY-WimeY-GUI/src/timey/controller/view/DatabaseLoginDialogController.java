package timey.controller.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import timey.controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> DatabaseLoginDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 12:54:18
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Database Login Dialog,
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class DatabaseLoginDialogController {
	@FXML
	/**
	 * Textfield for the dbUser
	 */
	private TextField dbuserField;
	@FXML
	/**
	 * Textfield for the dbPassword
	 */
	private PasswordField dbpasswordField;
	@FXML
	/**
	 * Textfield for the dbName
	 */
	private TextField dbNameField;
	@FXML
	/**
	 * Label for the explanation of this dialog
	 */
	private Label explanationLabel;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean for the ok button
	 */
	private boolean okClicked = false;
	/**
	 * Boolean if a new database shall be created TODO: Implement function
	 */
	private boolean createDatabase = false;
	@FXML
	/**
	 * Initializes this dialog
	 */
	private void initialize() {

	}

	/**
	 * Sets the MainApp to this dialog
	 * 
	 * @param main
	 *            is the mainApp of TimeY-WimeY
	 */
	public void setMainApp(MainApp main) {
	}

	/**
	 * Sets the stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage for this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Getter of isOkClicked
	 * 
	 * @return isOkClicked
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Sets the value of createDatabase
	 * 
	 * @param createDatabase
	 *            is a boolean for creating a Database or not
	 */
	public void setCreateDatabase(boolean createDatabase) {
		this.createDatabase = createDatabase;
		if (createDatabase == true) {
			explanationLabel
					.setText("Please enter user and password for a database user who can create a new schema. The database name is the name of the new database.");
		} else {
			explanationLabel
					.setText("Please enter the user and password for the database. The database name is the name of the database which should be used.");
		}
	}

	@FXML
	/**
	 * Handles the action if ok is clicked
	 * TODO: Implement Function to create a new database
	 */
	private void handleOK() {
		// no new database shall be created
		if (createDatabase == false) {
			try {
				File folder = new File(System.getProperty("user.dir")
						+ File.separator + "config");
				if (folder.exists() == false)
					folder.mkdir();
				File config = new File(System.getProperty("user.dir")
						+ File.separator + "config" + File.separator
						+ "config.txt");
				// there is no config file
				if (config.exists() == false) {
					config.createNewFile();
				}
				PrintWriter pw = new PrintWriter(new FileWriter(config, true));
				StringBuilder sb = new StringBuilder();
				sb.append("Database User: " + "\"" + dbuserField.getText()
						+ "\"" + " \n");
				sb.append("Database Password: " + "\""
						+ dbpasswordField.getText() + "\"" + " \n");
				sb.append("Database Name: " + "\"" + dbNameField.getText()
						+ "\"" + " \n");
				pw.println(sb.toString());
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.getStackTrace();
			}
		} else {

		}
		okClicked = true;
		dialogStage.close();

	}

	@FXML
	/**
	 * Close Stage on cancel
	 */
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	/**
	 * TODO: Implement Function to create a new user in a database
	 */
	private void handleCreateUser() {

	}
}
