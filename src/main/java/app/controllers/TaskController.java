package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskController
{
    public static void addRoute(Javalin app, ConnectionPool conn) {
        app.get("/tasks/cleardone", ctx -> cleardone(ctx, conn));
        app.get("/tasks/{id}", ctx -> toggleTask(ctx, conn));
        app.get("/tasks", ctx -> showTasks(ctx, conn));
        app.get("/task", ctx -> showTasks(ctx, conn));
        app.post("/task", ctx -> addTask(ctx, conn));

    }

    private static void cleardone(@NotNull Context ctx, ConnectionPool conn)
    {

    }

    private static void addTask(@NotNull Context ctx, ConnectionPool conn)
    {
        String taskName = ctx.formParam("taskname");
        String taskDesc = ctx.formParam("taskdesc");
        User user = ctx.sessionAttribute("user");
        if (user==null || taskName.isBlank() || taskDesc.isBlank()) {
            ctx.redirect("/tasks");
            ctx.attribute("message", "You must login first");
        }
        else {
            try
            {
                TaskMapper.addTask(taskName, taskDesc, user, conn);
            } catch (DatabaseException e)
            {
                ctx.attribute("message", e.getMessage());
            }
            ctx.redirect("/tasks");
        }

    }

    private static void toggleTask(Context ctx, ConnectionPool conn)
    {
        String toggleTask = ctx.pathParam("id");
        try {
            int taskId = Integer.parseInt(toggleTask);
            TaskMapper.toggleTask(taskId,conn);
        }
        catch (NumberFormatException e) {
            ctx.attribute("message", "Unknown task.");
        }
        catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
        }
        showTasks(ctx, conn);

    }

    private static void showTasks(Context ctx, ConnectionPool pool)
    {
        if ((ctx.sessionAttribute("user") == null)) {
            ctx.attribute("message", "You must be logged in.");

            ctx.redirect("/");
        }
        else
        {
            try
            {
                User user = ctx.sessionAttribute("user");
                List<Task> todo = TaskMapper.getTasks(false, user, pool);
                List<Task> tasks = TaskMapper.getTasks(true, user, pool);
                ctx.attribute("todoList", todo);
                ctx.attribute("taskList", tasks);
                ctx.render("tasks.html");
            }
            catch (DatabaseException e) {
                ctx.redirect("/");
                ctx.attribute("message", e.getMessage());
            }
        }

    }
}
