package models;

import com.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GridCell extends Label {
    private final int row, column;
    private Type type;
    private int weight;

    public GridCell(int row, int col) {
        this.row = row;
        this.column = col;
        this.weight = 1;
        this.setStyle("""
                -fx-border-width: 0.5;
                -fx-border-color: white;
                -fx-text-fill: white;\s""");
//        this.setText("(" + this.row + "," + this.column + ")");
        this.setFont(new Font("Arial", 8.5));
        this.setTextAlignment(TextAlignment.CENTER);
        this.setAlignment(Pos.CENTER);
    }

    public void setAttributes(Type type, int weight) {
        Color color = switch (type) {
            case VISITED -> MainController.visitedMap.get(this.getWeight());
            case EMPTY -> MainController.weightMap.get(weight);
            default -> MainController.typeMap.get(type);
        };

        this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        this.type = type;
        this.weight = weight;
    }

    public void clearTile() {
        this.setAttributes(Type.EMPTY, 1);
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public Type getType() {
        return this.type;
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean isWall() {
        return this.type == Type.WALL;
    }
}
