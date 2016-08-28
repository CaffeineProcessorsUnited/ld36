package de.caffeineaddicted.ld36.actors;

abstract public class UnitBase extends Entity {
    private float hp;

    abstract protected void onDie();

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void receiveDamage(float damage) {
        hp -= damage;
        if (hp < 0) {
            onDie();
        }
    }

    public boolean alive() {
        return hp >= 0;
    }
}
