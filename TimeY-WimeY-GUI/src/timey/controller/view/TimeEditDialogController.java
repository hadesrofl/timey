package timey.controller.view;

import timey.controller.MainApp;

import org.controlsfx.dialog.Dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> TimeEditDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 13:51:02
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Time Edit Dialog.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
@SuppressWarnings("deprecation")
public class TimeEditDialogController {
	@FXML
	/**
	 * Label for the date
	 */
	private Label dateLabel;
	@FXML
	/**
	 * TextField for the start time
	 */
	private TextField startTimeField;
	@FXML
	/**
	 * TextField for the end time
	 */
	private TextField endTimeField;
	@FXML
	/**
	 * Label for the total time
	 */
	private Label totalTimeLabel;
	@FXML
	/**
	 * ComboBox for the categories
	 */
	private ComboBox<String> categoryCombo;
	@FXML
	/**
	 * TextField for the note of the time
	 */
	private TextField noteField;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * Date of the time object
	 */
	private Date date;
	/**
	 * time object to edit
	 */
	private Time time;
	/**
	 * Boolean for the ok button
	 */
	private boolean okClicked = false;
	/**
	 * Boolean for changes in the database
	 */
	private boolean changed = false;
	/**
	 * MainApp of TimeY-WimeY
	 */
	private MainApp mainApp;
	/**
	 * List of categories of the user
	 */
	private ObservableList<String> categoriesData = FXCollections
			.observableArrayList();

	@FXML
	/**
	 * Initializes the dialog
	 */
	private void initialize() {
		setFocusListenerToTextField(endTimeField);
	}

	/**
	 * Sets the stage for this dialog
	 * 
	 * @param dialogStage
	 *            the stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the MainApp to this dialog
	 * 
	 * @param main
	 *            is the MainApp of TimeY-WimeY
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Sets the Date and Time to this dialog
	 * 
	 * @param date
	 *            of the time object
	 * @param time
	 *            is the time that shall be edited
	 */
	public void setDate(Date date, Time time) {
		this.date = date;
		this.time = time;
		dateLabel.setText(date.getCompleteDate());
		totalTimeLabel.setText("");
		categoriesData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoriesData);

		// Edit Time
		if (time != null) {
			startTimeField.setText(time.getStartTime() + "");
			endTimeField.setText(time.getEndTime() + "");
			totalTimeLabel.setText(time.getTotalTime() + "");
			for (int i = 0; i < categoriesData.size(); i++) {
				if (time.getCategory().compareTo(categoriesData.get(i)) == 0) {
					categoryCombo.getSelectionModel().select(i);
				}
			}
			if (time.getNote() != null) {
				noteField.setText(time.getNote());
			} else {
				noteField.setText("");
			}
		}

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
	 * Getter of changed
	 * 
	 * @return changed
	 */
	public boolean isSomethingChanged() {
		return changed;
	}

	@FXML
	/**
	 * Executes a update Query to edit the time in the database
	 */
	private void handleOK() {
		String[] startTime = startTimeField.getText().split(":");
		String[] endTime = endTimeField.getText().split(":");
		String startTimeQuery = startTime[0] + ":" + startTime[1];
		String endTimeQuery = endTime[0] + ":" + endTime[1];

		int categoryID = mainApp.getDBConn().getCategoryID(mainApp.getUserID(),
				categoryCombo.getSelectionModel().getSelectedItem());

		changed = mainApp.getDBConn().handleEditTime(time, mainApp.getUserID(),
				startTimeQuery, endTimeQuery, totalTimeLabel.getText(),
				noteField.getText(), date.getCalendarID(), categoryID);
		okClicked = true;
		dialogStage.close();
	}

	@FXML
	/**
	 * Closes the stage on cancel
	 */
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	/**
	 * Calculates the total time and shows it via the totalTimeLabel
	 */
	private void totalTimeCalculation() {
		if (startTimeField.getCharacters().length() == 5
				& endTimeField.getCharacters().length() == 5) {
			String[] startTime = startTimeField.getText().split(":");
			String[] endTime = endTimeField.getText().split(":");
			double startHour = Double.parseDouble(startTime[0]);
			double startMinutes = Double.parseDouble(startTime[1]);
			double endHour = Double.parseDouble(endTime[0]);
			double endMinutes = Double.parseDouble(endTime[1]);
			if (startHour > 24 | startHour < 0 | endHour > 24 | endHour < 0
					| startMinutes > 59 | startMinutes < 0 | endMinutes > 59
					| endMinutes < 0) {
				endTimeField.setText("");
				Dialogs.create()
						.title("Enter Time")
						.masthead("Not a correct Start or End Time")
						.message(
								"You know how to enter a time, don't you? (HH:MM)")
						.showWarning();
			}
			double diffHours = 0;
			if (endHour < startHour) {
				diffHours = (24 - startHour) + endHour;
			} else {
				diffHours = endHour - startHour;
			}
			double diffMinutes = endMinutes - startMinutes;
			double totalTime = Math
					.round(100 * ((diffHours * 60) + diffMinutes) / 60);
			totalTime = totalTime / 100;
			totalTimeLabel.setText(String.valueOf(totalTime));
		} else {
			startTimeField.setText("");
			endTimeField.setText("");
			Dialogs.create().title("Enter Time")
					.masthead("Enter Start-Time and End-Time")
					.message("Please enter the Start- and End-Time (HH:MM).")
					.showWarning();
		}
	}

	/**
	 * Sets a listener for getting or losing focus of a textfield
	 * 
	 * @param textField
	 *            the textfield which shall get a FocusListener
	 */
	private void setFocusListenerToTextField(TextField textField) {
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("Textfield on focus");

				} else {
					System.out.println("Textfield out focus");
					totalTimeCalculation();
				}
			}
		});
	}
}
