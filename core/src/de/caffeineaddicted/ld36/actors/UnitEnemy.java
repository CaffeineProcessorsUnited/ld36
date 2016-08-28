package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;

public class UnitEnemy extends UnitBase {
    public UnitEnemy.Type type;
    private float freezeTime;
    private float knockbackTime;
    private float speed;

    public UnitEnemy(UnitEnemy.Type type) {
        SGL.game().log("Spawning enemy: " + type.name());
        this.type = type;
        update();
    }

    public void freeze(float freezeTime) {
        this.freezeTime = freezeTime;
        //addTexture(type.fileFreeze);
    }

    public void unfreeze() {
        freezeTime = -1;
        addTexture(type.fileActive);
    }

    @Override
    public void receiveDamage(float damage) {
        if (getHp() < 0)
            return;
        super.receiveDamage(damage);
        knockbackTime = 0;
    }

    public void receiveDamage(float damage, float knockback) {
        SGL.game().debug("RECEIVED DAMAGE:d:" + damage + ",k:" + knockback);
        receiveDamage(damage);
        speed -= knockback;
        knockbackTime = 0;

        if (getHp() < 0)
            onDie();

    }

    @Override
    public void update() {
        setMaxhp(type.hp);
        setHp(type.hp);
        addTexture(type.fileActive);
    }

    @Override
    protected void onDie() {
        SGL.provide(DemoModeSaveState.class).provide().deleteLater.add(this);
        SGL.provide(DemoModeSaveState.class).provide().points += type.points;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        UnitCastle castle = SGL.provide(DemoModeSaveState.class).provide().getCastle();

        if (MathUtils.intersectRect(getX(), getY(),
                getX() + getWidth(), getY() + getHeight(),
                castle.getX(), castle.getY(),
                castle.getX() + castle.getWidth(), castle.getY() + castle.getHeight())) {
            castle.receiveDamage(type.damage);
            SGL.game().log("----UNICORN-----");
            SGL.provide(GameScreen.class).points -= type.points;
            onDie();
            return;
        }

        if (getY() > GameScreen.groundHeight) {
            setY(Math.max(GameScreen.groundHeight, getY() - GameScreen.gravity * delta));
        }

        float speedDiff = speed - type.speed;
        int speedDir = 0;
        if (speedDiff < -0.1)
            speedDir = -1;
        if (speedDiff > 0.1)
            speedDir = 1;

        if (Math.abs(speedDiff) > 0.1) {
            speed = speed - (speedDiff - speedDir * knockbackTime * type.drag);
        }

        freezeTime -= delta;
        if (freezeTime > 0)
            return;

        setX(getX() - speed * delta);
    }

    @Override
    public String name() {
        return "UnitEnemy";
    }

    @Override
    public String addTexture(String name, Texture texture) {
        return addActor(name, new Animation(texture, this.type.frameCount, this.type.width, this.type.height));
    }

    public static enum Type {
        Rider(100, 20, 300, 0.5f, 50f, 10, 10, "raw/enemy_horse_rider/Combined.png", 4, 100, 100),
        Wiking(50f, 10f, 70f, 0.5f, 20f, 5, 10, "raw/enemy_viking/combined.png", 7, 70, 68),
        Soldier(30f, 20f, 90f, 0.6f, 10f, 1, 5, "raw/enemy_soldier/combined.png", 4, 70, 68);
        public final float hp;
        public final float armor;
        public final float mass;
        public final float drag;
        public final float speed;
        public final int points;
        public final int damage;
        public final String fileActive;
        public final int frameCount;
        public final int width;
        public final int height;

        Type(float hp, float armor, float mass, float drag, float speed, int points, int damage, String file, int frameCount, int width, int height) {
            this.hp = hp;
            this.armor = armor;
            this.mass = mass;
            this.drag = drag;
            this.speed = speed;
            this.points = points;
            this.damage = damage;
            this.fileActive = file;
            this.frameCount = frameCount;
            this.width = width;
            this.height = height;

        }

        public static Type getRandom() {
            return values()[MathUtils.random(0, values().length - 1)];
        }
    }
}
