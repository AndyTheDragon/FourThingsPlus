package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class TaskController
{
    public static void addRoute(Javalin app, ConnectionPool pool) {
        app.get("/tasks/{id}", ctx -> toggleTask(ctx, pool));
        app.get("/tasks", ctx -> showTasks(ctx, pool));
    }

    private static void toggleTask(Context ctx, ConnectionPool pool)
    {
        String toggleTask = ctx.pathParam("id");
        try {
            int taskId = Integer.parseInt(toggleTask);
            ctx.attribute("message", toggleTask);
        }
        catch (NumberFormatException e) {
            ctx.attribute("message", "Unknown task.");
        }
        showTasks(ctx, pool);

    }

    private static void showTasks(Context ctx, ConnectionPool pool)
    {
        if (!(ctx.sessionAttribute("user") instanceof User)) {
            ctx.attribute("message", "You must be logged in.");
            ctx.redirect("/");
        }
        else
        {
            User user = ctx.sessionAttribute("user");
            List<Task> todo = TaskMapper.getTasks(false, user, pool);
            List<Task> tasks = TaskMapper.getTasks(true, user, pool);
            ctx.attribute("todoList", todo);
            ctx.attribute("taskList", tasks);
            ctx.render("tasks.html");
        }

    }
}
