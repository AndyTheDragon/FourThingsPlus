package app.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task
{
    private final int taskId;
    private final String taskName;
    private final String taskDescription;
    private boolean done;
    private final User user;
    private LocalDate lastChanged;

    public Task(int taskId, String taskName, String taskDescription, User user, LocalDate lastChanged, boolean done)
    {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.user = user;
        this.lastChanged = lastChanged;
        this.done = done;
    }

    public boolean isDone()
    {
        return done;
    }

    public LocalDate getLastChanged()
    {
        return lastChanged;
    }

    public String getTaskDescription()
    {
        return taskDescription;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public User getUser()
    {
        return user;
    }

    public String getAge() {
        LocalDate currentDate = LocalDate.now();
        long age = ChronoUnit.DAYS.between(lastChanged,currentDate);
        return age + " days ago";

    }
}
