'use strict';

function drawLine(context, start, end, width, color) {
  context.beginPath();
  context.lineWidth = width;
  context.strokeStyle = color;
  context.moveTo(start.x, start.y);
  context.lineTo(end.x, end.y);
  context.stroke(); // Draw it
}

function fillCircle(context, start, radius, color) {

  const x = start.x + radius;
  const y = start.y  + radius;

  context.beginPath();
  context.arc(x, y, radius, 0, 2 * Math.PI, false);
  context.fillStyle = color;
  context.fill();
}

const playerColor = "blue";
const opponentColor = "purple";

const CellSize = new Size(50, 50);
const player = new Player(new Coord(0,0));
const grid = new Grid(10, 10, player);

const canvas  = document.querySelector("#canvas");
canvas.width  = grid.width * CellSize.width;
canvas.height = grid.height * CellSize.height;

const ctx = canvas.getContext("2d");



function render() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for(let i = 0; i < grid.cells.length; i++) {
    const cell    = grid.cells[i];
    const startX  = cell.coord.x * CellSize.width;
    const startY  = cell.coord.y * CellSize.height;
    const endX    = startX + CellSize.width;
    const endY    = startY + CellSize.height;

    switch (cell.type) {
      case CellType.start:
        ctx.fillStyle = "red"
        ctx.fillRect(startX, startY, CellSize.width, CellSize.height);
        break;

      case CellType.exit:
        ctx.fillStyle = "green"
        ctx.fillRect(startX, startY, CellSize.width, CellSize.height);
        break;
      case CellType.bonus:
        ctx.fillStyle = "yellow"
        ctx.fillRect(startX, startY, CellSize.width, CellSize.height);
        break;

      default:
      case CellType.empty:
        break;
    }

    if(cell.hasWall(Wall.top)) {
      drawLine(ctx, new Coord(startX, startY), new Coord(endX, startY), "2", "black");
    }

    if(cell.hasWall(Wall.down)) {
      drawLine(ctx, new Coord(startX, endY), new Coord(endX, endY), "2", "black");
    }

    if(cell.hasWall(Wall.left)) {
      drawLine(ctx, new Coord(startX, startY), new Coord(startX, endY), "2", "black");
    }

    if(cell.hasWall(Wall.right)) {
      drawLine(ctx, new Coord(endX, startY), new Coord(endX, endY), "2", "black");
    }

  }

  for(let i = 0; i < grid.oponents.length; i++) {
    const opponent = grid.oponents[i];
    const pos = new Coord(opponent.coord.x * CellSize.width, opponent.coord.y * CellSize.height);

    fillCircle(ctx, pos, 25, opponentColor);
  }

  const pos = new Coord(grid.player.coord.x * CellSize.width, grid.player.coord.y * CellSize.height);
  fillCircle(ctx, pos, 25, playerColor);

  requestAnimationFrame(render);
}



let socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws/" +  window.location.pathname.split("/").pop());

socket.onmessage = e => {
    const data = JSON.parse(e.data)
    if(data.type === "LOAD_MAZE") {
        grid.applyGridState(data.cells);
        grid.player.coord.x = grid.startPos.x;
        grid.player.coord.y = grid.startPos.y;
        requestAnimationFrame(render);
    }
    else if(data.type === "PLAYER_JOINED") {
        const enemyPos = new Coord(grid.startPos.x, grid.startPos.y);
        const enemy = new Opponent(enemyPos, data.message);

        grid.addOpponent(enemy);
    }
    else if(data.type === "REMOVE_BONUS") {
        grid.cells[grid.getIndex(new Coord(data.x, data.y))].setCellType(CellType.empty);
    }
    else if(data.type === "NO_ROOM") {
        alert("something went wrong, please return to matchmaking");
        window.location = "/";
    }
    else if(data.type === "PLAYER_WON") {
        alert(`the player ${data.message} has won`);
        window.location = "/";
    }
    else if(data.type === "RESET_POSITION") {
        grid.player.coord.x = data.x;
        grid.player.coord.y = data.y;
    }
    else if(data.type === "PLAYER_LEFT") {
        grid.removeOpponent(data.message);
    }
    else if(data.type === "MOVE") {
        const opponent = grid.getOpponent(data.username);
        opponent.coord.x = data.x;
        opponent.coord.y = data.y;
    }

};



document.addEventListener("keydown", event => {
    let moved = false;
    if (event.key === "ArrowLeft") {
        if(grid.canMoveTo(Direction.left)) {
            grid.player.coord.x -= 1;
            moved = true;
        }
    }
    else if (event.key === "ArrowRight") {
        if(grid.canMoveTo(Direction.right)) {
            grid.player.coord.x += 1;
            moved = true;
        }
    }
    else if(event.key === "ArrowDown") {
        if(grid.canMoveTo(Direction.down)) {
            grid.player.coord.y += 1
            moved = true;
        }
    }
    else if(event.key === "ArrowUp") {
        if(grid.canMoveTo(Direction.top)) {
            grid.player.coord.y -= 1;
            moved = true;
        }
    }

    if(moved) {
        const cell = grid.cells[grid.getIndex(grid.player.coord)];
        if(cell.type === CellType.bonus) {
            cell.setCellType(CellType.empty);
        }

        socket.send(JSON.stringify({
            "type" : "MOVE",
            "x": grid.player.coord.x,
            "y": grid.player.coord.y,
        }))
    }
});