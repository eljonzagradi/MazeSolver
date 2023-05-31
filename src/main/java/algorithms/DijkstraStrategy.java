package algorithms;

import models.GridCell;
import models.GridView;
import models.Type;
import util.Painter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DijkstraStrategy {
    public static boolean pathFound = false;
    public static long time = 0;
    protected Painter painter;

    //This constructor is used call the function to visualise Dijkstra's algorithm process when it is executed
    public DijkstraStrategy() {
        this.painter = Painter.getInstance();
    }

    // This function is used in the GridView class
    public final void algorithm(GridView model, List<GridCell> path) {
        time = 0;
        // Runs pathfinding algorithm
        long startTime = System.currentTimeMillis();
        this.runPathfinder(model, path);
        long endTime = System.currentTimeMillis();

        // Calculate the elapsed time
        time = endTime - startTime;

        // visualises the path
        this.painter.drawPath(path, model);

    }

    // main function to find the path
    public void runPathfinder(GridView grid, List<GridCell> path) {
        GridCell root = grid.getRoot();
        GridCell target = grid.getTarget();

        HashMap<GridCell, GridCell> parents = new HashMap<>();
        HashMap<GridCell, Integer> weights = new HashMap<>();
        path.clear();

        executeDijkstra(grid, parents, weights);

        int cost = weights.get(target);

        GridCell tile = target;

        // If cost is infinite, means it didn't find the target
        if (cost != Integer.MAX_VALUE) {
            do {
                path.add(0, tile);
                tile = parents.get(tile);
            } while (tile != root);

            pathFound = true;

        }


    }

    //helper function in runPathfinder
    private void executeDijkstra(GridView grid,
                                 HashMap<GridCell, GridCell> parents,
                                 HashMap<GridCell, Integer> weights) {
        GridCell root = grid.getRoot();
        GridCell target = grid.getTarget();

        // Init all tiles
        Set<GridCell> unvisited = new HashSet<>();

        List<GridCell> cellList = grid.getCellList();
        for (GridCell tile : cellList) {
            if (!tile.isWall()) {
                unvisited.add(tile);
                weights.put(tile, Integer.MAX_VALUE);
                parents.put(tile, null);
            }
        }
        weights.put(root, 0);
        // Compute weights
        while (!unvisited.isEmpty()) {
            GridCell lowCostTile = getMinWeight(unvisited, weights);

            // If we ever get a lower cost that equals infinity, it means we're stuck
            if (weights.get(lowCostTile) == Integer.MAX_VALUE)
                break;

            // Paints current tile and updates statistics for visited tiles
            painter.drawGridCell(lowCostTile, target, root, Type.HIGHLIGHT, 1);

            // Remove current tile from unvisited set
            unvisited.remove(lowCostTile);

            // Get neighbors
            List<GridCell> neighbors = grid.getTileNeighbors(lowCostTile);

            for (GridCell tile : neighbors) {
                if (unvisited.contains(tile)) {
                    int weight = tile.getWeight() + weights.get(lowCostTile);
                    if (weights.get(tile) > weight) {
                        weights.put(tile, weight);
                        parents.put(tile, lowCostTile);
                    }
                }
            }
            this.painter.drawGridCell(lowCostTile, grid.getTarget(), root, Type.VISITED, 1);
        }
    }

    //helper class in executeDijkstra
    private GridCell getMinWeight(Set<GridCell> unvisited, HashMap<GridCell, Integer> weights) {
        //checks the unvisited tiles and find the best option with the minimum weight
        double minWeight = Integer.MAX_VALUE;
        GridCell minWeightTile = null;

        for (GridCell tile : unvisited) {
            if (weights.get(tile) <= minWeight) {
                minWeightTile = tile;
                minWeight = weights.get(tile);
            }
        }

        return minWeightTile;
    }
}