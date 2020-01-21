


const playerColor = "blue";

const CellSize = new Size(50, 50);
const player = new Player(new Coord(0,0));
const grid = new Grid(10, 10, player);

const canvas  = document.querySelector("#canvas");
canvas.width  = grid.width * CellSize.width;
canvas.height = grid.height * CellSize.height;

const ctx = canvas.getContext("2d");




async function loadMaze() {

  const maze = await (await fetch("/generate-maze")).json();
  grid.applyGridState(maze);
  player.coord.x = grid.startPos.x;
  player.coord.y = grid.startPos.y;

}


loadMaze()
    .then(() =>  {
        requestAnimationFrame(render);
        console.log("maze loaded.");
    })
    .catch(() => console.error("maze could not be loaded."));


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
            grid.player.coord.y += 1;
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
        let cell = grid.cells[grid.getIndex(player.coord)];

        if (cell.type === CellType.exit) {
            loadMaze();
        }
        else if (cell.type === CellType.bonus) {
            cell.type = CellType.empty;
        }
    }
});

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


    const pos = new Coord(grid.player.coord.x * CellSize.width, grid.player.coord.y * CellSize.height);
    fillCircle(ctx, pos, 25, playerColor);

    requestAnimationFrame(render);
}


