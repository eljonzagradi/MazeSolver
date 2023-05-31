package models;

import algorithms.BacktrackingStrategy;
import algorithms.DijkstraStrategy;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import util.Painter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridView extends GridPane {

    private final GridCell[][] cells;
    private final int rowsNum, colNum;
    private final Painter painter;

    private GridCell root, target;

    public GridView(int rows, int columns) {
        painter = Painter.getInstance();
        this.setAlignment(Pos.CENTER);
        this.setHgap(0);
        this.setVgap(0);
        this.rowsNum = rows;
        this.colNum = columns;
        for (int c = 0; c < columns; c++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / columns);
            this.getColumnConstraints().add(colConst);
        }
        for (int r = 0; r < rows; r++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / rows);
            this.getRowConstraints().add(rowConst);
        }

        this.cells = new GridCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                GridCell tile = new GridCell(row, col);
                if (row == 0 && col == 0) {
                    tile.setAttributes(Type.ROOT, 1);
                    this.setRoot(tile);
                } else if (row == rows - 1 && col == columns - 1) {
                    tile.setAttributes(Type.TARGET, 1);
                    this.setTarget(tile);
                } else {
                    tile.setAttributes(Type.EMPTY, 1);
                }
                tile.setPrefSize(100, 100);
                this.add(tile, col, row);
                cells[row][col] = tile;
            }
        }
    }

    public List<GridCell> getTileNeighbors(GridCell tile) {
        List<GridCell> neighbors = new ArrayList<>();

        neighbors.add(this.getNorthTile(tile));
        neighbors.add(this.getSouthTile(tile));
        neighbors.add(this.getEastTile(tile));
        neighbors.add(this.getWestTile(tile));

        return neighbors;
    }

    public void generateRandomMaze(BacktrackingStrategy mazeGenerationStrategy) {
        mazeGenerationStrategy.generate(this);
    }

    public void clearGrid() {
        for (int row = 0; row < this.rowsNum; row++) {
            for (int col = 0; col < this.colNum; col++) {
                cells[row][col].clearTile();
            }
        }
    }

    public GridCell getNorthTile(GridCell tile) {
        return (tile.getRow() - 1 >= 0) ? cells[tile.getRow() - 1][tile.getColumn()] : null;
    }

    public GridCell getSouthTile(GridCell tile) {
        return ((tile.getRow() + 1) <= (this.getRowCount() - 1)) ? cells[tile.getRow() + 1][tile.getColumn()] : null;
    }

    public GridCell getWestTile(GridCell tile) {
        return (tile.getColumn() - 1 >= 0) ? cells[tile.getRow()][tile.getColumn() - 1] : null;
    }

    public GridCell getEastTile(GridCell tile) {
        return ((tile.getColumn() + 1) <= (this.getColumnCount() - 1)) ? cells[tile.getRow()][tile.getColumn() + 1] : null;
    }

    public GridCell[][] getCells() {
        return this.cells;
    }

    public List<GridCell> getCellList() {
        List<GridCell> tiles = new ArrayList<>();

        for (int row = 0; row < this.rowsNum; row++) {
            tiles.addAll(Arrays.asList(cells[row]).subList(0, this.colNum));
        }

        return tiles;
    }

    public void executePathfinding(DijkstraStrategy pathfindingStrategy) throws InterruptedException {
        if (root == null || target == null) return;
        this.painter.clearPath(this);

        List<GridCell> path = new ArrayList<>();

        pathfindingStrategy.algorithm(this, path);

    }

    public int getRowsTot() {
        return this.rowsNum;
    }

    public int getColsTot() {
        return this.colNum;
    }

    public GridCell getRoot() {
        return root;
    }

    public void setRoot(GridCell root) {
        this.root = root;
    }

    public GridCell getTarget() {
        return target;
    }

    public void setTarget(GridCell target) {
        this.target = target;
    }

}
