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

@SuppressWarnings("deprecation")
public class DateViewController {
	@FXML
	private TableView<Date> dateTable;
	@FXML
	private TableColumn<Date, String> dateColumn;
	@FXML
	private TableColumn<Date, String> dayNameColumn;
	@FXML
	private TableColumn<Date, Boolean> holidayColumn;
	@FXML
	private TableColumn<Date, String> eventColumn;

	@FXML
	private TableView<Time> timeTable;
	@FXML
	private TableColumn<Time, String> dateTimeColumn;
	@FXML
	private TableColumn<Time, LocalTime> startTimeColumn;
	@FXML
	private TableColumn<Time, LocalTime> endTimeColumn;
	@FXML
	private TableColumn<Time, Number> totalTimeColumn;
	@FXML
	private TableColumn<Time, String> categoryColumn;
	@FXML
	private TableColumn<Time, String> noteColumn;

	@FXML
	private Label dateLabel;
	@FXML
	private Label dayLabel;
	@FXML
	private Label eventLabel;
	@FXML
	private Label holidayLabel;
	@FXML
	private PieChart pie;
	@FXML
	private Label caption;
	@FXML
	private DatePicker toPicker;
	@FXML
	private DatePicker fromPicker;
	private MainApp mainApp;

	public DateViewController() {

	}

	@FXML
	private void initialize() {

		dateColumn.setCellValueFactory(cellData -> cellData.getValue()
				.completeDateProperty());
		dayNameColumn.setCellValueFactory(cellData -> cellData.getValue()
				.dayNameProperty());
		holidayColumn.setCellValueFactory(cellData -> cellData.getValue()
				.holidayFlagProperty());
		eventColumn.setCellValueFactory(cellData -> cellData.getValue()
				.bigEventProperty());

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

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	private void showDateDetails(Date date) {
		if (date != null) {
			dateLabel.setText(date.getCompleteDate());
			dayLabel.setText(date.getDayName());
			eventLabel.setText(date.getBigEvent());
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
	private void handleNewTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null) {
			boolean changed = mainApp.showTimeEditDialog(selectedDate, null);
			if (changed) {
				handleRefresh();
//				showDateDetails(selectedDate);
			}

		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Date Selected")
					.message("Please select a date in the table.")
					.showWarning();
		}
	}

	@FXML
	private void handleEditTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		Time selectedTime = timeTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null & selectedTime != null) {
			boolean changed = mainApp.showTimeEditDialog(selectedDate,
					selectedTime);
			if (changed) {
				handleRefresh();
//				showDateDetails(selectedDate);
			}

		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Date Selected")
					.message("Please select a date and time in the table.")
					.showWarning();
		}
	}

	@FXML
	private void handleRefresh(){
		mainApp.refreshDateView(toPicker.getValue(), fromPicker.getValue(),	dateTable.getSelectionModel().getFocusedIndex());

	}
	public void setDatePicker(LocalDate toPickerValue, LocalDate fromPickerValue){
		if(fromPickerValue != null & toPickerValue != null) {
			fromPicker.setValue(fromPickerValue);
			toPicker.setValue(toPickerValue);
		}else if(fromPickerValue == null & toPickerValue != null){
			fromPicker.setValue(toPickerValue);
			toPicker.setValue(toPickerValue);
		}else if(fromPickerValue != null & toPickerValue == null){
			fromPicker.setValue(fromPickerValue);
			toPicker.setValue(fromPickerValue);
		}
	}
	public void setSelectionIndexDateTable(int selectIndex){
		dateTable.getSelectionModel().selectIndices(selectIndex);
	}
	
	@FXML
	private void handleDatePicker() {
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
		this.dateTable.setItems(mainApp.getDates(idFrom, idTo, mainApp.getUserID()));
	}

	@FXML
	private void handleNewCategory() {
		if(mainApp.showCategoryEditDialog(true) == true){
			handleRefresh();
		};
	}

	@FXML
	private void handleEditCategory() {
		mainApp.showCategoryEditDialog(false);
	}

	@FXML
	private void handleRemoveCategory() {
		mainApp.showCategoryRemoveDialog();
		handleRefresh();
	}
	@FXML
	private void handlePlanHolidays(){
		boolean changed = mainApp.showPlanHolidayDialog(fromPicker.getValue(), toPicker.getValue());
		if(changed == true){
			handleRefresh();
		}
	}
	@FXML
	private void handleAnalyzeCategory(){
		mainApp.showAnalyzeCategoryDialog(fromPicker.getValue(), toPicker.getValue());
	}

	@FXML
	private void handleRemoveTime() {
		Date selectedDate = dateTable.getSelectionModel().getSelectedItem();
		Time selectedTime = timeTable.getSelectionModel().getSelectedItem();
		if (selectedDate != null & selectedTime != null) {
			for (int i = 0; i < selectedDate.getTimes().size(); i++) {
				if (selectedDate.getTimes().get(i) == selectedTime) {
					selectedDate.getTimes().remove(i);
				}
			}
			mainApp.getDBConn().handleRemoveTime(mainApp.getUserID(), selectedTime);
			handleRefresh();
			

		} else {
			// Nothing selected.
			Dialogs.create().title("No Selection").masthead("No Data Selected")
					.message("Please select a date and a time in the tables.")
					.showWarning();
		}
	}

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
