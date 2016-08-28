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
        Stone("Stone", 0.2f, 0f, 70f, "cannon.png", Projectile.Type.StoneProjectile),
        Bow("Bow", 0.1f, 60f, 300f, "bow.png", Projectile.Type.BowArrow),
        Crossbow("Crossbow", 0.5f, 90f, 300f, "crossbow.png", Projectile.Type.CrossbowBolt),
        Speer("Speer", 2f, 60f, 100f, "speer.png", Projectile.Type.ThrowableSpeer),
        Shuriken("Shuriken", 0.05f, 120f, 90f, "shurkine.png", Projectile.Type.Shuriken),
        Catapult("Catapult", 5f, 300f, 2f, "catapult.png", Projectile.Type.CatapultAmmo),
        Baliste("Baliste",  7f, 400f, 2f, "baliste.png", Projectile.Type.ballisteArrow);

        public final String name;
        public final float reload_time;
        public final float research_time;
        public final float range;
        public final String texture;
        public final Projectile.Type projectile;

        Type(String name, float reload_time, float research_time, float range, String texture, Projectile.Type projectile) {
            this.name = name;
            this.reload_time = reload_time;
            this.research_time = research_time;
            this.range = range;
            this.texture = texture;
            this.projectile = projectile;
        }
    }
}
