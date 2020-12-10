package app.controllers;

import app.entitys.AttackFieldInfo;
import app.entitys.Checker;
import app.entitys.Field;
import app.entitys.Point;
import app.views.BoardGrid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Контроллирует действия на полях.
 *
 */
public class ActionController {
    private final Field[][] fields;

    /** Раунд, определяет чей ход. */
    private int round;

    /**
     * Активно какое-либо полеили нет.
     * Активно - пользователь нажал и появились варианты перестановки шашки.
     */
    private boolean isAction;

    /**
     * Активное поле.
     */
    private Field activeField;

    /**
     * Активированные поля для перемещения.
     */
    private final List<Field> activeFields;

    /**
     * Активированные поля для атаки.
     */
    private final LinkedList<AttackFieldInfo> attackFields;

    /**
     * Игровой контроллер.
     */
    private final GameBoardController controller;

    /**
     * Class constructor.
     * @param fields        - игровые поля.
     * @param controller    - контроллер игры.
     */
    public ActionController(Field[][] fields, GameBoardController controller) {
        this.fields = fields;
        this.controller = controller;

        isAction = false;
        activeFields = new ArrayList<>();
        attackFields = new LinkedList<>();
        round = 1;
    }

    /**
     * Обработка действий
     * @param field - активное поле
     */
    public void action (Field field) {
        /*
            Завершить действие если: поле пустое и действие не активно
         */
        if (field.isEmpty() && !isAction) return;

        /*
        Завершить действие если: действие перемещения направленно на шашки противника.
         */
        if (field.getChecker() != null && field.getChecker().getColor() != turn()) {
            return;
        }

        /*
          Если: действие активно, нажатое поле != автивному полю и на поле есть шашка
          то сделайть это поле активное и показать варианты движение для этой шашки
         */
        if (isAction && field != activeField && field.getChecker() != null) {
            if (field.getChecker().getColor() == turn()) {
                boolean checkFucka = isFUKA(field);
                setActive(field, true);
                clearAllActiveField();
                showPossibleField(field);

                if (checkFucka && attackFields.size() <= 0) {
                    setActive(null, false);
                }
            }
            return;
        }

        /*
            Если: двоеное нажатие на то же поле, то убрать варианты перестановок
         */
        if (field == activeField) {
            setActive(null, false);
            return;
        }

        /*
        Если нажатое поле является одним для перемещения, то переместить шаку на него
         */
        for (Field f : activeFields) {
            if (f == field) {
                goChecker(f, false);
                setActive(null, false);
                return;
            }
        }

        /*
        Если нажатое поле является атакой , то убрать убитую шашку и переместить
         */
        for (AttackFieldInfo f : attackFields) {
            if (f.getAttackField() == field){
                checkAttack(field);
                goChecker(field, true);
                return;
            }
        }

        /*
        Если действий небыло , сделать нажатое поле активным
         */
        if (!isAction) {
            boolean checkFucka = isFUKA(field);
            setActive(field, true);
            showPossibleField(field);
            if (checkFucka && attackFields.size() <= 0) {
                setActive(null, false);
            }
        }
    }

    private boolean isFUKA(Field field) {
        int count = 0;
        for (Field[] value : fields) {
            for (Field item : value) {
                if (item.getChecker() == null || field.getChecker() == null)
                    continue;
                if (field.getChecker().getColor() == item.getChecker().getColor()) {

                    showPossibleField(item);
                    if (attackFields.size() > 0) {
                        clearAllActiveField();
                        return true;
                    }
                }

                clearAllActiveField();
            }
        }
        return false;
    }

    /**
     * Выдача дамки для шашки.
     */
    private void getDamCheck(Field field) {
        Checker checker = field.getChecker();
        if (checker.getType() == Checker.Type.QUEEN) return;
        switch (field.getChecker().getColor()){
            case BLACK : {
                if (field.getY() == BoardGrid.COUNT_BOARD - 1) {
                    checker.setType(Checker.Type.QUEEN);
                }
            }
            case WHITE : {
                if (field.getY() == 0) {
                    checker.setType(Checker.Type.QUEEN);
                }
            }
        }
        field.setCheckerInfo();
    }


    /**
     * Проверка на атаку.

     */
    private void checkAttack(Field field) {
        for (AttackFieldInfo info :
                attackFields) {
            if (info.getAttackField() == field) {

                controller.delChecker(info.getEnemy().getChecker().getColor());
                info.getEnemy().delChecker();
                info.getAttackField().setActive(Field.ActiveState.EMPTY);
                clearAllActiveField();
                return;
            }
        }
    }

    /**
     * Перемещение шашки.
     field     - нажатое поле.
     isAttack  - это действие атака или нет.
     */
    private void goChecker(Field field, boolean isAttack) {
        field.setChecker(activeField.getChecker());
        activeField.delChecker();
        showPossibleField(field);
        if (findEnemyAround(field) && isAttack && attackFields.size() != 0) {
            setActive(field, true);
        }else {
            setActive(null, false);
            round += 1;
        }
        getDamCheck(field);
    }

    /**
     * Определяем какой цвет ходит.
     * @return цвет;
     */
    public Checker.Color turn(){
        return round % 2 == 0 ? Checker.Color.BLACK : Checker.Color.WHITE;
    }

    /**
     * Показать варианты перестановки.
     * @param field - нажатое поле.
     */
    private void showPossibleField(Field field) {
        Field frontRight= null;
        Field frontLeft= null;
        Field backRight= null;
        Field backLeft = null;
        switch (field.getChecker().getColor()) {
            case BLACK : {
                frontRight = getFieldByPoint(Point.pointDownRIGHT(field.getPoint()));
                frontLeft = getFieldByPoint(Point.pointDownLEFT(field.getPoint()));
                backRight = getFieldByPoint(Point.pointUpRIGHT(field.getPoint()));
                backLeft = getFieldByPoint(Point.pointUpLEFT(field.getPoint()));
                break;
            }
            case WHITE : {
                frontRight = getFieldByPoint(Point.pointUpRIGHT(field.getPoint()));
                frontLeft = getFieldByPoint(Point.pointUpLEFT(field.getPoint()));
                backRight = getFieldByPoint(Point.pointDownRIGHT(field.getPoint()));
                backLeft = getFieldByPoint(Point.pointDownLEFT(field.getPoint()));
                break;
            }
        }

        if (field.getChecker().getType() == Checker.Type.CHECKER) {
            checkPossibleFieldForChecker(frontRight, false);
            checkPossibleFieldForChecker(frontLeft, false);
            checkPossibleFieldForChecker(backRight, true);
            checkPossibleFieldForChecker(backLeft, true);
        } else {
            Checker.Color color = field.getChecker().getColor();
            checkPossibleFieldsForQueen(frontRight, color);
            checkPossibleFieldsForQueen(frontLeft, color);
            checkPossibleFieldsForQueen(backRight, color);
            checkPossibleFieldsForQueen(backLeft, color);
        }
        if (attackFields.size() >= 1) clearActiveField();

    }

    /**
     * Проверка возможных полей хода для дамки.
     field - нажатое поле.
     */
    private void checkPossibleFieldsForQueen(Field field, Checker.Color color){
        if (field == null) return;

        if (field.getChecker() != null && field.getChecker().getColor() == color){
            return;
        }

        //нашли врага, то ищем пути перемещения
        if (field.getChecker() != null && field.getChecker().getColor() != color) {
            findFieldsByAttack(field, field, color);
            return;
        }
        //Если пустое поле, то можно переместить
        if (field.isEmpty()) {
            activeFields.add(field);
            field.setActive(Field.ActiveState.POSSIBLE);
        }

        //если это поле существет попробуем найти по диагонали и направлению похожее
        Field newF = getFieldByPoint(Point.pointDirection(field.getPoint()));
        checkPossibleFieldsForQueen(newF, color);
    }


    /**
     * Проверка возможного поля хода для шашки.
     field - нажатое поле.
     */
    private void checkPossibleFieldForChecker(Field field, boolean back){
        if(field == null) return;
        //Если пустое поле, не ход назад, то можно переместить
        if (field.isEmpty() && !back) {
            activeFields.add(field);
            field.setActive(Field.ActiveState.POSSIBLE);
        }

        //если нашелся враг
        if (!field.isEmpty() && field.getChecker().getColor() != turn()) {
            Field f = findAttackFieldByChecker(Point.pointDirection(field.getPoint()));
            if (f != null) {
                f.setActive(Field.ActiveState.ATTACK);
                attackFields.add(new AttackFieldInfo(field, f));
            }
        }
    }

    /**
     * Поиск полей для атаки дамки.
     field - нажатое поле.
     */
    private void findFieldsByAttack(final Field enemy, Field field, Checker.Color color) {
        if (field == null) return;

        if (field.getChecker() != null && field.getChecker().getColor() == color) {
            field.setActive(Field.ActiveState.EMPTY);
            attackFields.removeLast();
            return;
        }


        Field newF = getFieldByPoint(Point.pointDirection(field.getPoint()));
        if (newF == null || !newF.isEmpty()) return;
        newF.setActive(Field.ActiveState.ATTACK);
        attackFields.add(new AttackFieldInfo(enemy, newF));
        findFieldsByAttack(enemy, newF, color);

    }

    /**
     * Поиск поля атаки дял шашки.
     p - поинт с вектором поиска.
     * @return поле аттаки, если нету то null.
     */
    private Field findAttackFieldByChecker(Point p) {
        return findAttackField(p, 0);
    }

    /**
     * Рекурсия поиска
     p - поинт с вектором поиска.
     count - отсчет.
     поле аттаки, если нету то null.
     */
    private Field findAttackField(Point p, int count){
        if (count >= 1) return null;

        if (p.getX() < 0 || p.getX() >= BoardGrid.COUNT_BOARD)
            return null;

        if (p.getY() < 0 || p.getY() >= BoardGrid.COUNT_BOARD)
            return null;

        Field field = fields[p.getX()][p.getY()];
        if (field != null && field.isEmpty()) {
            return field;
        } else {
            return findAttackField(Point.pointDirection(p), ++count);
        }
    }

    /**
     * Установить/сбросить активное поле.
     f - поле.
     b - активность. (да, нет)
     */
    private void setActive(Field f, boolean b) {
        this.activeField = f;
        isAction = b;
        clearAllActiveField();
    }

    /**
     * Очистить возможные и атакующе поля.
     */
    private void clearAllActiveField() {
        clearActiveField();
        clearAttackField();
    }

    /**
     * Очистить возможные поля.
     */
    private void clearActiveField() {
        for (Field field : activeFields) {
            field.setActive(Field.ActiveState.EMPTY);
        }
        activeFields.clear();
    }

    /**
     * Очистить атакующе поля.
     */
    private void clearAttackField() {
        for (AttackFieldInfo field : attackFields) {
            field.getEnemy().setActive(Field.ActiveState.EMPTY);
            field.getAttackField().setActive(Field.ActiveState.EMPTY);
        }
        attackFields.clear();
    }


    /**
     * ПОиск противников вокруг поля
     * field - поле
     * @return результат если найдет хотя бы один враг
     */
    private boolean findEnemyAround(Field field) {
        Field c1 = getFieldByPoint(Point.pointDirection(field.getPoint(),
                Point.VDirection.UP, Point.HDirection.RIGHT));
        Field c2 = getFieldByPoint(Point.pointDirection(field.getPoint(),
                Point.VDirection.UP, Point.HDirection.LEFT));
        Field c3 = getFieldByPoint(Point.pointDirection(field.getPoint(),
                Point.VDirection.DOWN, Point.HDirection.RIGHT));
        Field c4 = getFieldByPoint(Point.pointDirection(field.getPoint(),
                Point.VDirection.DOWN, Point.HDirection.LEFT));

        return isEnemy(field, c1)
                || isEnemy(field, c2)
                || isEnemy(field, c3)
                || isEnemy(field, c4) ;
    }

    /**
     * Сверка полей, являются ли шашки врагами
     *  f1 - первое поле
     * f2 - второе поле
     * @return враги или нет
     */
    private boolean isEnemy(Field f1, Field f2){
        if (f1 == null || f2 == null) return false;
        if (f1.isEmpty() || f2.isEmpty()) return false;
        if (f1.getChecker() == null || f2.getChecker() == null) return false;
        return f1.getChecker().getColor() != f2.getChecker().getColor();
    }

    /**
     * Получить поле по поинту
     *  p - координаты
     * @return поле.
     */
    private Field getFieldByPoint(Point p) {
        int x = p.getX(), y = p.getY();
        if (x >= BoardGrid.COUNT_BOARD || x < 0) return null;
        if (y >= BoardGrid.COUNT_BOARD || y < 0) return null;
        fields[x][y].setPoint(p);
        return fields[x][y];
    }

}