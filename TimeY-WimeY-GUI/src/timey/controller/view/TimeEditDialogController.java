package timey.controller.view;

import java.time.LocalTime;

import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timey.controller.model.Date;
import timey.controller.model.Time;

@SuppressWarnings("deprecation")
public class TimeEditDialogController {
	@FXML
	private Label dateLabel;
	@FXML
	private TextField startTimeField;
	@FXML
	private TextField endTimeField;
	@FXML
	private Label totalTimeLabel;
	@FXML
	private TextField categoryField;
	@FXML
	private TextField noteField;
	private Stage dialogStage;
	private Date date;
	private Time time;
	private boolean okClicked = false;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setDate(Date date, Time time) {
		this.date = date;
		this.time = time;
		dateLabel.setText(date.getCompleteDate());
		totalTimeLabel.setText("");

		// Edit Time
		if (time != null) {
			startTimeField.setText(time.getStartTime() + "");
			endTimeField.setText(time.getEndTime() + "");
			totalTimeLabel.setText(time.getTotalTime() + "");
			categoryField.setText(time.getCategory());
			if (time.getNote() != null) {
				noteField.setText(time.getNote());
			} else {
				noteField.setText("");
			}
		}

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOK() {
		String[] startTime = startTimeField.getText().split(":");
		String[] endTime = endTimeField.getText().split(":");
		if (time == null) {
			Time newTime = new Time(date.getCompleteDate(), LocalTime.of(
					Integer.parseInt(startTime[0]),
					Integer.parseInt(startTime[1])),
					LocalTime.of(Integer.parseInt(endTime[0]),
							Integer.parseInt(endTime[1])),
					Double.parseDouble(totalTimeLabel.getText()),
					categoryField.getText(), noteField.getText(),
					date.getCalendarID());
			date.getTimes().add(newTime);
		}else{
			time.setStartTime(LocalTime.of(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1])));
			time.setEndTime(LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1])));
			time.setTotalTime(Double.parseDouble(totalTimeLabel.getText()));
			time.setCategory(categoryField.getText());
			time.setNote(noteField.getText());
		}
		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleCategory() {
		if (startTimeField.getCharacters().length() == 5
				& endTimeField.getCharacters().length() == 5) {
			String[] startTime = startTimeField.getText().split(":");
			String[] endTime = endTimeField.getText().split(":");
			double totalTime = Math
					.round(100 * ((Double.parseDouble(endTime[0]) - Double
							.parseDouble(startTime[0])) * 60 + (Double
							.parseDouble(endTime[1]) - Double
							.parseDouble(startTime[1]))) / 60);
			totalTime = totalTime / 100;
			totalTimeLabel.setText(String.valueOf(totalTime));
		} else {
			categoryField.setText("");
			Dialogs.create().title("Enter Time")
					.masthead("Enter Start-Time and End-Time")
					.message("Please enter the Start- and End-Time (HH:MM).")
					.showWarning();
		}
	}
}
