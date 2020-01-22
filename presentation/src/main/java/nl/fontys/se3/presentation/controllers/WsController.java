package nl.fontys.se3.presentation.controllers;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import nl.fontys.se3.logic.*;
import nl.fontys.se3.presentation.WebSocketMazeRacerClient;
import nl.fontys.se3.presentation.models.BasicMessage;
import nl.fontys.se3.presentation.models.MessageType;
import nl.fontys.se3.presentation.models.PlayerMoveMessage;
import nl.fontys.se3.presentation.models.StringMessage;
import org.eclipse.jetty.server.session.SessionHandler;

import java.util.List;

import static nl.fontys.se3.presentation.Utils.wrapException;

public class WsController {
    private SessionHandler handler;
    private Game game;
    private UserService userService;

    public WsController(SessionHandler handler, Game game, UserService userService) {
        this.handler = handler;
        this.game = game;
        this.userService = userService;
    }

    public void connect(WsConnectContext ctx) {
        User user = getSessionUser(ctx);

        if(user == null) {
            ctx.send(new BasicMessage(MessageType.NO_ROOM));
            ctx.session.close(4000, "not authenticated");
            return;
        }

        ctx.attribute("user", user);

        Integer roomId = wrapException(() -> ctx.pathParam("id", Integer.class).getOrNull());

        if(roomId == null) {
            ctx.send(new BasicMessage(MessageType.NO_ROOM));
            ctx.session.close(4001, "invalid room id, could not parse int");
            return;
        }

        Room room = game.getRoom(roomId);

        if(room == null) {
            ctx.send(new BasicMessage(MessageType.NO_ROOM));
            ctx.session.close(4002, "room not available");
            return;
        }

        var playerClient = new WebSocketMazeRacerClient(ctx);


        if(!room.enter(user.getUsername(), playerClient)) {
            ctx.send(new BasicMessage(MessageType.NO_ROOM));
            ctx.session.close(4003, "player not in room");
            return;
        }

        if(room.isFull()) room.start(); //start in new thread with delay
    }

    public void message(WsMessageContext ctx) {
        Integer roomId = wrapException(() -> ctx.pathParam("id", Integer.class).getOrNull());

        if(roomId == null) {
            return;
        }

        Room room = game.getRoom(roomId);

        if(room == null) {
            return;
        }

        processMessage(room, ctx);

        if(room.getStatus() == GameStatus.OVER) {
            saveMatchResults(room);
        }
    }

    public void close(WsCloseContext ctx) {
        Integer roomId = wrapException(() -> ctx.pathParam("id", Integer.class).getOrNull());

        if(roomId == null) {
            return;
        }

        Room room = game.getRoom(roomId);

        if(room == null) {
            return;
        }

        User user = ctx.attribute("user");

        if (user == null) {
            return;
        }

       if(room.getStatus() == GameStatus.STARTED) {
           var dbUser = userService.getUserByName(user.getUsername());

           int score = dbUser.getScore() - 5 <= 0 ? -dbUser.getScore() : -5;
           userService.updateScore(dbUser, score);
       }


        room.removePlayer(user.getUsername());

        if(room.getPlayers().isEmpty()) {
            game.removeRoom(room);
        }
    }


    private void saveMatchResults(Room room) {
        List<String> results = room.getMatchResult();
        for(int i = 0; i < results.size(); i++) {
            User user = userService.getUserByName(results.get(i));
            var dbUser = userService.getUserByName(user.getUsername());

            if(i == 0) {
                //winner
                userService.updateScore(dbUser, 10);

            }
            else {
                //loser
                int score = dbUser.getScore() - 5 <= 0 ? -dbUser.getScore() : -5;
                userService.updateScore(dbUser, score);

            }
        }
    }

    private  User getSessionUser(WsContext ctx) {
        try {
            var sessionId = ctx.cookie("JSESSIONID");
            var session = handler.getHttpSession(sessionId);
            return (User) session.getAttribute("user");
        }
        catch (Exception e) {
            return null;
        }

    }

    private static void processMessage(Room room, WsMessageContext messageContext) {
        var basic = messageContext.message(BasicMessage.class);
        User user = messageContext.attribute("user");

        assert user != null;

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
}
