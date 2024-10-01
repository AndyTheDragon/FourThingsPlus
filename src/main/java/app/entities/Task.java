package app.entities;

import java.util.Date;

public class Task
{
    private int task_id;
    private String task_name;
    private String task_description;
    private boolean done;
    private User user;
    private Date last_changed;
}
