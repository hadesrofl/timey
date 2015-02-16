package timey.controller.model;

import java.time.LocalTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Time {
	private final StringProperty date;
	private final ObjectProperty<LocalTime> startTime;
	private final ObjectProperty<LocalTime> endTime;
	private final DoubleProperty totalTime;
	private final StringProperty category;
	private final StringProperty note;
	private final IntegerProperty calendarID;

	public Time(String date, LocalTime startTime, LocalTime endTime, double totalTime,
			String category, String note, int calendarID) {
		this.date = new SimpleStringProperty(date);
		this.startTime = new SimpleObjectProperty<LocalTime>(startTime);
		this.endTime = new SimpleObjectProperty<LocalTime>(endTime);
		this.totalTime = new SimpleDoubleProperty(totalTime);
		this.category = new SimpleStringProperty(category);
		this.note = new SimpleStringProperty(note);
		this.calendarID = new SimpleIntegerProperty(calendarID);
	}

	public String getDate(){
		return date.get();
	}
	public StringProperty dateProperty(){
		return date;
	}
	public void setDate(String date){
		this.date.set(date);
	}
	public LocalTime getStartTime() {
		return startTime.get();
	}

	public ObjectProperty<LocalTime> startTimeProperty() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime.set(startTime);
	}

	public LocalTime getEndTime() {
		return endTime.get();
	}

	public ObjectProperty<LocalTime> endTimeProperty() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime.set(endTime);
	}

	public double getTotalTime() {
		return totalTime.get();
	}

	public DoubleProperty totalTimeProperty() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime.set(totalTime);
	}

	public String getCategory() {
		return category.get();
	}

	public StringProperty categoryProperty() {
		return category;
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	public String getNote() {
		return note.get();
	}

	public StringProperty noteProperty() {
		return note;
	}

	public void setNote(String note) {
		this.note.set(note);
	}

	public int getCalendarID() {
		return calendarID.get();
	}

	public IntegerProperty calendarIDProperty() {
		return calendarID;
	}
}
