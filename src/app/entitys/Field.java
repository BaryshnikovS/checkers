package app.entitys;

import app.controllers.GameBoardController;
import app.views.BoardGrid;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**

 * Описывает игровое поле на игровой доске.
 */
public class Field extends Button {

    /**
     * Активное состояние.
     */
    public enum ActiveState {
        /** Свободное поле. */
        EMPTY,
        /** Поле возможное для хода. */
        POSSIBLE,
        /** Поле  возможное для атаки. */
        ATTACK
    }

    /** Контроллер игры. */
    private static GameBoardController controller;

    /** Координаты поля .*/
    private Point point;

    /** Шашка на поле.*/
    private Checker checker;

    /** Расцветка поля.*/
    private final String style;

    /**
     *
     *  point - координаты поля.
     *  style - цвет клетки.
     */
    public Field(final Point point,
                 final String style) {
        this.point = point;
        this.style = style;
        this.addEventHandler(
                ActionEvent.ACTION,
                actionEvent -> controller.checkerAction(Field.this)
        );

        setStyle(style);
    }

    /**
     * Установка контроллера.
     *  controller - контроллер.
     */
    public static void setController(final GameBoardController controller) {
        Field.controller = controller;
    }

    /**
     * Установить шашку на поле.
     */
    public void setChecker(final Checker checker) {
        this.checker = checker;
        setCheckerInfo();
    }

    /**
     * Удалить шашку с поля.
     */
    public void delChecker() {
        this.setGraphic(null);
        checker = null;
    }

    /**
     * Получить обьект шашки.
     */
    public Checker getChecker() {
        return checker;
    }

    /**
     * Установить поинт
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Получить поинт.
     */
    public Point getPoint() {
        return point;
    }


    /**
     * Получить У из поинта.
     */
    public int getY() {
        return point.getY();
    }


    /**
     * Создать новую шашку на поле.
     */
    public void createChecker(Checker.Color color) {
        checker = new Checker(Checker.Type.CHECKER, color);
        setCheckerInfo();
    }

    /**
     * показашь шашку на поле.
     */
    public void setCheckerInfo() {
        this.setGraphic(createImageView());
    }

    /**
     * @return пусте поле или нет.
     */
    public boolean isEmpty() {
        return checker == null;
    }

    /**
     * Актиновсть для поля.
     *  active - активность: свободная, возможная или атакующая.
     */
    public void setActive(ActiveState active) {
        String s = style;
        switch (active) {
            case ATTACK :
                s = "-fx-background-color: red";
                break;
            case POSSIBLE :
                s = "-fx-background-color: green";
                break;
        }
        setStyle(s);
    }


    /**
     * Создание изображения шашки.
     * возврат готовый элемент изображения.
     */
    private ImageView createImageView() {
        ImageView view = new ImageView(checker.getPathColor());
        view.setFitHeight(BoardGrid.FIELD_SIZE - 5);
        view.setFitWidth(BoardGrid.FIELD_SIZE - 5);
        view.setTranslateX(-5);
        return view;
    }
}
