package de.caffeineaddicted.ld36.weapons;

/**
 * Created by felix on 27.08.16.
 */

public class Damage {
    private float hp_damage;
    private float knockback;
    private float sleep;

    public Damage(){
        this.hp_damage = 0;
        this.knockback = 0;
        this.sleep = 0;
    }

    public Damage(Damage damage){
        this.hp_damage = damage.getHp_damage();
        this.knockback = damage.getKnockback();
        this.sleep = damage.getSleep();
    }

    public Damage(float hp_damage, float knockback, float sleep) {
        this.hp_damage = hp_damage;
        this.knockback = knockback;
        this.sleep = sleep;
    }

    public float getHp_damage() {
        return hp_damage;
    }

    public void setHp_damage(float hp_damage) {
        this.hp_damage = hp_damage;
    }

    public float getKnockback() {
        return knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    public float getSleep() {
        return sleep;
    }

    public void setSleep(float sleep) {
        this.sleep = sleep;
    }
}
