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

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.model
 * </p>
 * <p>
 * <b>File:</b> Time.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 11:57:19
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class specifies Time Objects in this app
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class Time {
	/**
	 * String Property of the date of this time object
	 */
	private final StringProperty date;
	/**
	 * Object Property of the local time object for the start time
	 */
	private final ObjectProperty<LocalTime> startTime;
	/**
	 * Object Property of the local time object for the end time
	 */
	private final ObjectProperty<LocalTime> endTime;
	/**
	 * Double Property of the total time
	 */
	private final DoubleProperty totalTime;
	/**
	 * String Property of the category
	 */
	private final StringProperty category;
	/**
	 * String Property of the note
	 */
	private final StringProperty note;
	/**
	 * Integer Property of the calendar id
	 */
	private final IntegerProperty calendarID;
	/**
	 * Integer Property of the time id
	 */
	private final IntegerProperty timeID;

	/**
	 * Constructor
	 * 
	 * @param date
	 *            is the date to which this time object is assigned to
	 * @param startTime
	 *            is the start time as local time object
	 * @param endTime
	 *            is the end time as local time object
	 * @param totalTime
	 *            is the total time as double
	 * @param category
	 *            is the name of the category as String
	 * @param note
	 *            is the note to this time as a String
	 * @param calendarID
	 *            is the id of the date in the database as integer
	 * @param timeID
	 *            is the id of the time in the database as integer
	 */
	public Time(String date, LocalTime startTime, LocalTime endTime,
			double totalTime, String category, String note, int calendarID,
			int timeID) {
		this.date = new SimpleStringProperty(date);
		this.startTime = new SimpleObjectProperty<LocalTime>(startTime);
		this.endTime = new SimpleObjectProperty<LocalTime>(endTime);
		this.totalTime = new SimpleDoubleProperty(totalTime);
		this.category = new SimpleStringProperty(category);
		this.note = new SimpleStringProperty(note);
		this.calendarID = new SimpleIntegerProperty(calendarID);
		this.timeID = new SimpleIntegerProperty(timeID);
	}

	/**
	 * Getter of the date
	 * 
	 * @return date
	 */
	public String getDate() {
		return date.get();
	}

	/**
	 * Getter of the property of the date
	 * 
	 * @return dateProperty
	 */
	public StringProperty dateProperty() {
		return date;
	}

	/**
	 * Setter of the date
	 * 
	 * @param date
	 *            is the new date
	 */
	public void setDate(String date) {
		this.date.set(date);
	}

	/**
	 * Getter of the start time of this object
	 * 
	 * @return startTime
	 */
	public LocalTime getStartTime() {
		return startTime.get();
	}

	/**
	 * Getter of the Object Property of the start time
	 * 
	 * @return startTimeProperty
	 */
	public ObjectProperty<LocalTime> startTimeProperty() {
		return startTime;
	}

	/**
	 * Setter of the start time
	 * 
	 * @param startTime
	 *            is the new start time
	 */
	public void setStartTime(LocalTime startTime) {
		this.startTime.set(startTime);
	}

	/**
	 * Getter of the end time
	 * 
	 * @return endTime
	 */
	public LocalTime getEndTime() {
		return endTime.get();
	}

	/**
	 * Getter of the property of the end time
	 * 
	 * @return endTimeProperty
	 */
	public ObjectProperty<LocalTime> endTimeProperty() {
		return endTime;
	}

	/**
	 * Setter of the end time
	 * 
	 * @param endTime
	 *            is the new end time
	 */
	public void setEndTime(LocalTime endTime) {
		this.endTime.set(endTime);
	}

	/**
	 * Getter of the total time
	 * 
	 * @return totalTime
	 */
	public double getTotalTime() {
		return totalTime.get();
	}

	/**
	 * Getter the property of the total time
	 * 
	 * @return totalTimeProperty
	 */
	public DoubleProperty totalTimeProperty() {
		return totalTime;
	}

	/**
	 * Setter of the total time
	 * 
	 * @param totalTime
	 *            is the new total time
	 */
	public void setTotalTime(double totalTime) {
		this.totalTime.set(totalTime);
	}

	/**
	 * Getter of the category
	 * 
	 * @return the name of the category as String
	 */
	public String getCategory() {
		return category.get();
	}

	/**
	 * Getter of the property of the category
	 * 
	 * @return categoryProperty
	 */
	public StringProperty categoryProperty() {
		return category;
	}

	/**
	 * Setter of the category
	 * 
	 * @param category
	 *            is the new category
	 */
	public void setCategory(String category) {
		this.category.set(category);
	}

	/**
	 * Getter of the note
	 * 
	 * @return note
	 */
	public String getNote() {
		return note.get();
	}

	/**
	 * Getter of the property of the note
	 * 
	 * @return noteProperty
	 */
	public StringProperty noteProperty() {
		return note;
	}

	/**
	 * Setter of the note
	 * 
	 * @param note
	 *            is the new note
	 */
	public void setNote(String note) {
		this.note.set(note);
	}

	/**
	 * Getter of the calendar id
	 * 
	 * @return calendarID
	 */
	public int getCalendarID() {
		return calendarID.get();
	}

	/**
	 * Getter of the property of the calendar id
	 * 
	 * @return calendarIDProperty
	 */
	public IntegerProperty calendarIDProperty() {
		return calendarID;
	}

	/**
	 * Getter of the time id
	 * 
	 * @return timeID
	 */
	public int getTimeID() {
		return timeID.get();
	}

	/**
	 * Getter of the property of the time id
	 * 
	 * @return timeIDProperty
	 */
	public IntegerProperty timeIDProperty() {
		return timeID;
	}
}
