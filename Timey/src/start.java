import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Scanner;

import utils.SQLviaSSH.ConnectorToSQL;

/**
 * 
 * 
 * <b>Project:</b> Timey
 * <p>
 * <b>Packages:</b>
 * </p>
 * <p>
 * <b>File:</b> start.java
 * </p>
 * <p>
 * <b>last update:</b> 11.02.2015
 * </p>
 * <p>
 * <b>Time:</b> 11:30:51
 * </p>
 * <b>Description:</b>
 * <p>
 * Timey is a time tracker, that was the first idea, maybe it's getting bigger
 * :-). First Version to check on some core features like entering an event, a
 * new time or show Dates.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.1
 */
public class start {

	public static void main(String[] args) {
		String rhost = args[0];
		int rport = Integer.parseInt(args[1]);
		int lport = Integer.parseInt(args[2]);
		String dbUser = args[3];
		String dbPassword = args[4];
		String sshUser = args[5];
		String sshPassword = args[6];
		String dbName = args[7];

		ConnectorToSQL sql = new ConnectorToSQL(rhost, rport, lport, sshUser,
				sshPassword, dbUser, dbPassword, dbName);
		sql.start();
		boolean shutdown = false;
		Scanner scanny = new Scanner(System.in);
		while (!shutdown) {
			System.out.println("--------------------------");
			System.out
					.println("What you wanna do?\n1. Show Dates\n2. Enter new Event\n3. Enter new Time\n4. Exit\nChoice: ");
			int menu = scanny.nextInt();
			// Menu
			switch (menu) {
			case 1: { // show Dates
				showDates(scanny, sql);
				break;
			}
			case 2: { // enter new Event
				enterEvent(scanny, sql);
				break;
			}
			case 3: { // enter new time slot
				enterTime(scanny, sql);
				break;
			}
			case 4: { // exit
				System.out.println("--------------------------");
				System.out.println("Shutting down...");
				shutdown = true;
				break;
			}
			}
		}
		sql.stop();
	}

	/**
	 * This Method shows dates. The User will be asked if he wants to show a
	 * period or single date and which day to which day it is.
	 * 
	 * @param scanny
	 *            Scanner object for getting input of the user
	 * @param sql
	 *            SQL Connection
	 */
	private static void showDates(Scanner scanny, ConnectorToSQL sql) {
		PreparedStatement stat = null;
		System.out.println("--------------------------");
		String show = "SELECT * FROM calendar_dimension LEFT JOIN time_dimension ON time_dimension.calendar_dimension_ref = calendar_dimension.id WHERE calendar_dimension.id >= ? AND calendar_dimension.id <= ?;";
		try {
			int x;
			stat = sql.getConn().prepareStatement(show);
			System.out.println("Want to show a period? (y/n)");
			String input = scanny.next();
			if (input.compareTo("y") == 0) {
				x = 2;
			} else if (input.compareTo("n") == 0) {
				x = 1;
			} else {
				System.err
						.println("Entered a not allowed character for this decision!");
				return;
			}
			for (int i = 1; i <= x; i++) {
				int id = chooseDate(scanny);
				if (x != 1) {
					stat.setInt(i, id);
				} else {
					stat.setInt(1, id);
					stat.setInt(2, id);
				}
			}
			ResultSet result = stat.executeQuery();

			// write results on to the commando line
			while (result.next()) {
				String day = result.getString("day");
				String dayName = result.getString("day_name");
				String month = result.getString("month_name");
				String year = result.getString("year");
				String event = result.getString("big_event");
				Time startTime = result.getTime("start_time");
				Time endTime = result.getTime("end_time");
				Double totalTime = result.getDouble("total_time");
				String category = result.getString("category");
				String note = result.getString("note");
				String[] sTime = null;
				String[] eTime = null;
				if (startTime != null) {
					sTime = startTime.toString().split(":");
				}
				if (endTime != null) {
					eTime = endTime.toString().split(":");
				}

				if (event == null)
					event = "";
				else
					event = "(" + event + ")";
				if (note == null)
					note = "";
				else
					note = "(" + note + ")";
				if (startTime == null || endTime == null)
					System.out.println("Result: " + dayName + ", der " + day
							+ ". " + month + " " + year + " " + event);
				else
					System.out.println("Result: "
							+ dayName
							+ ", der "
							+ day
							+ ". "
							+ month
							+ " "
							+ year
							+ " "
							+ event
							+ " "
							+ (startTime != null ? sTime[0] + ":" + sTime[1]
									: startTime)
							+ " - "
							+ (endTime != null ? eTime[0] + ":" + eTime[1]
									: endTime) + " = " + totalTime
							+ " hours for " + category + " " + note);

			}
		} catch (SQLException e) {
			System.err
					.println("Error while executing Statements for showing dates!");
		}

	}

	/**
	 * This Method is for entering new events into the database
	 * 
	 * @param scanny
	 *            Scanner Object for the input of the user
	 * @param sql
	 *            SQL Connection
	 */
	public static void enterEvent(Scanner scanny, ConnectorToSQL sql) {
		PreparedStatement stat = null;
		System.out.println("--------------------------");
		String update = "UPDATE calendar_dimension set big_event = ? WHERE id = ?";
		try {
			stat = sql.getConn().prepareStatement(update);
			String input = "y";
			while (input.compareTo("n") != 0) {
				int id = chooseDate(scanny);
				scanny = null;
				scanny = new Scanner(System.in);
				System.out.println("Please enter a Note for that day:");
				String note = scanny.nextLine();
				stat.setString(1, note);
				stat.setInt(2, id);
				stat.executeUpdate();
				System.out
						.println("Wanna take other notes, maybe for other days? (y/n)");
				input = scanny.next();
			}
		} catch (SQLException e) {
			System.err
					.println("Error while executing Statements for showing dates!");
		}
	}

	/**
	 * This Method is for entering time slots for specific dates
	 * 
	 * @param scanny
	 *            Scanner Object for the input of the user
	 * @param sql
	 *            SQL Connection
	 */
	public static void enterTime(Scanner scanny, ConnectorToSQL sql) {
		PreparedStatement stat = null;
		System.out.println("--------------------------");
		String insert = "INSERT INTO time_dimension (start_time, end_time, category, note, calendar_dimension_ref, total_time) values (?, ?, ?, ?, ?,  ((hour(end_time) - hour(start_time))*60 + (Minute(end_time) - Minute(start_time)))/60);";
		try {
			stat = sql.getConn().prepareStatement(insert);
			String input = "y";
			while (input.compareTo("n") != 0) {
				int id = chooseDate(scanny);
				scanny = null;
				scanny = new Scanner(System.in);
				System.out
						.println("Please enter the start time in format: HH:MM");
				String time = scanny.nextLine();
				stat.setString(1, time);
				System.out
						.println("Please enter the end time in format: HH:MM");
				time = scanny.nextLine();
				stat.setString(2, time);
				System.out.println("Please enter category for this time: ");
				String category = scanny.nextLine();
				System.out.println("Please leave a note if you like: ");
				String note = scanny.nextLine();
				if (note.compareTo("") == 0)
					note = null;
				stat.setString(3, category);
				stat.setString(4, note);
				stat.setInt(5, id);
				stat.execute();
				System.out.println("Wanna add other times? (y/n)");
				input = scanny.next();
			}
		} catch (SQLException e) {
			System.err
					.println("Error while executing Statements for showing dates!");
		}
	}

	/**
	 * Chosing Dialog for a date
	 * 
	 * @param scanny
	 *            Scanner Object for the input of the user
	 * @return an integer for an ID for a date in the database
	 */
	private static int chooseDate(Scanner scanny) {
		String id;
		System.out.println("---\nWhich day?");
		String day = scanny.next();
		if (Integer.parseInt(day) < 10)
			day = 0 + day;
		id = day;
		System.out.println("---\nWhich month?");
		String month = scanny.next();
		if (Integer.parseInt(month) < 10)
			month = 0 + month;
		id = month + id;
		System.out.println("---\nWhich year?");
		id = scanny.next() + id;
		return Integer.parseInt(id);
	}
}
