package app.entitys;

/**
 * Описывает игровую шашку.

 */
public class Checker {

    /**
     * Типш шашки
     */
    public enum Type {
        CHECKER,
        QUEEN
    }

    /**
     * Цвет шашки, и путь к изображению
     */
    public enum Color {
        BLACK("black"),
        WHITE("white");

        private final String color;

        Color(String color) {
            this.color = color;
        }

        public String getPathColor(Type type) {
            String res = "app/res/" + color;
            res += type == Type.QUEEN ? "Dam.png" : ".png";
            return res;
        }
    }

    /**
     * Тип шашки.
     */
    private Type type;

    /**
     * Цвет шашки.
     */
    private final Color color;

    /**
     * КОнструктор
     *  type  - типш шашки
     *  color - цвет шашки
     */
    public Checker(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    /**
     * Устанвоить тип.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * возврат тип шашки.
     */
    public Type getType() {
        return type;
    }

    /**
     * возврат цвет шашки.
     */
    public Color getColor() {
        return color;
    }

    /**
     * возврат путь к изображеию шашки.
     */
    public String getPathColor() {
        return color.getPathColor(type);
    }
}
