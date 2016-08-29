package de.caffeineaddicted.ld36.weapons;

import de.caffeineaddicted.ld36.actors.Projectile;

import java.util.ArrayList;

public class Weapon {
    public final Weapon.Type type;
    private int level;
    private boolean available;
    private float waitForShot;

    public Weapon(Weapon.Type type) {
        this.type = type;
        this.level = 0;
        this.available = false;
        waitForShot = type.getLevel(0).reload_time;
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

    public boolean canFire(){
        return isAvailable() && waitForShot < 0;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Projectile fire(float angle) {
        if(waitForShot >= 0)
            return null;

        Projectile p = new Projectile(type.getLevel(level).projectile);
        p.setAngle(angle);
        if (type.getLevel(level).singleUse) {
            this.setAvailable(false);
        }
        waitForShot = type.getLevel(getLevel()).reload_time;
        return p;
    }

    public void act(float delta){
        waitForShot-=delta;
    }


    public static enum Type {
        // TODO: adjust textures
        Stone(new Level("Stone", 0.2f, 0f, 0, 70f, false, Projectile.Type.StoneProjectile, "raw/Stone/Stone1.png", "raw/Stone/Stone1.png", false, 0, 0, 0)),
        Bow(new Level("Bow", 0.1f, 60f, 1, 300f, false, Projectile.Type.BowArrow, "raw/arrow/ArchersCombined.png", "raw/arrow/arrow3.png", true, 4, 128, 128)),
        Crossbow(new Level("Crossbow", 0.5f, 90f, 2, 300f, false, Projectile.Type.CrossbowBolt, "raw/crossbow/bolt.png", "raw/crossbow/bolt.png", false, 0, 0, 0)),
        Speer(new Level("Speer", 2f, 60f, 3, 100f, false, Projectile.Type.Javelin, "raw/Javelin/Javelin3.png", "raw/Javelin/Javelin3.png", false, 0, 0, 0)),
        Shuriken(new Level("Shuriken", 0.05f, 120f, 5, 90f, false, Projectile.Type.Shuriken, "raw/shuriken/Shuriken2.png", "raw/shuriken/Shuriken2.png", false, 0, 0, 0)),
        Catapult(new Level("Catapult", 5f, 180f, 10, 500f, false, Projectile.Type.CatapultAmmo, "raw/catapult/Combined.png", "raw/catapult/step2.png", true, 4, 128, 128)),
        Balliste(new Level("Baliste", 7f, 210f, 10, 500f, false, Projectile.Type.BallisteArrow, "raw/balliste/Combined.png", "raw/balliste/step3.png", true, 3, 128, 128)),
        Cannon(new Level("Cannon", 6f, 240f, 12, 500f, false, Projectile.Type.CannonBall, "raw/cannon/Combined.png", "raw/cannon/step1.png", true, 8, 128, 128)),
        ExplosiveBarrel(new Level("Explosive Barrel", 10f, 270f, 15, 80f, false, Projectile.Type.ExplosiveBarrel, "raw/barrel/Barrel.png", "raw/barrel/Barrel.png", false, 0, 0, 0)),
        Tomahawk(new Level("Tomahawk", 3f, 60f, 4, 90f, false, Projectile.Type.Tomahawk, "raw/axe/axe3.png", "raw/axe/axe3.png", false, 0, 0, 0)),
        Trident(new Level("Trident", 4f, 90f, 7, 100f, false, Projectile.Type.Trident, "raw/trident/Trident.png", "raw/trident/Trident.png", false, 0, 0, 0)),
        UnicornUlt(new Level("Unicorn Ult", 240f, 600f, 20, 1000f, true, Projectile.Type.Unicorn, "cannon.png", "cannon.png", false, 0, 0, 0));

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
            public final boolean singleUse;
            public final Projectile.Type projectile;

            public final String texture;
            public final String preview;
            public final boolean isAnimated;
            public final int animationCount;
            public final int animationWidth;
            public final int animationHeight;

            Level(String name, float reload_time, float research_time, int unlockPrice, float range, boolean singleUse, Projectile.Type projectile, String texture, String preview, boolean isAnimated, int animationCount, int animationWidth, int animationHeight) {
                this.name = name;
                this.reload_time = reload_time;
                this.research_time = research_time;
                this.unlockPrice = unlockPrice;
                this.range = range;
                this.singleUse = singleUse;
                this.projectile = projectile;

                this.texture = texture;
                this.preview = preview;
                this.isAnimated = isAnimated;
                this.animationCount = animationCount;
                this.animationWidth = animationWidth;
                this.animationHeight = animationHeight;
            }
        }

    }
}
