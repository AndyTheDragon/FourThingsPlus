package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class UserController
{

    public static void addRoute(Javalin app, ConnectionPool connectionPool)
    {
        app.post("/login", ctx -> login(ctx, connectionPool) );

    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try
        {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("user", user);
            ctx.render("tasks.html");
            //ctx.redirect("/tasks");
        }
        catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");

        }
    }

}
