package app.controllers;

import app.entitys.Checker;
import app.entitys.Field;
import app.views.BoardGrid;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameBoardController extends Application {
    /**
     * Иконка игры.
     */
    private final String ICON = "app/res/blackDam.png";


    private Stage stage;
    private ActionController actionController;
    private Field[][] fields;

    private int blackCount, whiteCount;

    public static void start(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image(ICON));
        stage.setHeight(535);
        stage.setWidth(510);
        stage.setResizable(false);
        stage.setTitle("Checkers");
        stage.show();
        this.stage = stage;
        startGame();
    }

    /**
     *  количество шашек определенного цвета и проверка победы.
     *  color - цвет убитой шишки.
     */
    public void delChecker(Checker.Color color) {
        if (color == Checker.Color.WHITE)
            whiteCount--;
        if (color == Checker.Color.BLACK)
            blackCount--;

        if (blackCount != 0 && whiteCount != 0) return;
        String win = "Выйграли ";
        if (whiteCount == 0) win += "Черные";
        if (blackCount == 0) win += "Белые";
        createFinal(win);
    }

    /**
     * Сздание диалогоого окна победы.
     *  mes - сообщение в окне.
     */
    private void createFinal(String mes) {
        Label secondLabel = new Label(mes);

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        Stage newWindow = new Stage();
        newWindow.setOnCloseRequest(windowEvent -> startGame());
        newWindow.setTitle("Конец игры.");
        newWindow.getIcons().add(new Image(ICON));
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);

        newWindow.setX(stage.getX() + 200);
        newWindow.setY(stage.getY() + 100);

        newWindow.show();
    }

    /**
     * Начало игры.
     */
    private void startGame() {
        BoardGrid grid = new BoardGrid(500, this);
        fields = grid.addBtmOnGrid();
        placeCheckers();

        blackCount = whiteCount = 12;
        actionController = new ActionController(fields, this);

        stage.setScene(new Scene(grid, 500, 500));
    }

    /**
     * Начальое расположение шашек.
     */
    public void placeCheckers() {
        for (int i = 0; i < 3; i++) {
            int m = i % 2 == 0 ? 1 : 0;
            for (int j = m; j < BoardGrid.COUNT_BOARD; j+=2) {
                fields[j][i].createChecker(Checker.Color.BLACK);
            }
        }
        for (int i = BoardGrid.COUNT_BOARD-3; i < BoardGrid.COUNT_BOARD; i++) {
            int m = i % 2 == 0 ? 1 : 0;
            for (int j = m; j < BoardGrid.COUNT_BOARD; j+=2) {
                fields[j][i].createChecker(Checker.Color.WHITE);
            }
        }

    }

    /**
     * Првоерка действия.
     *  field - нажатое поле.
     */
    public void checkerAction (Field field) {
        actionController.action(field);
        stage.setTitle("Ход " + actionController.turn());
    }

}
