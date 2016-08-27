package de.caffeineaddicted.ld36.actors;

import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.weapons.Damage;

public class Projectile extends Entity {

    public final Type type;
    private float angle; // should be value between 0 and 360

    public Projectile(Type type) {
        this.type = type;
        angle = 0;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Damage calculateDamage(UnitEnemy enemy) {
        Damage enemydamage = new Damage();
        //Todo Calculate Damages

        //Calculate Sleep Time
        float maxsleeptime = 4f;
        float minsleeptime = 1f;
        boolean sleep = false;
        if (Math.random() <= this.type.freeze_chance) {
            sleep = true;
        }
        if (sleep) {
            enemydamage.setSleep((float)((maxsleeptime-minsleeptime) * Math.random() + minsleeptime));
        } else {
            enemydamage.setSleep(0);
        }
        return enemydamage;
    }

    public static enum Type{
        TestProjectile(5f, 5f, "sample_projectile.png", 1.0f, 1.0f, 1.0f, 1.0f, 0.1f);

        public final float speed;
        public final float weight;
        public final String texture;

        public final float damage;
        public final float armor_piercing;
        public final float crit_hit_chance; //should be a value between 0 and 1
        public final float knockback;
        public final float freeze_chance; //should be a value between 0 and 1


        Type(float speed, float weight, String texture, float damage, float armor_piercing, float crit_hit_chance,float knockback, float freeze_chance){
            this.speed = speed;
            this.weight = weight;
            this.texture = texture;

            this.damage = damage;
            this.armor_piercing = armor_piercing;
            this.crit_hit_chance = crit_hit_chance;
            this.knockback = knockback;
            this.freeze_chance = freeze_chance;
        }
    }
}
