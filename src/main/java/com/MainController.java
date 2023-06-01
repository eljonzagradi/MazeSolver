package com;

import algorithms.BacktrackingStrategy;
import algorithms.DijkstraStrategy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.GridView;
import models.Type;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public static Map<Type, Color> typeMap;
    public static Map<Integer, Color> weightMap;
    public static Map<Integer, Color> visitedMap;
    private final int[] WEIGHTS = {1};
    GridView model;
    @FXML
    private Button btnClear, btnExit, btnMazeGen, btnRun;
    @FXML
    private ComboBox<String> cmbMazeAlgorithm, cmbSolutionAlgorithm;
    @FXML
    private HBox hBoxBtnHolder;
    @FXML
    private Label lblGridInfo, lblInfo, lblPathFound, lblRootCord,
            lblTargetCord, lblTime, lblTotalTiles, lblTotalWalls;
    @FXML
    private BorderPane mainPane;
    @FXML
    private AnchorPane panelPane, girdHolder;
    @FXML
    private StackPane stackPane;

    public void hashSets() {

        // Type color Mapping
        typeMap = new HashMap<>();
        typeMap.put(Type.ROOT, Color.GREEN);
        typeMap.put(Type.TARGET, Color.DARKRED);
        typeMap.put(Type.EMPTY, Color.ORANGE);
        typeMap.put(Type.WALL, Color.BLACK);
        typeMap.put(Type.PATH, Color.DEEPPINK);
        typeMap.put(Type.HIGHLIGHT, Color.RED);
        typeMap.put(Type.VISITED, Color.LIGHTGREEN);

        // Visited Color based on weight
        visitedMap = new HashMap<>();
        visitedMap.put(this.WEIGHTS[0], Color.PALEGREEN);

        // Empty Weight color
        weightMap = new HashMap<>();
        weightMap.put(this.WEIGHTS[0], Color.GREY);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hashSets();
        cmbSolutionAlgorithm.getItems().addAll("Dijkstra");
        cmbSolutionAlgorithm.setValue("Dijkstra");
        cmbMazeAlgorithm.getItems().addAll("BackTracker");
        cmbMazeAlgorithm.setValue("BackTracker");
        //MUST BE ODD NUMBER OF ROWS AND COLUMNS
        model = new GridView(29, 39);
        stackPane.getChildren().add(model);
        lblGridInfo.setText("Grid Size: ( Rows: " + model.getRowCount() + ", Columns: " + model.getColumnCount() + " )");
        lblRootCord.setText("( " + model.getRoot().getRow() + " , " + model.getRoot().getColumn() + " )");
        lblTargetCord.setText("( " + model.getTarget().getRow() + " , " + model.getTarget().getColumn() + " )");

    }

    public void setStatisticsMazeGeneration() {
        lblTotalTiles.setText(String.valueOf(this.model.getColumnCount() * this.model.getRowCount()));

        int contWalls = 0;
        for (int row = 0; row < this.model.getRowCount(); row++) {
            for (int col = 0; col < this.model.getColumnCount(); col++) {
                if (this.model.getCells()[row][col].getType() == Type.WALL)
                    contWalls++;
            }
        }
        lblTotalWalls.setText(String.valueOf(contWalls));
    }

    public void setStatisticsPathFinding() {
        lblPathFound.setText(DijkstraStrategy.pathFound ? "YES" : "NO");
        lblTime.setText(DijkstraStrategy.time + " ms");
    }

    @FXML
    public void generateMaze() {
        BacktrackingStrategy mazeGenerationStrategy = new BacktrackingStrategy();
        this.model.generateRandomMaze(mazeGenerationStrategy);
        setStatisticsMazeGeneration();
    }

    @FXML
    public void run() throws InterruptedException {
        DijkstraStrategy.pathFound = false;
        DijkstraStrategy pathfindingStrategy = new DijkstraStrategy();
        this.model.executePathfinding(pathfindingStrategy);
        setStatisticsPathFinding();
    }

    @FXML
    public void clearGrid() {
        this.model.clearGrid();
        lblTotalTiles.setText("?");
        lblTotalWalls.setText("?");
        lblPathFound.setText("?");
        lblTime.setText(" ?");
    }

    @FXML
    public void exit() {
        System.exit(1);
    }
}
