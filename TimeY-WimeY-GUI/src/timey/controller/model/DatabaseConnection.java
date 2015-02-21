package timey.controller.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SQLviaSSH.ConnectorToSQL;

@SuppressWarnings("deprecation")
public class DatabaseConnection {
	private ConnectorToSQL sql;

	public DatabaseConnection(String rhost, int rport, int lport,
			String sshUser, String sshPassword, String dbUser,
			String dbPassword, String dbName) {
		this.sql = new ConnectorToSQL(rhost, rport, lport, sshUser,
				sshPassword, dbUser, dbPassword, dbName);
		sql.start();
	}

	public ObservableList<Date> getDates(String id1, String id2, int userID) {
		PreparedStatement stat = null;
		Date date = null;
		boolean contains = false;
		ObservableList<Date> dates = FXCollections.observableArrayList();

		try {
			ResultSet result = null;
			if (id1.compareTo("") == 0 | id2.compareTo("") == 0) {
				String show = "SELECT cd.id, cd.day_name, cd.month_name, cd.day, cd.month, cd.year, cd.week, cd.quarter, cd.weekend_flag, uc.event, uc.holiday_flag,  td.id AS time_dimension_id, td.start_time, td.end_time, td.total_time, c.name AS category, td.note FROM user_calendar uc LEFT JOIN time_dimension td on td.user_calendar_calendar_dimension_ref = uc.calendar_dimension_ref LEFT JOIN calendar_dimension cd on cd.id = uc.calendar_dimension_ref LEFT JOIN category c on c.user_ref = uc.user_ref AND td.category_ref = c.id WHERE uc.user_ref = ? AND uc.calendar_dimension_ref >= ?;";
				stat = sql.getConn().prepareStatement(show);
				stat.setInt(1, userID);
				stat.setString(2, id1);
				result = stat.executeQuery();
			} else {
				String show = "SELECT cd.id, cd.day_name, cd.month_name, cd.day, cd.month, cd.year, cd.week, cd.quarter, cd.weekend_flag, uc.event, uc.holiday_flag, td.id AS time_dimension_id, td.start_time, td.end_time, td.total_time, c.name AS category, td.note FROM user_calendar uc LEFT JOIN time_dimension td on td.user_calendar_calendar_dimension_ref = uc.calendar_dimension_ref LEFT JOIN calendar_dimension cd on cd.id = uc.calendar_dimension_ref LEFT JOIN category c on c.user_ref = uc.user_ref AND td.category_ref = c.id WHERE uc.user_ref = ? AND uc.calendar_dimension_ref >= ? AND uc.calendar_dimension_ref <= ?;";
				stat = sql.getConn().prepareStatement(show);
				stat.setInt(1, userID);
				stat.setString(2, id1);
				stat.setString(3, id2);
				result = stat.executeQuery();

			}

			// write results on to the commando line
			while (result.next()) {
				contains = false;
				timey.controller.model.Time time = null;
				String id = result.getString("id");
				int day = result.getInt("day");
				String dayName = result.getString("day_name");
				int week = result.getInt("week");
				int month = result.getInt("month");
				String monthName = result.getString("month_name");
				int year = result.getInt("year");
				String holidayFlag = result.getString("holiday_flag");
				String weekendFlag = result.getString("weekend_flag");
				String event = result.getString("event");
				boolean holiday;
				boolean weekend;
				if (holidayFlag.compareTo("f") == 0) {
					holiday = false;
				} else {
					holiday = true;
				}
				if (weekendFlag.compareTo("f") == 0) {
					weekend = false;
				} else {
					weekend = true;
				}
				java.sql.Time startTime = result.getTime("start_time");
				java.sql.Time endTime = result.getTime("end_time");
				Double totalTime = result.getDouble("total_time");
				String category = result.getString("category");
				String note = result.getString("note");
				String dateTime = day + "." + month + "." + year;
				int timeID = result.getInt("time_dimension_id");
				if (startTime != null | endTime != null) {

					LocalTime startLocalTime = LocalTime.of(
							startTime.getHours(), startTime.getMinutes());
					LocalTime endLocalTime = LocalTime.of(endTime.getHours(),
							endTime.getMinutes());
					time = new Time(dateTime, startLocalTime, endLocalTime,
							totalTime, category, note, Integer.parseInt(id),
							timeID);
				}

				for (int i = 0; i < dates.size(); i++) {
					if (Integer.parseInt(id) == dates.get(i).getCalendarID()) {
						contains = true;
						if (time != null) {
							dates.get(i).getTimes().add(time);
						}
					}
				}
				if (contains == false) {
					date = new Date(Integer.parseInt(id), day, month, week,
							year, dayName, monthName, holiday, weekend, event);
					if (time != null) {
						date.getTimes().add(time);
					}

					dates.add(date);

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dates;
	}

	public ConnectorToSQL getConn() {
		return this.sql;
	}

	public ObservableList<String> getCategories(int userID) {
		boolean contains = false;
		ObservableList<String> categoryData = FXCollections
				.observableArrayList();
		String categoryQuery = "SELECT * FROM category where user_ref = ?;";
		try {
			PreparedStatement stat = sql.getConn().prepareStatement(
					categoryQuery);
			stat.setInt(1, userID);
			ResultSet result = stat.executeQuery();

			while (result.next()) {
				contains = false;
				String category = result.getString("name");
				for (int i = 0; i < categoryData.size(); i++) {
					if (category.compareTo(categoryData.get(i)) == 0) {
						contains = true;
					}
				}
				if (contains == false) {
					categoryData.add(category);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryData;
	}

	public void handleRemoveTime(int userID, Time selectedTime) {
		try {
			String deleteQuery = "DELETE FROM time_dimension WHERE user_calendar_user_ref = ? AND user_calendar_calendar_dimension_ref = ? AND id = ?;";
			PreparedStatement stat = sql.getConn()
					.prepareStatement(deleteQuery);
			stat.setInt(1, userID);
			stat.setInt(2, selectedTime.getCalendarID());
			stat.setInt(3, selectedTime.getTimeID());
			stat.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getCategoryID(int userID, String categoryName) {
		// get categoryID
		int categoryID = 0;
		String selectQuery = "SELECT * FROM category WHERE user_ref = ? AND name = ?";
		PreparedStatement statSelect;
		try {
			statSelect = sql.getConn().prepareStatement(selectQuery);
			statSelect.setInt(1, userID);
			statSelect.setString(2, categoryName);
			ResultSet result = statSelect.executeQuery();
			while (result.next()) {
				categoryID = result.getInt("id");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return categoryID;
	}

	public boolean handleEditTime(Time time, int userID, String startTime,
			String endTime, String totalTime, String note, int calendarID,
			int categoryID) {
		boolean updated = false;

		// insert new row
		if (time == null) {
			try {
				String insertQuery = "INSERT INTO time_dimension (start_time, end_time, total_time, note, user_calendar_user_ref, user_calendar_calendar_dimension_ref, category_ref) VALUES (?,?,?,?,?,?,?);";
				PreparedStatement statInsert = sql.getConn().prepareStatement(
						insertQuery);

				statInsert.setString(1, startTime);
				statInsert.setString(2, endTime);
				statInsert.setDouble(3, Double.parseDouble(totalTime));
				statInsert.setString(4, note);
				statInsert.setInt(5, userID);
				statInsert.setInt(6, calendarID);
				statInsert.setInt(7, categoryID);
				updated = statInsert.execute();
				int updates = statInsert.getUpdateCount();
				if(updates >= 1) updated = true;
			} catch (SQLException e) {
				Dialogs.create()
						.title("Edit Time")
						.masthead("Can't accept your entries")
						.message(
								"Please check for Times (HH:MM) and if you've chosen a category. You have to check a category!")
						.showWarning();
				e.printStackTrace();
			}

			// update existing row
		} else {

			try {
				String updateQuery = "UPDATE time_dimension SET start_time = ?, end_time = ? , total_time = ? , note = ? , category_ref = ? WHERE id = ? ";
				PreparedStatement statUpdate = sql.getConn().prepareStatement(
						updateQuery);
				statUpdate.setString(1, startTime);
				statUpdate.setString(2, endTime);
				statUpdate.setDouble(3, Double.parseDouble(totalTime));
				statUpdate.setString(4, note);
				statUpdate.setInt(5, categoryID);
				statUpdate.setInt(6, time.getTimeID());
				int updates = statUpdate.executeUpdate();
				if(updates >= 1) updated = true;
			} catch (SQLException e) {
				Dialogs.create()
						.title("Edit Time")
						.masthead("Can't accept your entries")
						.message(
								"Please check for Times (HH:MM) and if you've chosen a category. You have to check a category!")
						.showWarning();
				e.printStackTrace();
			}
		}
		return updated;
	}
	public void handleRemoveCategory(String selected, int userID){
		String deleteQuery = "DELETE FROM category WHERE name = ? AND user_ref = ?";
		try {
			PreparedStatement stat = sql.getConn().prepareStatement(deleteQuery);
		stat.setString(1, selected);
		stat.setInt(2, userID);
		stat.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean handleEditCategory(String newCategoryName, int userID, String oldCategoryName){
		boolean updated = false;
		try {
			String categoryUpdate = "UPDATE category SET name = ? WHERE user_ref = ? AND name = ?;";
			PreparedStatement stat = sql.getConn().prepareStatement(categoryUpdate);
			stat.setString(1, newCategoryName);
			stat.setInt(2, userID);
			stat.setString(3, oldCategoryName);
			int changed = stat.executeUpdate();
			if(changed == 1){
				updated = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updated;
	}
	
	public void handleNewCategory(String categoryName, int userID){
		try {
			String categoryInsert = "INSERT INTO category (name, user_ref) VALUES (?, ?);";
			PreparedStatement stat;
			stat = sql.getConn().prepareStatement(categoryInsert);
			stat.setString(1, categoryName);
			stat.setInt(2, userID);
			stat.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
