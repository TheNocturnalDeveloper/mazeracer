package nl.fontys.se3.presentation.controllers;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import nl.fontys.se3.logic.Coord;
import nl.fontys.se3.logic.Game;
import nl.fontys.se3.logic.Room;
import nl.fontys.se3.logic.User;
import nl.fontys.se3.presentation.WebSocketMazeRacerClient;
import nl.fontys.se3.presentation.models.BasicMessage;
import nl.fontys.se3.presentation.models.MessageType;
import nl.fontys.se3.presentation.models.PlayerMoveMessage;
import nl.fontys.se3.presentation.models.StringMessage;
import org.eclipse.jetty.server.session.SessionHandler;

import static nl.fontys.se3.presentation.Utils.wrapException;

public class WsController {
    private SessionHandler handler;
    private Game game;

    public WsController(SessionHandler handler, Game game) {
        this.handler = handler;
        this.game = game;
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
        }

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
        room.removePlayer(user.getUsername());

        if(room.getPlayers().isEmpty()) {
            game.removeRoom(room);
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
