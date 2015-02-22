package timey.controller.view;

import java.time.LocalDate;

import timey.controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlanHolidayDialogController {
	@FXML
	private DatePicker fromPicker;
	@FXML
	private DatePicker toPicker;
	@FXML
	private CheckBox isHoliday;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean changed = false;
	private MainApp mainApp;

	@FXML
	private void initialize() {

	}

	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	public void setData(LocalDate from, LocalDate to) {
		if(from != null & to != null) {
			fromPicker.setValue(from);
			toPicker.setValue(to);
		}else if(from == null & to != null){
			fromPicker.setValue(to);
			toPicker.setValue(to);
		}else if(from != null & to == null){
			fromPicker.setValue(from);
			toPicker.setValue(from);
		}
		
	
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public boolean isSomethingChanged() {
		return changed;
	}

	@FXML
	private void handleOK() {
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
			changed = mainApp.getDBConn().handlePlanHoliday(idFrom, idTo,
					mainApp.getUserID(), isHoliday.isSelected());
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
