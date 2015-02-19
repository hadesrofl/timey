package timey.controller.view;

import java.time.LocalTime;

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
	private ComboBox<String> categoryCombo;
	@FXML
	private TextField noteField;
	private Stage dialogStage;
	private Date date;
	private Time time;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		setFocusListenerToTextField(endTimeField);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setDate(Date date, Time time) {
		this.date = date;
		this.time = time;
		dateLabel.setText(date.getCompleteDate());
		totalTimeLabel.setText("");
		ObservableList<String> categoriesData = FXCollections.observableArrayList();
		categoryCombo.setItems(categoriesData);

		// Edit Time
		if (time != null) {
			startTimeField.setText(time.getStartTime() + "");
			endTimeField.setText(time.getEndTime() + "");
			totalTimeLabel.setText(time.getTotalTime() + "");
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
					categoryCombo.getSelectionModel().getSelectedItem(), noteField.getText(),
					date.getCalendarID());
			date.getTimes().add(newTime);
		}else{
			time.setStartTime(LocalTime.of(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1])));
			time.setEndTime(LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1])));
			time.setTotalTime(Double.parseDouble(totalTimeLabel.getText()));
			time.setCategory(categoryCombo.getSelectionModel().getSelectedItem());
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
	private void totalTimeCalculation() {
		if (startTimeField.getCharacters().length() == 5
				& endTimeField.getCharacters().length() == 5) {
			String[] startTime = startTimeField.getText().split(":");
			String[] endTime = endTimeField.getText().split(":");
			double startHour = Double.parseDouble(startTime[0]);
			double startMinutes = Double.parseDouble(startTime[1]);
			double endHour = Double.parseDouble(endTime[0]);
			double endMinutes = Double.parseDouble(endTime[1]);
			double diffHours = 0;
			if(endHour < startHour){
				diffHours = (24 - startHour) + endHour;
			}else{
				diffHours = endHour - startHour;
			}
			double diffMinutes = endMinutes - startMinutes;
			double totalTime = Math.round(100*((diffHours * 60) + diffMinutes) / 60);
			totalTime = totalTime / 100;
			totalTimeLabel.setText(String.valueOf(totalTime));
		} else {
			endTimeField.setText("");
			Dialogs.create().title("Enter Time")
					.masthead("Enter Start-Time and End-Time")
					.message("Please enter the Start- and End-Time (HH:MM).")
					.showWarning();
		}
	}
	
	private void setFocusListenerToTextField(TextField textField){
		textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        if (newPropertyValue)
		        {
		            System.out.println("Textfield on focus");

		        }
		        else
		        {
		            System.out.println("Textfield out focus");
		            totalTimeCalculation();
		        }
		    }
		});
	}
}
