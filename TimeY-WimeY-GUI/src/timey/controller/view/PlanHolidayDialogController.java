package timey.controller.view;

import java.time.LocalDate;

import timey.controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> PlanHolidayDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 13:06:02
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Plan Holiday Dialog
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class PlanHolidayDialogController {
	@FXML
	/**
	 * Date Picker for the start date
	 */
	private DatePicker fromPicker;
	@FXML
	/**
	 * Date Picker for the end date
	 */
	private DatePicker toPicker;
	@FXML
	/**
	 * CheckBox if dates are holidays for the user
	 */
	private CheckBox isHoliday;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean for ok button
	 */
	private boolean okClicked = false;
	/**
	 * Boolean for changes in the database
	 */
	private boolean changed = false;
	private MainApp mainApp;

	@FXML
	/**
	 * Initializes this dialog
	 */
	private void initialize() {

	}

	/**
	 * Sets MainApp to this dialog
	 * 
	 * @param main
	 *            is the MainApp of TimeY-WimeY
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Sets the Values of the Date Picker
	 * 
	 * @param from
	 *            is the start date
	 * @param to
	 *            is the end date
	 */
	public void setData(LocalDate from, LocalDate to) {
		if (from != null & to != null) {
			fromPicker.setValue(from);
			toPicker.setValue(to);
		} else if (from == null & to != null) {
			fromPicker.setValue(to);
			toPicker.setValue(to);
		} else if (from != null & to == null) {
			fromPicker.setValue(from);
			toPicker.setValue(from);
		}

	}

	/**
	 * Sets the stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage of this dialog
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

	/**
	 * Getter for changed
	 * 
	 * @return changed
	 */
	public boolean isSomethingChanged() {
		return changed;
	}

	@FXML
	/**
	 * Edit multiple Dates and set holiday flag for these dates
	 */
	private void handleOK() {
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
		changed = mainApp.getDBConn().handlePlanHoliday(idFrom, idTo,
				mainApp.getUserID(), isHoliday.isSelected());
		dialogStage.close();
	}

	@FXML
	/**
	 * Closes the stage if cancel is pressed
	 */
	private void handleCancel() {
		dialogStage.close();
	}
}
