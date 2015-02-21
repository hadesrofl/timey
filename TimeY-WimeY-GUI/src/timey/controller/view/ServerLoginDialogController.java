package timey.controller.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import timey.controller.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerLoginDialogController {
	@FXML
	private TextField remoteHostField;
	@FXML
	private TextField remotePortField;
	@FXML
	private TextField localPortField;
	@FXML
	private TextField sshUserField;
	@FXML
	private PasswordField sshPasswordField;

	private Stage dialogStage;
	private boolean okClicked = false;
	private MainApp mainApp;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOK() {
		try {
			File folder = new File(System.getProperty("user.dir")
					+ File.separator + "config");
			if (folder.exists() == false)
				folder.mkdir();
			File config = new File(System.getProperty("user.dir")
					+ File.separator + "config" + File.separator + "config.txt");
			if (config.exists() == false) {
				config.createNewFile();
			} else {
				config.delete();
				config.createNewFile();
			}
			PrintWriter pw = new PrintWriter(new FileWriter(config));
			StringBuilder sb = new StringBuilder();
			sb.append("Remote Host: " + "\"" + remoteHostField.getText() + "\""  + " \n");
			sb.append("Remote Port: " + "\"" + remotePortField.getText() + "\"" + " \n");
			sb.append("Local Port: " + "\"" + localPortField.getText() + "\"" + " \n");
			sb.append("SSH User: " + "\"" + sshUserField.getText() + "\"" + " \n");
			sb.append("SSH Password: " + "\"" + sshPasswordField.getText() + "\"" + " \n");
			pw.println(sb.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		okClicked = true;

		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private void setFocusListenerToTextField(TextField textField) {
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("Textfield on focus");

				} else {
					System.out.println("Textfield out focus");
				}
			}
		});
	}
}
