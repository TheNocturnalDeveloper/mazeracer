package nl.fontys.se3.presentation.controllers;

import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import nl.fontys.se3.logic.Maze;
import nl.fontys.se3.logic.RoomDifficulty;
import nl.fontys.se3.presentation.models.CellModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static nl.fontys.se3.presentation.Utils.getResourceFileAsString;


public class IndexController {
    private String indexPage;
    private String leaderboardPage;

    public IndexController() throws IOException {
        indexPage = getResourceFileAsString(IndexController.class,"/web/landing.html");
        leaderboardPage = getResourceFileAsString(IndexController.class,"/web/leaderboard.html");
    }

    @OpenApi(
            path = "/",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void index(Context ctx) {
        ctx.contentType("text/html");
        ctx.result(indexPage);
    }

    @OpenApi(
            path = "/leaderboard",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void leaderboard(Context ctx) {
        ctx.contentType("text/html");
        ctx.result(leaderboardPage);
    }

    @OpenApi(
            path = "/generate-maze",
            method = HttpMethod.GET,
            queryParams = {
                    @OpenApiParam(name = "difficulty", type = RoomDifficulty.class)
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            content = @OpenApiContent(from = CellModel.class, isArray = true)
                    )
            }
    )
    public void generateMaze(Context ctx) {
        RoomDifficulty difficulty = ctx.queryParam("difficulty", RoomDifficulty.class).getOrNull();

        if(difficulty == null) difficulty = RoomDifficulty.EASY;

        Maze maze = new Maze(difficulty.getMazeSize(), difficulty.getMazeSize());
           maze.generateMaze();

        var cells = Arrays.stream(maze.getCells())
                .map(c -> new CellModel(c.getX(), c.getY(), c.getType().value, c.getWalls()))
                .collect(Collectors.toList());
        ctx.json(cells);
    }

    @OpenApi(
            path = "/single-player",
            method = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public void singlePlayer(Context ctx) {
        //ctx.contentType("text/html");
        //ctx.result(SinglePlayerPage);
    }

}