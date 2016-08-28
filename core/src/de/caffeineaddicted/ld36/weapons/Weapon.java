package de.caffeineaddicted.ld36.weapons;

import de.caffeineaddicted.ld36.actors.Projectile;

import java.util.ArrayList;

public class Weapon {
    public final Weapon.Type type;
    private int level;
    private boolean available;

    public Weapon(Weapon.Type type) {
        this.type = type;
        this.level = 0;
        this.available = false;
    }

    public int getLevel() {
        return this.level;
    }

    public void levelUp() {
        if (level < type.maxlevel()) {
            level++;
        }
    }

    public boolean levelUpAvailable() {
        if (level < type.maxlevel()) {
            return true;
        }
        return false;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Projectile fire(float angle) {
        Projectile p = new Projectile(type.getLevel(level).projectile);
        p.setAngle(angle);
        if (type.getLevel(level).singleUse) {
            this.setAvailable(false);
        }
        return p;
    }


    public static enum Type {
        Stone(new Level("Stone", 0.2f, 0f, 0, 70f, "raw/Stone/Stone1.png", false, Projectile.Type.StoneProjectile)),
        Bow(new Level("Bow", 0.1f, 60f, 1, 300f, "bow.png", false, Projectile.Type.BowArrow)),
        Crossbow(new Level("Crossbow", 0.5f, 90f, 2, 300f, "crossbow.png", false, Projectile.Type.CrossbowBolt)),
        Speer(new Level("Speer", 2f, 60f, 3, 100f, "speer.png", false, Projectile.Type.Javelin)),
        Shuriken(new Level("Shuriken", 0.05f, 120f, 5, 90f, "shurkine.png", false, Projectile.Type.Shuriken)),
        Catapult(new Level("Catapult", 5f, 180f, 10, 500f, "catapult.png", false, Projectile.Type.CatapultAmmo)),
        Balliste(new Level("Baliste", 7f, 210f, 10, 500f, "baliste.png", false, Projectile.Type.BallisteArrow)),
        Cannon(new Level("Cannon", 6f, 240f, 12, 500f, "cannon.png", false, Projectile.Type.CannonBall)),
        ExplosiveBarrel(new Level("Explosive Barrel", 10f, 270f, 15, 80f, "explosivebarrel.png", false, Projectile.Type.ExplosiveBarrel)),
        Tomahawk(new Level("Tomahawk", 3f, 60f, 4, 90f, "tomahawk.png", false, Projectile.Type.Tomahawk)),
        Trident(new Level("Trident", 4f, 90f, 7, 100f, "trident.png", false, Projectile.Type.Trident)),
        UnicornUlt(new Level("Unicorn Ult", 240f, 600f, 20, 1000f, "unicorn.png", true, Projectile.Type.Unicorn));

        private ArrayList<Level> levels = new ArrayList<Level>();

        Type(Level... levels) {
            for (Level l : levels) {
                this.levels.add(l);
            }

        }

        public int maxlevel() {
            return this.levels.size() - 1;
        }

        public Level getLevel(int level) {
            return this.levels.get(level);
        }

        public static class Level {
            public final String name;
            public final float reload_time;
            public final float research_time;
            public final int unlockPrice;
            public final float range;
            public final String texture;
            public final boolean singleUse;
            public final Projectile.Type projectile;

            Level(String name, float reload_time, float research_time, int unlockPrice, float range, String texture, boolean singleUse, Projectile.Type projectile) {
                this.name = name;
                this.reload_time = reload_time;
                this.research_time = research_time;
                this.unlockPrice = unlockPrice;
                this.range = range;
                this.texture = texture;
                this.singleUse = singleUse;
                this.projectile = projectile;
            }
        }

    }
}
