package nl.fontys.se3.presentation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.dsl.OpenApiBuilder;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.javalin.websocket.WsMessageContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import io.swagger.v3.oas.models.info.Info;

import nl.fontys.se3.data.UserDAO;
import nl.fontys.se3.logic.*;
import nl.fontys.se3.presentation.models.*;
import org.eclipse.jetty.server.session.SessionHandler;

import javax.servlet.http.HttpSession;


public class Main {

    private static Game game = new Game();
    private static UserService userService = new UserService(new UserDAO());

    public static void main(String[] args) throws IOException {
        String gameHtml = getResourceFileAsString("/web/game.html");
        String indexHtml = getResourceFileAsString("/web/landing.html");
        String leaderboardHtml = getResourceFileAsString("/web/leaderboard.html");
        String loginHtml = getResourceFileAsString("/web/login.html");
        String registerHtml = getResourceFileAsString("/web/register.html");

        JavalinJackson.configure(JavalinJackson.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));


        //https://github.com/tipsy/javalin/blob/a27b600c7418e7ec766d126e1d13e9129312fc2a/src/main/java/io/javalin/websocket/JavalinWsServlet.kt
        //https://github.com/tipsy/javalin/blob/7165cca105860df938c402a875f1f855dba208be/src/main/java/io/javalin/Javalin.java

        //TODO: new idea make a class that inherrits from javalinwsservlet and manually move http session to websocket?


        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setHttpOnly(true);

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("web");
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
            config.sessionHandler(() -> sessionHandler);
            //maybe ws context can somehow access app attributes
            // config.inner.appAttributes.put(SessionHandler.class, sessionHandler);

        }).start(80);

        app.get("/", ctx -> {
            ctx.contentType("text/html");
            ctx.result(indexHtml);
        });


        app.get("/new", ctx -> {

            User user =  ctx.sessionAttribute("user");

            Room room = game.findRoom(user);

            if(room == null || !room.addPlayer(user.getUsername())) {
                room = game.createRoom(2, user.getScore(), RoomDifficulty.EASY);
            }
            room.addPlayer(user.getUsername());
            ctx.redirect("/game/" + room.getId());
            //TODO: automatically add player when creating room?

        });

        app.get("/game/:id", ctx -> {

            ctx.contentType("text/html");
            ctx.result(gameHtml);

        });

        app.get("/leaderboard", ctx -> {
            ctx.contentType("text/html");
            ctx.result(leaderboardHtml);
        });

        OpenApiDocumentation mazeDocumentation = OpenApiBuilder
                .document()
               .jsonArray("200", CellModel.class);

        app.get("generate-maze", OpenApiBuilder.documented(mazeDocumentation, ctx -> {
           Maze maze = new Maze(10, 10);
           maze.generateMaze();

           //TODO: MOVE START, EXIT AND BONUS SELECTION TO MAZE
           Arrays.stream(maze.getCells())
                   .max(Comparator.comparingInt(Cell::getSteps))
                   .get()
                   .setType(CellType.EXIT);

            Arrays.stream(maze.getCells())
                    .min(Comparator.comparingInt(Cell::getSteps))
                    .get()
                    .setType(CellType.START);

            var cells = Arrays.stream(maze.getCells())
                    .map(c -> new CellModel(c.getX(), c.getY(), c.getType().value, c.getWalls()))
                    .collect(Collectors.toList());
           ctx.json(cells);
        }));



        OpenApiDocumentation loginDocumentation = OpenApiBuilder
                .document()
                .body(LoginModel.class);

        app.post("/login", OpenApiBuilder.documented(loginDocumentation, ctx -> {

            LoginModel login = readForm(ctx, LoginModel.class);

            User user = userService.checkCredentials(login.getUsername(), login.getPassword());
            if(user != null) {
                ctx.sessionAttribute("user", user);
                ctx.redirect("/");
            }

        }));

        app.get("/login", ctx -> {
            ctx.contentType("text/html");
            ctx.result(loginHtml);
        });

        app.get("/logout", ctx -> {
            ctx.req.getSession().invalidate();
            ctx.redirect("/");
        });

        OpenApiDocumentation registerDocumentation = OpenApiBuilder
                .document()
                .body(RegisterModel.class);

        app.post("/register", OpenApiBuilder.documented(registerDocumentation, ctx -> {
            RegisterModel register = readForm(ctx, RegisterModel.class);

            userService.insertUser(new User(register.getUsername(), register.getPassword(), 0));

            ctx.redirect("/login");

        }));


        app.get("/register", ctx -> {
            ctx.contentType("text/html");
            ctx.result(registerHtml);
        });

        app.ws("/ws/:id", ws -> {
            ws.onConnect(ctx -> {
                //app.config.inner.sessionHandler.getHttpSession()
                //TODO: check if player is logged in

                String sessionId = ctx.cookie("JSESSIONID");
                HttpSession session = sessionHandler.getHttpSession(sessionId);
                User user = (User) session.getAttribute("user");

                ctx.attribute("user", user);

                Integer roomId = wrapException(() -> {
                    return ctx.pathParam("id", Integer.class).getOrNull();
                });

                if(roomId == null) {
                    ctx.send(new BasicMessage(MessageType.NO_ROOM));
                    ctx.session.close(4000, "invalid room id, could not parse int");
                    return;
                }

                Room room = game.getRoom(roomId);

                if(room == null) {
                    ctx.send(new BasicMessage(MessageType.NO_ROOM));
                    ctx.session.close(4000, "room not available");
                    return;
                }

                var playerClient = new WebSocketMazeRacerClient(ctx);


                if(!room.enter(user.getUsername(), playerClient)) {
                    ctx.send(new BasicMessage(MessageType.NO_ROOM));
                    ctx.session.close(4000, "player not in room");
                    return;
                }

            });

            ws.onClose(ctx -> {
                Integer roomId = wrapException(() -> {
                    return ctx.pathParam("id", Integer.class).getOrNull();
                });

                if(roomId == null) {
                    return;
                }

                Room room = game.getRoom(roomId);

                if(room == null) {
                    return;
                }

                User user = ctx.attribute("user");
                room.removePlayer(user.getUsername());

                if(room.getPlayers().isEmpty()) {
                    game.removeRoom(room);
                }
            });

            ws.onMessage(ctx -> {
                Integer roomId = wrapException(() -> {
                    return ctx.pathParam("id", Integer.class).getOrNull();
                });

                if(roomId == null) {
                    return;
                }

                Room room = game.getRoom(roomId);

                if(room == null) {
                    return;
                }
                processMessage(room, ctx);
            });
        });


    }

    private static OpenApiOptions getOpenApiOptions() {
        
        Info applicationInfo = new Info()
                .version("1.0")
                .description("<h3>maze racer API documentation<h3>");

        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger")
                        .title("maze racer API documentation"));
    }


    public static void processMessage(Room room, WsMessageContext messageContext) {
        var basic = messageContext.message(BasicMessage.class);
        User user = messageContext.attribute("user");

        switch (basic.getType()) {
            case MOVE:
                var moveMessage = messageContext.message(PlayerMoveMessage.class);
                var playerCoord = new Coord(moveMessage.getX(), moveMessage.getY());

                room.movePlayer(user.getUsername(), playerCoord);
                break;
            default:
                messageContext.send(new StringMessage("unknown or invalid message type", MessageType.UNKNOWN));
                break;
        }
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    static String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    private static <T> T readForm(Context ctx, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
       var fields = clazz.getDeclaredFields();

       T returnValue = clazz.getConstructor().newInstance();


        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                String capFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                var fieldType = field.getType();
                var formValue = ctx.formParam(fieldName, fieldType).getValue();

                var setter = clazz.getMethod("set" + capFieldName, fieldType);

                setter.invoke(returnValue, formValue);

            } catch (NoSuchMethodException e) {
                //field had no setter
            }
        }

        return  returnValue;
    }


    /**
     * wraps a method that can throw an exception
     *
     * @param  method method that will be wrapped
     * @return the result of the wrapped value or null if it failed
     */
    private static <T> T wrapException(Supplier<T> method) {
        try {
            return  method.get();
        }
        catch (Exception e) {
            return null;
        }
    }
}
