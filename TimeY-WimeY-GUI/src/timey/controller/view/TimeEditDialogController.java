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
	private boolean changed = false;
	private MainApp mainApp;
	private ObservableList<String> categoriesData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {
		setFocusListenerToTextField(endTimeField);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

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
			for(int i = 0; i < categoriesData.size(); i++){
				if(time.getCategory().compareTo(categoriesData.get(i)) == 0){
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

	public boolean isOkClicked() {
		return okClicked;
	}
	
	public boolean somethingIsChanged(){
		return changed;
	}

	@FXML
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
			endTimeField.setText("");
			Dialogs.create().title("Enter Time")
					.masthead("Enter Start-Time and End-Time")
					.message("Please enter the Start- and End-Time (HH:MM).")
					.showWarning();
		}
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
					totalTimeCalculation();
				}
			}
		});
	}
}
