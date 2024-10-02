package app.persistence;

import app.entities.Task;
import app.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TaskMapper
{
    public static Map<Integer, Task> getTasks(int user_id, ConnectionPool connectionPool) {
        Map<Integer, Task> tasks = new HashMap<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";

        return tasks;
    }

    public static List<Task> getTasks(boolean status, User user, ConnectionPool pool)
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE user_id = ? AND done= ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getUserId());
            stmt.setBoolean(2, status);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int taskId = rs.getInt("task_id");
                String taskName = rs.getString("task_name");
                String taskDescription = rs.getString("task_description");
                boolean done = rs.getBoolean("done");
                LocalDate lastChanged = rs.getDate("last_changed").toLocalDate();
                tasks.add(new Task(taskId, taskName, taskDescription, user, lastChanged, done));
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return tasks;
    }
}
