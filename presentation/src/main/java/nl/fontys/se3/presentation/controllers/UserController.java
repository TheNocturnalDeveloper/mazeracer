package nl.fontys.se3.presentation.controllers;

import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import nl.fontys.se3.logic.User;
import nl.fontys.se3.logic.UserService;
import nl.fontys.se3.presentation.models.LoginModel;
import nl.fontys.se3.presentation.models.RegisterModel;

import java.io.IOException;

import static nl.fontys.se3.presentation.Utils.getResourceFileAsString;
import static nl.fontys.se3.presentation.Utils.readForm;

public class UserController {

    private UserService service;

    private String loginPage;
    private String registerPage;

    public UserController(UserService service) throws IOException {
        this.service = service;

        loginPage = getResourceFileAsString(UserController.class, "/web/login.html");
        registerPage = getResourceFileAsString(UserController.class, "/web/register.html");
    }


    @OpenApi(
                path = "/users/login",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void login(Context ctx) {
        ctx.contentType("text/html");
        ctx.result(loginPage);
    }

    @OpenApi(
            path = "/users/register",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void register(Context ctx) {
        ctx.contentType("text/html");
        ctx.result(registerPage);
    }

    @OpenApi(
            path = "/users/logout",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void logout(Context ctx) {
        ctx.req.getSession().invalidate();
        ctx.redirect("/");
    }

    @OpenApi(
            path = "/users/login",
            method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = LoginModel.class)),
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void loginUser(Context ctx) {
        User user = null;

        try {
            LoginModel login =  readForm(ctx, LoginModel.class);
            user = service.checkCredentials(login.getUsername(), login.getPassword());
        } catch (Exception ignored) {
            //failed to read login form, user will be null
        }

        if(user != null) {
            ctx.sessionAttribute("user", user);
            ctx.redirect("/");
        }
    }

    @OpenApi(
            path = "/users/register",
            method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = RegisterModel.class)),
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void registerPlayer(Context ctx) {
        User user = null;



        try {
            var register = readForm(ctx, RegisterModel.class);

            if(register.getPassword().length() <= 8) {
                ctx.redirect("");
                return;
            }

            service.insertUser(new User(register.getUsername(), register.getPassword(), 0));
            user = service.checkCredentials(register.getUsername(), register.getPassword());
        } catch (Exception ignore) {
            //failed to read register form, user will be null
        }

        if(user != null) {
            ctx.sessionAttribute("user", user);
            ctx.redirect("/");
        }

    }


}
