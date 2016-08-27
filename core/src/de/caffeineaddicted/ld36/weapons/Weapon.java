package de.caffeineaddicted.ld36.weapons;

import de.caffeineaddicted.ld36.actors.UnitEnemy;

public class Weapon {
    public final Weapon.Type type;
    public final Projectile projectile;
    boolean available;

    public Weapon(Weapon.Type type, Projectile projectile) {
        this.type = type;
        this.projectile = projectile;
        this.available = false;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    public static enum Type {
        TestWeapon("Sample Weapon", 1.0f, 1.0f, 1.0f, 1.0f, 0.1f, 0f, 10f, "sample.png");

        public final String name;
        public final float damage;
        public final float armor_piercing;
        public final float crit_hit_chance; //should be a value between 0 and 1
        public final float knockback;
        public final float freeze_chance; //should be a value between 0 and 1
        public final float reload_time;
        public final float research_time;
        public final String texture;

        Type(String name, float damage, float armor_piercing, float crit_hit_chance,float knockback, float freeze_chance, float reload_time, float research_time, String texture) {
            this.name = name;
            this.damage = damage;
            this.armor_piercing = armor_piercing;
            this.crit_hit_chance = crit_hit_chance;
            this.knockback = knockback;
            this.freeze_chance = freeze_chance;
            this.reload_time = reload_time;
            this.research_time = research_time;
            this.texture = texture;
        }
    }
}
