/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import models.GridCell;
import models.GridView;
import models.Type;
import util.Painter;

import java.util.*;

public class BacktrackingStrategy {
    protected Random random;
    protected Painter painter;
    protected long painterWait;

    public BacktrackingStrategy() {
        this.painter = Painter.getInstance();
        this.painterWait = 4; //4
    }

    public void generate(GridView model) {
        // template
        model.clearGrid();
        this.setDefaultWalls(model.getCells(), model.getRowsTot(), model.getColsTot());

        // maze gen algorithm
        this.algorithm(model);
    }

    public void algorithm(GridView model) {
        // Grid
        GridCell[][] grid = model.getCells();

        // This stack backtracks the maze
        Stack<GridCell> stack = new Stack<>();
        // Keeps track of current tile's neighbors
        List<GridCell> neighbors = new ArrayList<>();
        // Keeps track of visited Tiles
        Set<GridCell> visited = new HashSet<>();
        // Keeps track of current Tile
        GridCell currentTile = grid[0][0];

        stack.push(currentTile);
        visited.add(currentTile);

        while (!stack.isEmpty()) {
            this.addNeighbors(model, currentTile, neighbors);

            // Removes neighbors that have been visited
            neighbors.removeIf(visited::contains);

            // If there are no available neighbors, backtrack.
            if (neighbors.isEmpty()) {
                currentTile = stack.pop();
                this.painter.highlightGridCell(currentTile, painterWait);
                continue;
            }

            // Pick random neighbor from not visited neighbors
            GridCell randomNeighbor = neighbors.get(this.getRandomInt(neighbors.size()));
            this.painter.drawGridCell(randomNeighbor, null, null, Type.EMPTY, this.painterWait);

            // Remove walls in between
            this.removeWallBetween(grid, currentTile, randomNeighbor);

            // Set picked neighbor as current tile for next 
            currentTile = randomNeighbor;
            // Set tile as visited
            visited.add(randomNeighbor);
            // Push to stack
            stack.push(randomNeighbor);

            //  This is logic for visualization
            this.painter.highlightGridCell(randomNeighbor, this.painterWait);
        }
    }

    protected void addNeighbors(GridView model, GridCell tile, List<GridCell> neighbors) {
        // Clear neighbors to make sure we work with an empty list
        neighbors.clear();
        // Temporary holder for each neighbor
        GridCell temp;

        temp = model.getNorthTile(tile);
        if (temp != null)
            neighbors.add((temp.getRow() % 2 != 0) ? model.getCells()[temp.getRow() - 1][temp.getColumn()] : temp);

        temp = model.getSouthTile(tile);
        if (temp != null)
            neighbors.add((temp.getRow() % 2 != 0) ? model.getCells()[temp.getRow() + 1][temp.getColumn()] : temp);

        temp = model.getWestTile(tile);
        if (temp != null)
            neighbors.add((temp.getColumn() % 2 != 0) ? model.getCells()[temp.getRow()][temp.getColumn() - 1] : temp);

        temp = model.getEastTile(tile);
        if (temp != null)
            neighbors.add((temp.getColumn() % 2 != 0) ? model.getCells()[temp.getRow()][temp.getColumn() + 1] : temp);
    }

    protected void setDefaultWalls(GridCell[][] grid, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col].setAttributes(Type.WALL, 1);
            }
        }
    }

    protected void removeWallBetween(GridCell[][] grid, GridCell current, GridCell randomN) {
        int row = current.getRow();
        int col = current.getColumn();

        // Remove wall between currentTile and randomNeighbor
        if (current.getRow() < randomN.getRow()) row += 1; //randomN in S
        else if (current.getColumn() < randomN.getColumn()) col += 1;//randomN in E
        else if (current.getRow() > randomN.getRow()) row -= 1;// randomN in N
        else if (current.getColumn() > randomN.getColumn()) col -= 1;//randomN in W

        // This logic is for visualization
        this.painter.highlightGridCell(grid[row][col], this.painterWait);
    }

    protected int getRandomInt(int max) {
        this.random = new Random();
        return ((int) (Math.random() * (max)));
    }
}