'use strict';

const Wall = Object.freeze({
  "top":    0b1000, 
  "down":   0b0100, 
  "left":   0b0010,
  "right":  0b0001,
  "all":    0b1111,
  "none":   0b0000
});

const Direction = Object.freeze({
  "top":    0,
  "down":   1, 
  "left":   2,
  "right":  3
});

const CellType = Object.freeze({
  "empty":  0,
  "bonus":  1,
  "exit":   2,
  "start":  3,
});


class Coord {
    constructor(x,y) {
        this.x = x;
        this.y = y;
    }
}

class Size {
  constructor(width, height) {
    this.width = width;
    this.height = height;
  }
}

class Entity {

  constructor(coord) {
    this.coord = coord;
  }

  setCoord(coord) {
    this.coord = coord;
  }
}

class Cell extends Entity {
  constructor(coord) {
    super(coord);
    this.walls = Wall.all;
    this.type = CellType.empty;
  }

  removeWall(wall) {
    this.walls ^= wall;
  }

  addWall(wall) {
    this.walls |= wall;
  }

  hasWall(wall) {
    return (this.walls & wall) !== 0;
  }

  setWall(wall) {
    return this.walls = wall;
  }

  setCellType(type) {
    this.type = type;
  }
 
}

class Player extends Entity {
  constructor(coord) {
    super(coord);
  }
}

class Opponent extends Entity {
  constructor(coord, id) {
    super(coord);
    this.id = id;
  }
}


class Grid {

    constructor(width, height, player) {

        this.width = width;
        this.height = height;
        this.cells = [];
        this.player = player;
        this.oponents = [];


        for(let y = 0; y < this.height; y++) {
            for(let x = 0; x < this.width; x++) {
                const cell = new Cell(new Coord(x, y));
                this.cells.push(cell);
            }
        }

    }

    applyGridState(states) {
      for(let i = 0; i < states.length; i++) {
        const state  = states[i];

        this.cells[i].setWall(state.walls);
        this.cells[i].setCoord(new Coord(state.x, state.y));
        this.cells[i].setCellType(state.type);

        if(state.type === CellType.start) {
          this.player.coord.x = state.x;
          this.player.coord.y = state.y;
        }
      }
    }


    getOpponent(id) {
      return this.oponents.filter(o => o.id === id)[0];
    }



    addOpponent(opponent) {
      this.oponents.push(opponent)
    }


    getIndex(pos) {
        return pos.x + pos.y * this.width;
    }

    canMoveTo(dir) {
      //todo: bounds check breaks movement??
      const playerPos = this.player.coord;
      const playerCell = this.cells[this.getIndex(playerPos)];

      switch (dir) {
          case Direction.top:
              if(!playerCell.hasWall(Wall.top)) {
                  return true;
              }
              break;

          case Direction.down:
              if(!playerCell.hasWall(Wall.down)) {
                  return true;
              }
            break;

          case Direction.left:
              if(!playerCell.hasWall(Wall.left)) {
                  return true;
              }
              break;

          case Direction.right:
              if(!playerCell.hasWall(Wall.right)) {
                  return true;
              }
              break;
      }

      return false;
    }
}

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


async function loadMaze() {

  const maze = await (await fetch("/generate-maze")).json();

  grid.applyGridState(maze);

  document.addEventListener("keydown", event => {
    if (event.key === "ArrowLeft") {
      if(grid.canMoveTo(Direction.left)) {
        grid.player.coord.x -= 1;
      }
    }
    else if (event.key === "ArrowRight") {
      if(grid.canMoveTo(Direction.right)) {
        grid.player.coord.x += 1;
      }
    }
    else if(event.key === "ArrowDown") {
      if(grid.canMoveTo(Direction.down)) {
        grid.player.coord.y += 1;
      }
    }
    else if(event.key === "ArrowUp") {
      if(grid.canMoveTo(Direction.top)) {
        grid.player.coord.y -= 1;
      }
    }
  });

}


loadMaze()
    .then(() => console.log("maze loaded."))
    .catch(() => console.error("maze could not be loaded."));

requestAnimationFrame(render);
let socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws/" +  window.location.pathname.split("/").pop());
socket.onmessage = e => {
    console.log(e.data);
}