package nl.fontys.se3.presentation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.javalin.Javalin;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import nl.fontys.se3.data.UserDAO;
import nl.fontys.se3.logic.Game;
import nl.fontys.se3.logic.RoomDifficulty;
import nl.fontys.se3.logic.UserService;
import nl.fontys.se3.presentation.controllers.IndexController;
import nl.fontys.se3.presentation.controllers.RoomController;
import nl.fontys.se3.presentation.controllers.UserController;
import nl.fontys.se3.presentation.controllers.WsController;

import java.io.IOException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static nl.fontys.se3.presentation.Utils.wrapException;


public class Main {

    private static OpenApiOptions getOpenApiOptions() {

        Info applicationInfo = new Info()
                .version("1.0")
                .description("<h3>maze racer API documentation<h3>");

        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .activateAnnotationScanningFor("nl.fontys.se3.presentation.controllers")
                .swagger(new SwaggerOptions("/swagger")
                        .title("maze racer API documentation"));
    }

    private static Javalin configureServer() {
        JavalinJackson.configure(JavalinJackson.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));

        JavalinValidation.register(RoomDifficulty.class, input -> wrapException(() -> RoomDifficulty.valueOf(input.toUpperCase())));

        return Javalin.create(config -> {
            config.addStaticFiles("presentation/src/main/resources/web", Location.EXTERNAL);
            config.addStaticFiles("web");
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        }).start(80);
    }

    private static void configureControllers(Javalin app) throws IOException {
        Game game = new Game();
        UserService userService = new UserService(new UserDAO());

        IndexController indexController = new IndexController();
        UserController userController = new UserController(userService);
        RoomController roomController = new RoomController(game);
        WsController wsController = new WsController(app.config.inner.sessionHandler, game, userService);

        app.routes(() -> {
           get("/", indexController::index);
           get("leaderboard", indexController::leaderboard);
           get("/generate-maze", indexController::generateMaze);
           get("/single-player", indexController::singlePlayer);

           path("users", () -> {
               path("login", () -> {
                   get(userController::login);
                   post(userController::loginUser);
               });
               path("register", () -> {
                   get(userController::register);
                   post(userController::registerPlayer);
               });
               get("logout", userController::logout);
           });
           path("rooms", () -> {
               get("join/:id", roomController::joinGame);
               path("match-making", () -> {
                  get(roomController::matchSettings);
                  post(roomController::findMatch);
               });
           });
           ws("ws/:id", ws -> {
               ws.onConnect(wsController::connect);
               ws.onMessage(wsController::message);
               ws.onClose(wsController::close);
           });
        });

    }

    public static void main(String[] args) throws IOException {
        configureControllers(configureServer());
    }
}
