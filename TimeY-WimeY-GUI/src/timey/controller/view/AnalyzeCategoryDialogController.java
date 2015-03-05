package timey.controller.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.controlsfx.dialog.Dialogs;

import timey.controller.MainApp;
import timey.controller.model.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> AnalyzeCategoryDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 12:21:38
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Analyze Category Dialog and reacts to the javaFX
 * elements in the scene
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
@SuppressWarnings("deprecation")
public class AnalyzeCategoryDialogController {
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
	 * ComboBox for choosing a category
	 */
	private ComboBox<String> categoryCombo;
	@FXML
	/**
	 * Label for the total hours
	 */
	private Label totalHours;
	@FXML
	/**
	 * Label for the calculation of hours per day spent on a certain category
	 */
	private Label hoursPerDay;
	@FXML
	/**
	 * Label for the calculation of hours per week spent on a certain category
	 */
	private Label hoursPerWeek;
	@FXML
	/**
	 * Label for the calculation of hours per month spent on a certain category
	 */
	private Label hoursPerMonth;
	@FXML
	/**
	 * Label for the calculation of hours in percent spent on a certain category
	 */
	private Label hoursSpentPercent;
	@FXML
	/**
	 * Label for the day
	 */
	private Label dayLabel;
	@FXML
	/**
	 * PieChart for showing the hours of a category
	 */
	private PieChart pie;
	/**
	 * Stage of the dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean if ok button is clicked
	 */
	private boolean okClicked = false;
	/**
	 * MainApp of TimeY-WimeY
	 */
	private MainApp mainApp;
	/**
	 * List of categories
	 */
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	/**
	 * Initializes the Dialog
	 */
	private void initialize() {
		dayLabel.setText("");
		totalHours.setText("");
		hoursPerDay.setText("");
		hoursPerWeek.setText("");
		hoursPerMonth.setText("");
		hoursSpentPercent.setText("");
	}

	/**
	 * Assign the mainApp to this object
	 * 
	 * @param main
	 *            is the mainApp
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Set the Data for this Dialog
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
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoryData);

	}

	/**
	 * Sets the stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage for this dialog
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

	@FXML
	/**
	 * Handle the action for the Analyze Button and calculation all data for the results in this dialog
	 */
	private void handleAnalyze() {
		// both date picker have no values
		if (fromPicker.getValue() == null & toPicker.getValue() == null) {
			Dialogs.create().title("Select a Date!")
					.masthead("You have not selected a date")
					.message("Please select a date to continue!").showWarning();
			return;

		}
		// start date is not set
		else if (fromPicker.getValue() == null & toPicker.getValue() != null) {
			fromPicker.setValue(toPicker.getValue());
		}
		// end date is not set
		else if (fromPicker.getValue() != null & toPicker.getValue() == null) {
			toPicker.setValue(fromPicker.getValue());
		}
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
		String selected = categoryCombo.getSelectionModel().getSelectedItem();
		// no category is set
		if (selected == null) {
			Dialogs.create().title("Select a Category!")
					.masthead("You have not selected a category")
					.message("Please select a category to continue!")
					.showWarning();
			return;
		}
		double totalTimeCategory = 0;
		double totalTimeAll = 0;
		ObservableList<Time> times = mainApp.getDBConn().handleAnalyzeCategory(
				idFrom, idTo, mainApp.getUserID());
		new LinkedList<String>();
		pie.setData(getChartData(times));
		// get total time of selected category
		for (int i = 0; i < times.size(); i++) {
			if (selected.compareTo(times.get(i).getCategory()) == 0) {
				totalTimeCategory = totalTimeCategory
						+ times.get(i).getTotalTime();
			}
			totalTimeAll = totalTimeAll + times.get(i).getTotalTime();
		}
		totalHours.setText(totalTimeCategory + "");
		int days = mainApp.getDBConn().countDays(idFrom, idTo);
		dayLabel.setText(days + "");
		double hoursPDay = Math.round(100 * (totalTimeCategory / days));
		hoursPDay = hoursPDay / 100;
		hoursPerDay.setText(hoursPDay + "");
		double weeks = days / 7;
		// if no full week, set to 1
		if (weeks < 1)
			weeks = 1;
		double hoursPWeek = totalTimeCategory / weeks;
		hoursPerWeek.setText(hoursPWeek + "");
		double months = weeks / 4;
		// if no full month, set to 1
		if (months < 1) {
			hoursPerMonth.setText("");
		} else {
			double hoursPMonth = Math.round(100 * (totalTimeCategory / months));
			hoursPMonth = hoursPMonth / 100;
			hoursPerMonth.setText(hoursPMonth + "");
		}
		double hoursPercent = Math.round(100 * (totalTimeCategory * 100)
				/ totalTimeAll);
		hoursPercent = hoursPercent / 100;
		hoursSpentPercent.setText(hoursPercent + " %");
	}

	@FXML
	/**
	 * Close stage on exit button
	 */
	private void handleExit() {
		dialogStage.close();
	}

	/**
	 * Create PieChart Data from a list of times
	 * 
	 * @param times
	 *            list with time objects to show in the pie chart
	 * @return a list of data for the pie chart
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
