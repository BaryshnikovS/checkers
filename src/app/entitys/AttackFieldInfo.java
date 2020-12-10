package app.entitys;

/**
 * Описывает местоположение врага и поле для его атаки.
 */
public class AttackFieldInfo {
    /**
     * Враг
     */
    private final Field enemy;

    /**
     * Поле для атаки.
     */
    private final Field attackField;

    /**
     * Class constructor .
     *
     *  enemy         - поле врага.
     *  attackField   - поле атаки.
     */
    public AttackFieldInfo(Field enemy, Field attackField) {
        this.enemy = enemy;
        this.attackField = attackField;
    }

    public Field getEnemy() {
        return enemy;
    }

    public Field getAttackField() {
        return attackField;
    }
}

