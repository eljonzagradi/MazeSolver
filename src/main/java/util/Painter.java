/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import algorithms.DijkstraStrategy;
import models.GridCell;
import models.GridView;
import models.Type;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Painter {
    private static final Painter INSTANCE = new Painter();
    private final Executor executor;

    private Painter() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static Painter getInstance() {
        return INSTANCE;
    }

    public void drawPath(List<GridCell> path, GridView model) {
        this.executor.execute(
                () ->
                        path.stream().filter((GridCell) -> !(GridCell == model.getTarget() || GridCell == model.getRoot())).peek((GridCell) ->
                                GridCell.setAttributes(Type.PATH, GridCell.getWeight())).forEachOrdered((_item) ->
                        {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(GridView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }));
    }

    public void drawGridCell(GridCell cell, GridCell target, GridCell root, Type type, long sleep) {
        this.executor.execute(() ->
        {
            if (cell != target && cell != root)
                cell.setAttributes(type, cell.getWeight());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(DijkstraStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void highlightGridCell(GridCell cell, long waitTime) {
        this.drawGridCell(cell, null, null, Type.HIGHLIGHT, waitTime);
        this.drawGridCell(cell, null, null, Type.EMPTY, waitTime);
    }

    public void clearPath(GridView model) {
        this.executor.execute(() ->
        {
            GridCell cell;
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    cell = model.getCells()[row][col];
                    if (cell.getType() == Type.PATH || cell.getType() == Type.VISITED) {
                        cell.setAttributes(Type.EMPTY, cell.getWeight());
                    }
                }
            }
        });
    }
}
