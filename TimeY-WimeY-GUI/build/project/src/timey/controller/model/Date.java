package timey.controller.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Date {
	private final IntegerProperty calendarID;
	private final StringProperty completeDate;
	private final IntegerProperty day;
	private final IntegerProperty month;
	private final IntegerProperty week;
	private final IntegerProperty year;
	private final StringProperty dayName;
	private final StringProperty monthName;
	private final BooleanProperty holidayFlag;
	private final BooleanProperty weekendFlag;
	private final StringProperty bigEvent;
	private ObservableList<Time> times =  FXCollections.observableArrayList();

	public Date(int calendarID, int day, int month, int week, int year,
			String dayName, String monthName, boolean holidayFlag,
			boolean weekendFlag, String bigEvent) {
		this.calendarID = new SimpleIntegerProperty(calendarID);
		this.day = new SimpleIntegerProperty(day);
		this.month = new SimpleIntegerProperty(month);
		this.week = new SimpleIntegerProperty(week);
		this.year = new SimpleIntegerProperty(year);
		this.dayName = new SimpleStringProperty(dayName);
		this.monthName = new SimpleStringProperty(monthName);
		this.holidayFlag = new SimpleBooleanProperty(holidayFlag);
		this.weekendFlag = new SimpleBooleanProperty(weekendFlag);
		this.bigEvent = new SimpleStringProperty(bigEvent);
		this.completeDate = new SimpleStringProperty(this.day.get() + "."
				+ this.month.get() + "." + this.year.get());
	}

	public int getCalendarID() {
		return calendarID.get();
	}

	public IntegerProperty calendarIDProperty() {
		return calendarID;
	}

	public int getDay() {
		return day.get();
	}

	public IntegerProperty dayProperty() {
		return day;
	}

	public void setDay(int day) {
		this.day.set(day);
	}

	public int getMonth() {
		return month.get();
	}

	public IntegerProperty monthProperty() {
		return month;
	}

	public void setMonth(int month) {
		this.month.set(month);
	}

	public int getweek() {
		return week.get();
	}

	public IntegerProperty weekProperty() {
		return week;
	}

	public void setWeek(int week) {
		this.week.set(week);
	}

	public int getYear() {
		return year.get();
	}

	public IntegerProperty yearProperty() {
		return year;
	}

	public void setYear(int year) {
		this.year.set(year);
	}

	public String getDayName() {
		return dayName.get();
	}

	public StringProperty dayNameProperty() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName.set(dayName);
	}

	public String getMonthName() {
		return monthName.get();
	}

	public StringProperty monthNameProperty() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName.set(monthName);
	}

	public boolean getHolidayFlag() {
		return holidayFlag.get();
	}

	public BooleanProperty holidayFlagProperty() {
		return holidayFlag;
	}

	public void setHolidayFlag(boolean holidayFlag) {
		this.holidayFlag.set(holidayFlag);
	}

	public boolean getWeekendFlag() {
		return weekendFlag.get();
	}

	public BooleanProperty weekendFlagProperty() {
		return weekendFlag;
	}

	public void setWeekendFlag(boolean weekendFlag) {
		this.weekendFlag.set(weekendFlag);
	}

	public String getBigEvent() {
		return bigEvent.get();
	}

	public StringProperty bigEventProperty() {
		return bigEvent;
	}

	public void setBigEvent(String bigEvent) {
		this.bigEvent.set(bigEvent);
	}

	public String getCompleteDate() {
		return this.completeDate.get();
	}

	public StringProperty completeDateProperty() {
		return completeDate;
	}

	public void updateCompleteDate() {
		this.completeDate.set(this.day.get() + "." + this.month.get() + "."
				+ this.year.get());
	}
	public void addTime(Time newTime){
		times.add(newTime);
	}
	public ObservableList<Time> getTimes(){
		return this.times;
	}
}
