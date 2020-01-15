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