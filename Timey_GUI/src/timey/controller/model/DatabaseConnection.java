package timey.controller.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.SQLviaSSH.ConnectorToSQL;

public class DatabaseConnection {
	private ConnectorToSQL sql;

	public DatabaseConnection(String rhost, int rport, int lport,
			String sshUser, String sshPassword, String dbUser,
			String dbPassword, String dbName) {
		this.sql = new ConnectorToSQL(rhost, rport, lport, sshUser,
				sshPassword, dbUser, dbPassword, dbName);
		sql.start();
	}

	public ObservableList<Date> getDates(int id1, int id2) {
		PreparedStatement stat = null;
		Date date = null;
		ObservableList<Date> dates = FXCollections.observableArrayList();
		try {
			String show = "SELECT * FROM calendar_dimension LEFT JOIN time_dimension ON time_dimension.calendar_dimension_ref = calendar_dimension.id WHERE calendar_dimension.id >= ? AND calendar_dimension.id <= ?;";
			stat = sql.getConn().prepareStatement(show);
			stat.setInt(1, id1);
			stat.setInt(2, id2);
			ResultSet result = stat.executeQuery();

			// write results on to the commando line
			while (result.next()) {
				String id = result.getString("id");
				int day = result.getInt("day");
				String dayName = result.getString("day_name");
				int week = result.getInt("week");
				int month = result.getInt("month");
				String monthName = result.getString("month_name");
				int year = result.getInt("year");
				String holidayFlag = result.getString("holiday_flag");
				String weekendFlag = result.getString("weekend_flag");
				String event = result.getString("big_event");
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
				// String event = result.getString("big_event");
				// Time startTime = result.getTime("start_time");
				// Time endTime = result.getTime("end_time");
				// Double totalTime = result.getDouble("total_time");
				// String category = result.getString("category");
				// String note = result.getString("note");
				date = new Date(Integer.parseInt(id), day, month, week, year,
						dayName, monthName, holiday, weekend, event);
				dates.add(date);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dates;
	}
	public ConnectorToSQL getConn(){
		return this.sql;
	}
}
