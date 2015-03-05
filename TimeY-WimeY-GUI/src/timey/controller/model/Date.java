package timey.controller.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.model
 * </p>
 * <p>
 * <b>File:</b> Date.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 11:10:04
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class specifies a date object in this app.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class Date {
	/**
	 * Integer Property for a calendarID to use in javaFX elements
	 */
	private final IntegerProperty calendarID;
	/**
	 * String Property for a String of the complete Date in german layout
	 */
	private final StringProperty completeDate;
	/**
	 * Integer Property for a day to use in javaFX elements
	 */
	private final IntegerProperty day;
	/**
	 * Integer Property for a month to use in javaFX elements
	 */
	private final IntegerProperty month;
	/**
	 * Integer Property for a week to use in javaFX elements
	 */
	private final IntegerProperty week;
	/**
	 * Integer Property for a year to use in javaFX elements
	 */
	private final IntegerProperty year;
	/**
	 * String Property of the name of a day to use in javaFX elements
	 */
	private final StringProperty dayName;
	/**
	 * String Property of the name of a month to use in javaFX elements
	 */
	private final StringProperty monthName;
	/**
	 * Boolean Property of a flag for holidays to use in javaFX elements
	 */
	private final BooleanProperty holidayFlag;
	/**
	 * Boolean Property of a flag for weekends to use in javaFX elements
	 */
	private final BooleanProperty weekendFlag;
	/**
	 * String Property of a event note to use in javaFX elements
	 */
	private final StringProperty event;
	/**
	 * List of time objects regarding this date object
	 */
	private ObservableList<Time> times = FXCollections.observableArrayList();

	/**
	 * Constructor
	 * 
	 * @param calendarID
	 *            is the id of the date in the calendar as integer
	 * @param day
	 *            is the number of the day as integer
	 * @param month
	 *            is the number of the month as integer
	 * @param week
	 *            is the number of the week as integer
	 * @param year
	 *            is the number of the year as integer
	 * @param dayName
	 *            is the name of the day as String
	 * @param monthName
	 *            is the name of the month as String
	 * @param holidayFlag
	 *            is the boolean to show if it is a holiday for the user or not
	 * @param weekendFlag
	 *            is the boolean to show if it is a day at the weekend or not
	 * @param event
	 *            is a note for the day as String
	 */
	public Date(int calendarID, int day, int month, int week, int year,
			String dayName, String monthName, boolean holidayFlag,
			boolean weekendFlag, String event) {
		this.calendarID = new SimpleIntegerProperty(calendarID);
		this.day = new SimpleIntegerProperty(day);
		this.month = new SimpleIntegerProperty(month);
		this.week = new SimpleIntegerProperty(week);
		this.year = new SimpleIntegerProperty(year);
		this.dayName = new SimpleStringProperty(dayName);
		this.monthName = new SimpleStringProperty(monthName);
		this.holidayFlag = new SimpleBooleanProperty(holidayFlag);
		this.weekendFlag = new SimpleBooleanProperty(weekendFlag);
		this.event = new SimpleStringProperty(event);
		this.completeDate = new SimpleStringProperty(this.day.get() + "."
				+ this.month.get() + "." + this.year.get());
	}

	/**
	 * Getter of the calendar id
	 * 
	 * @return the calendar id as integer
	 */
	public int getCalendarID() {
		return calendarID.get();
	}

	/**
	 * Getter of the property of the calendar id
	 * 
	 * @return the calendarIDProperty
	 */
	public IntegerProperty calendarIDProperty() {
		return calendarID;
	}

	/**
	 * Getter of the number of the day
	 * 
	 * @return the day as integer
	 */
	public int getDay() {
		return day.get();
	}

	/**
	 * Getter of the property of the number of the day
	 * 
	 * @return dayProperty
	 */
	public IntegerProperty dayProperty() {
		return day;
	}

	/**
	 * Setter of the day number
	 * 
	 * @param day
	 *            is the number of the day
	 */
	public void setDay(int day) {
		this.day.set(day);
	}

	/**
	 * Getter of the number of the month
	 * 
	 * @return the month as integer
	 */
	public int getMonth() {
		return month.get();
	}

	/**
	 * Getter of the property of the number of the month
	 * 
	 * @return monthProperty
	 */
	public IntegerProperty monthProperty() {
		return month;
	}

	/**
	 * Setter of the number of the month
	 * 
	 * @param month
	 *            is the number of the month
	 */
	public void setMonth(int month) {
		this.month.set(month);
	}

	/**
	 * Getter of the number of the week
	 * 
	 * @return the week as integer
	 */
	public int getweek() {
		return week.get();
	}

	/**
	 * Getter of the property of the number of the week
	 * 
	 * @return weekProperty
	 */
	public IntegerProperty weekProperty() {
		return week;
	}

	/**
	 * Setter of the number of the week
	 * 
	 * @param week
	 *            is the number of the week
	 */
	public void setWeek(int week) {
		this.week.set(week);
	}

	/**
	 * Getter of the number of the year
	 * 
	 * @return the year as integer
	 */
	public int getYear() {
		return year.get();
	}

	/**
	 * Getter of the property of the number of the year
	 * 
	 * @return yearProperty
	 */
	public IntegerProperty yearProperty() {
		return year;
	}

	/**
	 * Setter of the number of the year
	 * 
	 * @param year
	 *            is the number of the year
	 */
	public void setYear(int year) {
		this.year.set(year);
	}

	/**
	 * Getter of the name of the day
	 * 
	 * @return a String with the day name
	 */
	public String getDayName() {
		return dayName.get();
	}

	/**
	 * Getter of the Property of the name of the day
	 * 
	 * @return dayNameProperty
	 */
	public StringProperty dayNameProperty() {
		return dayName;
	}

	/**
	 * Setter of the name of the day
	 * 
	 * @param dayName
	 *            the name of the day
	 */
	public void setDayName(String dayName) {
		this.dayName.set(dayName);
	}

	/**
	 * Getter of the name of the month
	 * 
	 * @return the name of the month as String
	 */
	public String getMonthName() {
		return monthName.get();
	}

	/**
	 * Getter of the property of the name of the month
	 * 
	 * @return monthNameProperty
	 */
	public StringProperty monthNameProperty() {
		return monthName;
	}

	/**
	 * Setter of the name of the month
	 * 
	 * @param monthName
	 *            is the name of the month
	 */
	public void setMonthName(String monthName) {
		this.monthName.set(monthName);
	}

	/**
	 * Getter of the holiday flag
	 * 
	 * @return the holiday flag as boolean
	 */
	public boolean getHolidayFlag() {
		return holidayFlag.get();
	}

	/**
	 * Getter of the property of the holiday flag
	 * 
	 * @return holidayFlagProperty
	 */
	public BooleanProperty holidayFlagProperty() {
		return holidayFlag;
	}

	/**
	 * Setter of the holiday flag
	 * 
	 * @param holidayFlag
	 *            is the new holiday flag
	 */
	public void setHolidayFlag(boolean holidayFlag) {
		this.holidayFlag.set(holidayFlag);
	}

	/**
	 * Getter of the weekend flag
	 * 
	 * @return weekend flag as boolean
	 */
	public boolean getWeekendFlag() {
		return weekendFlag.get();
	}

	/**
	 * Getter of the property of the weekend flag
	 * 
	 * @return weekendFlagProperty
	 */
	public BooleanProperty weekendFlagProperty() {
		return weekendFlag;
	}

	/**
	 * Setter of the weekend flag
	 * 
	 * @param weekendFlag
	 *            is the new weekend flag
	 */
	public void setWeekendFlag(boolean weekendFlag) {
		this.weekendFlag.set(weekendFlag);
	}

	/**
	 * Getter of the event
	 * 
	 * @return the event as String
	 */
	public String getEvent() {
		return event.get();
	}

	/**
	 * Getter of the property of the event
	 * 
	 * @return eventProperty
	 */
	public StringProperty eventProperty() {
		return event;
	}

	/**
	 * Setter of the event
	 * 
	 * @param event
	 *            a string for the new event
	 */
	public void setEvent(String event) {
		this.event.set(event);
	}

	/**
	 * Getter of the complete date
	 * 
	 * @return a string of the complete date
	 */
	public String getCompleteDate() {
		return this.completeDate.get();
	}

	/**
	 * Getter of the property of the complete date
	 * 
	 * @return completeDateProperty
	 */
	public StringProperty completeDateProperty() {
		return completeDate;
	}

	/**
	 * Updates the complete data field with the data of the day, month and year
	 * field
	 */
	public void updateCompleteDate() {
		this.completeDate.set(this.day.get() + "." + this.month.get() + "."
				+ this.year.get());
	}

	/**
	 * Adds a time to the date object
	 * 
	 * @param newTime
	 *            is the time object that shall be added to the list of time
	 *            objects of this date
	 */
	public void addTime(Time newTime) {
		times.add(newTime);
	}

	/**
	 * Getter of the list of time objects
	 * 
	 * @return times
	 */
	public ObservableList<Time> getTimes() {
		return this.times;
	}
}
