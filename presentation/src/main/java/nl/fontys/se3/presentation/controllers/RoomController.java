package nl.fontys.se3.presentation.controllers;

import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import nl.fontys.se3.logic.Game;
import nl.fontys.se3.logic.Room;
import nl.fontys.se3.logic.RoomDifficulty;
import nl.fontys.se3.logic.User;
import nl.fontys.se3.presentation.Main;

import java.io.IOException;

import static nl.fontys.se3.presentation.Utils.*;

public class RoomController {
    private Game game;
    private final String matchSettingsPage;
    private final String gamePage;

    public RoomController(Game game) throws IOException {
        this.game = game;
        matchSettingsPage = getResourceFileAsString(RoomController.class, "/web/matchmaking.html");
        gamePage = getResourceFileAsString(Main.class, "/web/game.html");
    }

    @OpenApi(
            path = "/rooms/match-making",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void matchSettings(Context ctx) {
        User user =  ctx.sessionAttribute("user");

        if(user == null) {
            ctx.redirect("/");
            return;
        }

        ctx.contentType("text/html");
        ctx.result(matchSettingsPage);
    }

    @OpenApi(
            path = "/rooms/match-making",
            method = HttpMethod.POST,
            responses = {
                    @OpenApiResponse(status = "303", description = "looks for a match and redirects the user.")
            },
            queryParams = {
                    @OpenApiParam(name = "difficulty", type = RoomDifficulty.class)
            }
    )
    public void findMatch(Context ctx) {
        User user =  ctx.sessionAttribute("user");

        if(user == null) {
            ctx.redirect("/");
            return;
        }

        RoomDifficulty difficulty = ctx.formParam("difficulty", RoomDifficulty.class).getOrNull();

        if(difficulty == null) difficulty = RoomDifficulty.EASY;

        Room room = game.findRoom(user, difficulty);

        if(room == null || !room.addPlayer(user.getUsername())) {
            room = game.createRoom(2, user.getScore(), difficulty);
        }
        room.addPlayer(user.getUsername());
        ctx.redirect("/rooms/join/" + room.getId());
    }

    @OpenApi(
            path = "/rooms/join/:id",
            method = HttpMethod.GET,
            pathParams = {
                    @OpenApiParam(name = "id", type = Integer.class, required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void joinGame(Context ctx) {

        User user =  ctx.sessionAttribute("user");

        if(user == null) {
            ctx.redirect("/");
            return;
        }

        ctx.contentType("text/html");
        ctx.result(gamePage);
    }
}
