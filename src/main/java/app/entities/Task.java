package app.entities;

import java.util.Date;

public class Task
{
    private final int task_id;
    private final String task_name;
    private final String task_description;
    private boolean done;
    private final User user;
    private Date last_changed;

    public Task(int task_id, String task_name, String task_description, User user, Date last_changed, boolean done)
    {
        this.task_id = task_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.user = user;
        this.last_changed = last_changed;
        this.done = done;
    }

    public boolean isDone()
    {
        return done;
    }

    public Date getLast_changed()
    {
        return last_changed;
    }

    public String getTask_description()
    {
        return task_description;
    }

    public int getTask_id()
    {
        return task_id;
    }

    public String getTask_name()
    {
        return task_name;
    }

    public User getUser()
    {
        return user;
    }

    public void change() {
        this.done = !this.done;
        this.last_changed = new Date();
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }

    public void setLast_changed(Date last_changed)
    {
        this.last_changed = last_changed;
    }
}
