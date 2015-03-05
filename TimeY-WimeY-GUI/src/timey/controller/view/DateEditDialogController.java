package timey.controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timey.controller.MainApp;
import timey.controller.model.Date;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> DateEditDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 13:00:55
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Date Edit Dialog.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class DateEditDialogController {
	@FXML
	/**
	 * Textfield for the event
	 */
	private TextField eventField;
	@FXML
	/**
	 * Checkbox if this date is a holiday
	 */
	private CheckBox holidayCB;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * The Date to edit
	 */
	private Date date;
	/**
	 * Boolean for ok button
	 */
	private boolean okClicked = false;
	/**
	 * Boolean if there were updates in the database
	 */
	private boolean updated = false;
	/**
	 * MainApp of TimeY-WimeY
	 */
	private MainApp mainApp;

	@FXML
	/**
	 * Initializes the dialog
	 */
	private void initialize() {

	}

	/**
	 * Sets the Stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the MainApp to this object
	 * 
	 * @param main
	 *            is the MainApp of TimeY-WimeY
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Sets the Date to this dialog
	 * 
	 * @param date
	 *            is the date to be edited
	 */
	public void setDate(Date date) {
		this.date = date;
		String event = "";
		if (date.getEvent() != null)
			event = date.getEvent();
		else
			event = "";
		eventField.setText(event);
		holidayCB.setSelected(date.getHolidayFlag());
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
	 * Getter of updated
	 * 
	 * @return updated
	 */
	public boolean isSomethingChanged() {
		return updated;
	}

	@FXML
	/**
	 * Handles the action if ok is pressed
	 * @return true if there were updates in the database, false if not
	 */
	private boolean handleOK() {
		date.setEvent(eventField.getText());
		date.setHolidayFlag(holidayCB.isSelected());
		String holidayFlag = "f";
		if (holidayCB.isSelected()) {
			holidayFlag = "t";
		} else {
			holidayFlag = "f";
		}
		updated = mainApp.getDBConn()
				.handleEditDate(holidayFlag, eventField.getText(),
						mainApp.getUserID(), date.getCalendarID());
		okClicked = true;
		dialogStage.close();
		return updated;
	}

	@FXML
	/**
	 * Closes Stage on cancel
	 */
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	/**
	 * Handle if CheckBox is clicked
	 */
	private void handleCheckBox() {
		if (holidayCB.isSelected()) {
			holidayCB.setSelected(false);
		} else {
			holidayCB.setSelected(true);
		}
	}
}
