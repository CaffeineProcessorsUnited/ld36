package de.caffeineaddicted.ld36.weapons;

import de.caffeineaddicted.ld36.actors.Projectile;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.sgl.SGL;

public class Weapon {
    public final Weapon.Type type;
    public final Projectile.Type projectile;
    boolean available;

    public Weapon(Weapon.Type type) {
        this.type = type;
        this.projectile = type.projectile;
        this.available = false;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Projectile fire(float angle){
        Projectile p = new Projectile(projectile);
        p.setAngle(angle);
        return p;
    }

    public static enum Type {
        TestWeapon("Sample Weapon", 0f, 10f, "sample.png", Projectile.Type.TestProjectile);

        public final String name;
        public final float reload_time;
        public final float research_time;
        public final String texture;
        public final Projectile.Type projectile;

        Type(String name, float reload_time, float research_time, String texture, Projectile.Type projectile) {
            this.name = name;
            this.reload_time = reload_time;
            this.research_time = research_time;
            this.texture = texture;
            this.projectile = projectile;
        }
    }
}
