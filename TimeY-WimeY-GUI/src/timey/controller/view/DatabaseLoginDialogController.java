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

public class DatabaseLoginDialogController {
	@FXML
	private TextField dbuserField;
	@FXML
	private PasswordField dbpasswordField;
	@FXML
	private TextField dbNameField;
	@FXML
	private Label explanationLabel;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean createDatabase = false;
	private MainApp mainApp;

	@FXML
	private void initialize() {

	}

	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

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
	private void handleOK() {
		if (createDatabase == false) {
			try {
				File folder = new File(System.getProperty("user.dir")
						+ File.separator + "config");
				if (folder.exists() == false)
					folder.mkdir();
				File config = new File(System.getProperty("user.dir")
						+ File.separator + "config" + File.separator
						+ "config.txt");
				if (config.exists() == false) {
					config.createNewFile();
				} else {
					// config.delete();
					// config.createNewFile();
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
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleCreateUser() {

	}
}
