package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;


public class UserController
{

    public static void addRoute(Javalin app, ConnectionPool connectionPool)
    {
        app.post("/login", ctx -> login(ctx, connectionPool) );
        app.get("/login", ctx ->  ctx.render("index.html"));
        app.get("/logout", ctx -> logout(ctx));
        app.post("/register", ctx -> createUser(ctx, connectionPool));
        app.get("/register", ctx -> ctx.render("createuser.html"));


    }

    private static void createUser(@NotNull Context ctx, ConnectionPool connectionPool)
    {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        if ((username!=null && username.isBlank()) || password.isBlank()) {
            ctx.attribute("message", "Username and password cannot be empty.");
            ctx.render("createuser.html");
        }
        else {
            try {
                User newUser = UserMapper.createUser(username, password, connectionPool);
                ctx.attribute("message", "User created successfully.");
                ctx.redirect("/login");

            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("createuser.html");
            }
        }
    }

    private static void logout(@NotNull Context ctx)
    {
        //Invalidate session
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void login(Context ctx, ConnectionPool connectionPool) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try
        {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("user", user);
            //ctx.render("tasks.html");
            ctx.redirect("/tasks");
        }
        catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");

        }
    }

}
