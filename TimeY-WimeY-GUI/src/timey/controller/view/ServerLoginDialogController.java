package timey.controller.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.fxml.FXML;
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
 * <b>File:</b> ServerLoginDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 13:10:18
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Server Login Dialog
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class ServerLoginDialogController {
	@FXML
	/**
	 * Textfield for the remote Host
	 */
	private TextField remoteHostField;
	@FXML
	/**
	 * Textfield for the remote Port
	 */
	private TextField remotePortField;
	@FXML
	/**
	 * Textfield for the local Port
	 */
	private TextField localPortField;
	@FXML
	/**
	 * Textfield for the sshUser
	 */
	private TextField sshUserField;
	@FXML
	/**
	 * PasswordField for the sshPassword
	 */
	private PasswordField sshPasswordField;
	/**
	 * Stage for this dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean for the ok button
	 */
	private boolean okClicked = false;

	@FXML
	/**
	 * Initializes this dialog
	 */
	private void initialize() {
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
	 * Getter of okClicked
	 * 
	 * @return okClicked
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	/**
	 * Writes Field Values into a config file
	 */
	private void handleOK() {
		try {
			File folder = new File(System.getProperty("user.dir")
					+ File.separator + "config");
			if (folder.exists() == false)
				folder.mkdir();
			File config = new File(System.getProperty("user.dir")
					+ File.separator + "config" + File.separator + "config.txt");
			// config fiel does not exists
			if (config.exists() == false) {
				config.createNewFile();
			}
			// deletes existing config file if there is new data for it
			else {
				config.delete();
				config.createNewFile();
			}
			PrintWriter pw = new PrintWriter(new FileWriter(config));
			StringBuilder sb = new StringBuilder();
			sb.append("Remote Host: " + "\"" + remoteHostField.getText() + "\""
					+ " \n");
			sb.append("Remote Port: " + "\"" + remotePortField.getText() + "\""
					+ " \n");
			sb.append("Local Port: " + "\"" + localPortField.getText() + "\""
					+ " \n");
			sb.append("SSH User: " + "\"" + sshUserField.getText() + "\""
					+ " \n");
			sb.append("SSH Password: " + "\"" + sshPasswordField.getText()
					+ "\"" + " \n");
			pw.println(sb.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		okClicked = true;

		dialogStage.close();
	}

	@FXML
	/**
	 * Closes stage on cancel
	 */
	private void handleCancel() {
		dialogStage.close();
	}
}
