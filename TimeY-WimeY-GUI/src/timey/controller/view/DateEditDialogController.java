package timey.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timey.controller.model.Date;

public class DateEditDialogController {
	@FXML
	private TextField eventField;
	@FXML
	private CheckBox holidayCB;
	private Stage dialogStage;
	private Date date;
	private boolean okClicked = false;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
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

	@FXML
	private void handleOK() {
		date.setBigEvent(eventField.getText());
		date.setHolidayFlag(holidayCB.isSelected());
		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	@FXML
	private void handleCheckBox(){
		if(holidayCB.isSelected()){
			holidayCB.setSelected(false);
		}else{
			holidayCB.setSelected(true);
		}
	}
}
