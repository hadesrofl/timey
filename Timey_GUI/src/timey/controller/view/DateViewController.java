package timey.controller.view;

import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timey.controller.model.Date;
import timey.controller.model.Time;
import timey.controller.MainApp;

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
	private DatePicker from;
	@FXML
	private DatePicker to;
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

	private MainApp mainApp;

	public DateViewController() {

	}

	@FXML
	private void initialize() {
		int i = 0;
		
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

	}
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		System.out.println("Anzahl der Elemente: " + this.mainApp.getDates().get(0).getTimes().size());
		this.dateTable.setItems(mainApp.getDates());
		this.timeTable.setItems(mainApp.getDates().get(0).getTimes());
	}
}
