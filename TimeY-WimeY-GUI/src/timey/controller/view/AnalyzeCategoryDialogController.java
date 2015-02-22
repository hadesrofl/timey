package timey.controller.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

@SuppressWarnings("deprecation")
public class AnalyzeCategoryDialogController {
	@FXML
	private DatePicker fromPicker;
	@FXML
	private DatePicker toPicker;
	@FXML
	private ComboBox<String> categoryCombo;
	@FXML
	private Label totalHours;
	@FXML
	private Label hoursPerDay;
	@FXML
	private Label hoursPerWeek;
	@FXML
	private Label hoursPerMonth;
	@FXML
	private Label hoursSpentPercent;
	@FXML
	private Label dayLabel;
	@FXML
	private PieChart pie;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean changed = false;
	private MainApp mainApp;
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {
		dayLabel.setText("");
		totalHours.setText("");
		hoursPerDay.setText("");
		hoursPerWeek.setText("");
		hoursPerMonth.setText("");
		hoursSpentPercent.setText("");
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
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoryData);
	
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
	private void handleAnalyze() {
		if(fromPicker.getValue() == null & toPicker.getValue() == null) {
			Dialogs.create().title("Select a Date!").masthead("You have not selected a date").message("Please select a date to continue!").showWarning();
			return;
		}else if(fromPicker.getValue() == null & toPicker.getValue() != null){
			fromPicker.setValue(toPicker.getValue());
		}else if(fromPicker.getValue() != null & toPicker.getValue() == null){
			toPicker.setValue(fromPicker.getValue());
		}
		String idFrom = mainApp.createCalendarIDFromDatePicker(fromPicker);
		String idTo = mainApp.createCalendarIDFromDatePicker(toPicker);
		String selected = categoryCombo.getSelectionModel().getSelectedItem();
		if(selected == null) {
			Dialogs.create().title("Select a Category!").masthead("You have not selected a category").message("Please select a category to continue!").showWarning();
			return;
		}
		boolean visited;
		double totalTimeCategory = 0;
		double totalTimeAll = 0;
			ObservableList<Time> times = mainApp.getDBConn().handleAnalyzeCategory(idFrom, idTo, mainApp.getUserID());
			List<String> categories = new LinkedList<String>();
			pie.setData(getChartData(times));
			for(int i = 0; i < times.size(); i++){
//				visited = false;
//				for(int j = 0; j < categories.size(); j++){
//					if(categories.get(j).compareTo(times.get(i).getCategory()) == 0){
//						visited = true;
//						break;
//					}
//				}
//				if(visited == false){
//					categories.add(times.get(i).getCategory());
//				}
				if(selected.compareTo(times.get(i).getCategory()) == 0){
					totalTimeCategory = totalTimeCategory + times.get(i).getTotalTime();
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
			if(weeks < 1) weeks = 1;
			double hoursPWeek = totalTimeCategory / weeks;
			hoursPerWeek.setText(hoursPWeek + "");
			double months = weeks/4;
			if(months < 1){
				hoursPerMonth.setText("");
			}else {
				double hoursPMonth = Math.round(100* (totalTimeCategory / months));
				hoursPMonth = hoursPMonth / 100;
				hoursPerMonth.setText(hoursPMonth + "");
			}
			double hoursPercent = Math.round(100* (totalTimeCategory * 100) / totalTimeAll);
			hoursPercent = hoursPercent / 100;
			hoursSpentPercent.setText(hoursPercent + " %");
	}

	@FXML
	private void handleExit() {
		dialogStage.close();
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

