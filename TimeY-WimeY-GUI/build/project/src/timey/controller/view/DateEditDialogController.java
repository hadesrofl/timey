package timey.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timey.controller.MainApp;
import timey.controller.model.Date;

public class DateEditDialogController {
	@FXML
	private TextField eventField;
	@FXML
	private CheckBox holidayCB;
	private Stage dialogStage;
	private Date date;
	private boolean okClicked = false;
	private boolean updated = false;
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

	public void setDate(Date date) {
		this.date = date;
		String event = "";
		if (date.getBigEvent() != null)
			event = date.getBigEvent();
		else
			event = "";
		eventField.setText(event);
		holidayCB.setSelected(date.getHolidayFlag());
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	public boolean isSomethingChanged(){
		return updated;
	}

	@FXML
	private boolean handleOK() {
		date.setBigEvent(eventField.getText());
		date.setHolidayFlag(holidayCB.isSelected());
		String holidayFlag = "f";
		if(holidayCB.isSelected()){
		holidayFlag = "t";
		}else{
			holidayFlag = "f";
		}
		updated = mainApp.getDBConn().handleEditDate(holidayFlag, eventField.getText(), mainApp.getUserID(), date.getCalendarID());
		okClicked = true;
		dialogStage.close();
		return updated;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleCheckBox() {
		if (holidayCB.isSelected()) {
			holidayCB.setSelected(false);
		} else {
			holidayCB.setSelected(true);
		}
	}
}
