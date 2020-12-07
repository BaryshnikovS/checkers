package app.entitys;

/**
 * Описывает игровое поле на игровой доске
 */
public class Point {

    /** Вертикальное направление. */
    public enum VDirection {
        UP,
        DOWN
    }

    /** Горизонтальное направление. */
    public enum HDirection {
        RIGHT,
        LEFT
    }

    /** Местоположение по Х . */
    private final int x;

    /** Местоположение по У . */
    private final int y;

    /** Направление для следущющего поиска поля относительно этого.*/
    private Direction direction;

    /**
     * Class constructor.
     *  x - Местоположение по Х.
     *  y - Местоположение по У.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  constructor.
     *  x         - Местоположение по Х.
     *  y         - Местоположение по У.
     *  direction - направление поиска.
     */
    public Point(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * Function to get value of field {@link Point#x}.
     * возврат returns X.
     */
    public int getX() {
        return x;
    }

    /**
     * Function to get value of field {@link Point#y}.
     * возврат returns Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Движение по нарвпвлению.
     *  p - старое место.
     * возврат новое место по направлению.
     */
    public static Point pointDirection(Point p) {
        return pointDirection(p, p.direction.v, p.direction.h);
    }

    /**
     * Движение по указанному нарвпвлению.
     * p - старое место.
     *  v - вертикальное направление.
     *  h - горизонтальное направление.
     * возврат новое место по направлению.
     */
    public static Point pointDirection (Point p, VDirection v, HDirection h) {
        if (v == VDirection.UP && h == HDirection.RIGHT){
            return new Point(p.getX() + 1, p.getY() - 1, new Direction(v, h));
        } else if (v == VDirection.UP && h == HDirection.LEFT){
            return new Point(p.getX() - 1, p.getY() - 1, new Direction(v, h));
        } else if (v == VDirection.DOWN && h == HDirection.RIGHT){
            return new Point(p.getX() + 1, p.getY() + 1, new Direction(v, h));
        } else if (v == VDirection.DOWN && h == HDirection.LEFT){
            return new Point(p.getX() - 1, p.getY() + 1, new Direction(v, h));
        } else {
            return p;
        }
    }

    /**
     * Место по направлению вверх и вправо.
     *  p - старое место.
     * возврат новое место.
     */
    public static Point pointUpRIGHT(Point p) {
        return new Point(p.getX() + 1, p.getY() - 1,
                new Direction(VDirection.UP, HDirection.RIGHT));
    }

    /**
     * Место по направлению вверх и влево.
     *  p - старое место.
     * возврат новое место.
     */
    public static Point pointUpLEFT(Point p) {
        return new Point(p.getX() - 1, p.getY() - 1,
                new Direction(VDirection.UP, HDirection.LEFT));
    }

    /**
     * Место по направлению вниз и вправо.
     *  p - старое место.
     * возврат новое место.
     */
    public static Point pointDownRIGHT(Point p) {
        return new Point(p.getX() + 1, p.getY() + 1,
                new Direction(VDirection.DOWN, HDirection.RIGHT));
    }

    /**
     * Место по направлению вниз и влево.
     *  p - старое место.
     * возврат новое место.
     */
    public static Point pointDownLEFT(Point p) {
        return new Point(p.getX() - 1, p.getY() + 1,
                new Direction(VDirection.DOWN, HDirection.LEFT));
    }


    @Override
    public String toString() {
        return "app.entitys.Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Класс направления
     */
    private static class Direction {

        /** вертикальное направление. */
        VDirection v;
        /** горизонтальное направление. */
        HDirection h;

        public Direction(VDirection v, HDirection h) {
            this.v = v;
            this.h = h;
        }
    }
}
