package app.views;

import app.controllers.GameBoardController;
import app.entitys.Field;
import app.entitys.Point;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Создает сетку игрового поля.
 */
public class BoardGrid extends GridPane {

    public static final int COUNT_BOARD = 8;

    public static int FIELD_SIZE; // размер сетки

    public BoardGrid(int height, GameBoardController controller) {
        super();
        FIELD_SIZE = height / COUNT_BOARD;
        Field.setController(controller);
        createBoard();
        setGridLinesVisible(true);
    }

    /**
     * Создание сетки.
     */
    private void createBoard() {
        for (int i = 0; i < COUNT_BOARD; i++) {
            getColumnConstraints().add(new ColumnConstraints(FIELD_SIZE));
            getRowConstraints().add(new RowConstraints(FIELD_SIZE));
        }
    }

    /**
     * Добавление игровых полей на поле.
     */
    public Field[][] addBtmOnGrid() {
        Field[][] buttons = new Field[COUNT_BOARD][COUNT_BOARD];
        for (int i = 0; i < COUNT_BOARD; i++) {
            int m = i % 2 == 0 ? 1 : 0;
            for (int j = 0; j < COUNT_BOARD; j++) {
                String color = "-fx-background-color: " + (m++ % 2 == 0? "gray" : "white");
                Field b = createGridField( i, j, color);
                buttons[i][j] = b;
                add(b, i, j);
            }
        }
        return buttons;
    }

    /**
     * Создание элемента поля.
     *  style - стиль с указанием цвета поля
     * возврат готовый элемент.
     */
    private Field createGridField (int i, int j, String style) {
        Field b = new Field(new Point(i, j), style);
        b.setPrefHeight(FIELD_SIZE);
        b.setPrefWidth(FIELD_SIZE);
        return b;
    }
}
