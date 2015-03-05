package timey.controller.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.MainApp;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> DateViewController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 14:10:49
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Date View Scene, which is the main scene for the user
 * and contains all buttons for getting to other dialogs and create, edit or
 * delete data in the database or analyze categories
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
@SuppressWarnings("deprecation")
public class DateViewController {
	@FXML
	/**
	 * Table for the dates
	 */
	private TableView<Date> dateTable;
	@FXML
	/**
	 * Column in the Date Table for the date
	 */
	private TableColumn<Date, String> dateColumn;
	@FXML
	/**
	 * Column in the Date Table for the day name
	 */
	private TableColumn<Date, String> dayNameColumn;
	@FXML
	/**
	 * Column in the Date Table for the holiday flag
	 */
	private TableColumn<Date, Boolean> holidayColumn;
	@FXML
	/**
	 * Column in the Date Table for the event
	 */
	private TableColumn<Date, String> eventColumn;

	@FXML
	/**
	 * Table for the times
	 */
	private TableView<Time> timeTable;
	@FXML
	/**
	 * Column in the Time Table for the date of the time
	 */
	private TableColumn<Time, String> dateTimeColumn;
	@FXML
	/**
	 * Column in the Time Table for the start time
	 */
	private TableColumn<Time, LocalTime> startTimeColumn;
	@FXML
	/**
	 * Column in the Time Table for the end time
	 */
	private TableColumn<Time, LocalTime> endTimeColumn;
	@FXML
	/**
	 * Column in the Time Table for the total time
	 */
	private TableColumn<Time, Number> totalTimeColumn;
	@FXML
	/**
	 * Column in the Time Table for the category
	 */
	private TableColumn<Time, String> categoryColumn;
	@FXML
	/**
	 * Column in the Time Table for the note
	 */
	private TableColumn<Time, String> noteColumn;

	@FXML
	/**
	 * Label for the date
	 */
	private Label dateLabel;
	@FXML
	/**
	 * Label for the day name
	 */
	private Label dayLabel;
	@FXML
	/**
	 * Label for the event
	 */
	private Label eventLabel;
	@FXML
	/**
	 * Label for the holiday flag
	 */
	private Label holidayLabel;
	@FXML
	/**
	 * PieChart for showing the hours of each category per day
	 */
	private PieChart pie;
	@FXML
	/**
	 * Label for the caption
	 */
	private Label caption;
	@FXML
	/**
	 * Date Picker for the end date
	 */
	private DatePicker toPicker;
	@FXML
	/**
	 * Date Picker for the start date
	 */
	private DatePicker fromPicker;
	/**
	 * MainApp Class of TimeY-WimeY
	 */
	private MainApp mainApp;

	/**
	 * Constructor
	 */
	public DateViewController() {

	}

	@FXML
	/**
	 * Initializes this Scene and fills the Tables and Pie Chart with data
	 */
	private void initialize() {

		dateColumn.setCellValueFactory(cellData -> cellData.getValue()
				.completeDateProperty());
		dayNameColumn.setCellValueFactory(cellData -> cellData.getValue()
				.dayNameProperty());
		holidayColumn.setCellValueFactory(cellData -> cellData.getValue()
				.holidayFlagProperty());
		eventColumn.setCellValueFactory(cellData -> cellData.getValue()
				.eventProperty());

		dateTimeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.dateProperty());
		startTimeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.startTimeProperty());
		endTimeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.endTimeProperty());
		totalTimeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.totalTimeProperty());
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue()
				.categoryProperty());
		noteColumn.setCellValueFactory(cellData -> cellData.getValue()
				.noteProperty());

		// Clear Date Details
		showDateDetails(null);

		// Listen for selection changes and show the details when changed
		dateTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showDateDetails(newValue));
		dateTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> this.timeTable
								.setItems(newValue.getTimes()));
		dateTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> this.pie
								.setData(getChartData(newValue.getTimes())));
	}

	/**
	 * Set the Main App to this scene
	 * 
	 * @param mainApp
	 *            is the MainApp of TimeY-WimeY
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Shows the details of a specific date
	 * 
	 * @param date
	 *            is the date to be shown
	 */
	private void showDateDetails(Date date) {
		if (date != null) {
			dateLabel.setText(date.getCompleteDate());
			dayLabel.setText(date.getDayName());
			eventLabel.setText(date.getEvent());
			String holiday = "";
			if (date.getHolidayFlag()) {
				holiday = "T";
			} else {
				holiday = "F";
			}
			holidayLabel.setText(holiday);
		} else {
			dateLabel.setText("");
			dayLabel.setText("");
			eventLabel.setText("");
			holidayLabel.setText("");
		}
	}

	@FXML
	/**
	 * Shows the Edit Date Dialog if button is pressed. Creates a dialog if there is an error
	 */
	private void handleEditDate() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null) {
			boolean changed = mainApp.showDateEditDialog(selectedDate);
			if (changed) {
				handleRefresh();
				showDateDetails(selectedDate);
			}
		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection")
					.masthead("No Person Selected")
					.message("Please select a date in the table.")
					.showWarning();
		}
	}

	@FXML
	/**
	 * Shows the New Time Dialog if button is pressed. Creates a dialog if there is an error
	 */
	private void handleNewTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null) {
			boolean changed = mainApp.showTimeEditDialog(selectedDate, null);
			if (changed) {
				handleRefresh();
			}
		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Date Selected")
					.message("Please select a date in the table.")
					.showWarning();
		}
	}

	@FXML
	/**
	 * Shows the Edit Time Dialog if button is pressed. Creates a dialog if there is an error
	 */
	private void handleEditTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		Time selectedTime = timeTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null & selectedTime != null) {
			boolean changed = mainApp.showTimeEditDialog(selectedDate,
					selectedTime);
			if (changed) {
				handleRefresh();
			}
		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Date Selected")
					.message("Please select a date and time in the table.")
					.showWarning();
		}
	}

	@FXML
	/**
	 * Refreshes this Scene
	 */
	private void handleRefresh() {
		mainApp.refreshDateView(toPicker.getValue(), fromPicker.getValue(),
				dateTable.getSelectionModel().getFocusedIndex());
	}

	/**
	 * Sets the Values of the Date Picker
	 * 
	 * @param toPickerValue
	 *            is the value of the end date for the Date Picker
	 * @param fromPickerValue
	 *            is the value of the start date for the Date Picker
	 */
	public void setDatePicker(LocalDate toPickerValue, LocalDate fromPickerValue) {
		if (fromPickerValue != null & toPickerValue != null) {
			fromPicker.setValue(fromPickerValue);
			toPicker.setValue(toPickerValue);
		} else if (fromPickerValue == null & toPickerValue != null) {
			fromPicker.setValue(toPickerValue);
			toPicker.setValue(toPickerValue);
		} else if (fromPickerValue != null & toPickerValue == null) {
			fromPicker.setValue(fromPickerValue);
			toPicker.setValue(fromPickerValue);
		}
	}

	/**
	 * Selects the specific index in the Date Table
	 * 
	 * @param selectIndex
	 *            is the index to select
	 */
	public void setSelectionIndexDateTable(int selectIndex) {
		dateTable.getSelectionModel().selectIndices(selectIndex);
	}

	@FXML
	/**
	 * Set Date Table to selected values of the date pickers
	 */
	private void handleDatePicker() {
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
		this.dateTable.setItems(mainApp.getDates(idFrom, idTo,
				mainApp.getUserID()));
	}

	@FXML
	/**
	 * Shows the Category Edit Dialog without a ComboBox to enter a new category and then refresh the DateView Scene
	 */
	private void handleNewCategory() {
		if (mainApp.showCategoryEditDialog(true) == true) {
			handleRefresh();
		}
	}

	@FXML
	/**
	 * Show the Category Edit Dialog to edit a selected category
	 */
	private void handleEditCategory() {
		mainApp.showCategoryEditDialog(false);
	}

	@FXML
	/**
	 * Show the Remove Category Dialog and refresh DateView Scene after that
	 */
	private void handleRemoveCategory() {
		mainApp.showCategoryRemoveDialog();
		handleRefresh();
	}

	@FXML
	/**
	 * Shows the Plan Holidays Dialog to set or unset Holiday Flag to multiple Dates
	 */
	private void handlePlanHolidays() {
		boolean changed = mainApp.showPlanHolidayDialog(fromPicker.getValue(),
				toPicker.getValue());
		if (changed == true) {
			handleRefresh();
		}
	}

	@FXML
	/**
	 * Show the Analyze Category Dialog 
	 */
	private void handleAnalyzeCategory() {
		mainApp.showAnalyzeCategoryDialog(fromPicker.getValue(),
				toPicker.getValue());
	}

	@FXML
	/**
	 * Removes a selected time from the table and database. Creates a dialog if there is an error
	 */
	private void handleRemoveTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		Time selectedTime = timeTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null & selectedTime != null) {
			for (int i = 0; i < selectedDate.getTimes().size(); i++) {
				if (selectedDate.getTimes().get(i) == selectedTime) {
					selectedDate.getTimes().remove(i);
				}
			}
			mainApp.getDBConn().handleRemoveTime(mainApp.getUserID(),
					selectedTime);
			handleRefresh();

		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Data Selected")
					.message("Please select a date and a time in the tables.")
					.showWarning();
		}
	}

	/**
	 * Get Data for the pie chart
	 * 
	 * @param times
	 *            is a list of times that shall be displayed in the pie chart
	 * @return a list of data objects for the pie chart
	 */
	private ObservableList<Data> getChartData(ObservableList<Time> times) {
		ObservableList<Data> data = FXCollections.observableArrayList();
		try {
			Map<String, Double> category = new HashMap<String, Double>();
			Double newValue = 0.0;

			// Add all totalTimes per category together
			for (Time t : times) {
				if (category.containsKey(t.getCategory()) == true) {
					newValue = category.get(t.getCategory()) + t.getTotalTime();
				} else {
					newValue = t.getTotalTime();
				}
				category.put(t.getCategory(), newValue);
			}

			// put in data for pieChart
			for (Entry<String, Double> entry : category.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				data.add(new Data(key, value));
			}
		} catch (NullPointerException e) {
			e.getStackTrace();
		}
		return data;
	}
}
