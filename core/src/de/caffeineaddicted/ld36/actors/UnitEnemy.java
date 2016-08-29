package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;

public class UnitEnemy extends UnitBase {
    public UnitEnemy.Type type;
    private float freezeTime;
    private float knockbackTime;
    private float speed;

    private String ACTOR_HEALTHBAR, ACTOR_UNIT;

    public UnitEnemy(UnitEnemy.Type type, float waveMultiplier){
        SGL.game().log("Spawning enemy: " + type.name());
        this.type = type;
        setMaxhp(type.hp*waveMultiplier);
        setHp(type.hp*waveMultiplier);
        ACTOR_UNIT = addTexture(type.fileActive);
        SGL.game().log(getActor(ACTOR_UNIT).getHeight() + " height for unit");
        ACTOR_HEALTHBAR = addActor(new ProgressBar(6));
        getActor(ACTOR_HEALTHBAR).setWidth(getActor(ACTOR_UNIT).getWidth() * 0.6f);
        getActor(ACTOR_HEALTHBAR).setPosition(getActor(ACTOR_UNIT).getWidth() * 0.2f, getActor(ACTOR_UNIT).getHeight() + 5);

        speed = type.speed;
    }

    public UnitEnemy(UnitEnemy.Type type) {
        this(type,1);
    }

    public void freeze(float freezeTime) {
        //this.freezeTime = freezeTime;
        //addTexture(type.fileFreeze);
    }

    public void unfreeze() {
        freezeTime = -1;
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
    protected void onDie() {
        SGL.provide(DemoModeSaveState.class).provide().deleteLater.add(this);
        SGL.provide(DemoModeSaveState.class).provide().points += type.points;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        knockbackTime += delta;

        getActor(ACTOR_HEALTHBAR, ProgressBar.class).setPercentage(getHp() / getMaxhp());

        UnitCastle castle = SGL.provide(DemoModeSaveState.class).provide().getCastle();

        if (MathUtils.intersectRect(getX(), getY(),
                getX() + getWidth(), getY() + getHeight(),
                castle.getX(), castle.getY(),
                castle.getX() + castle.getWidth(), castle.getY() + castle.getHeight())) {
            if (alive()) {
                castle.receiveDamage(type.damage);
                SGL.provide(GameScreen.class).points -= type.points;
                setHp(-1);
                onDie();
                return;
            }
        }
        float nexty = getY();
        if (getY() > GameScreen.groundHeight) {
            nexty = Math.max(GameScreen.groundHeight, getY() - GameScreen.gravity * delta);
        }

        float speedDiff = speed - type.speed;
        int speedDir = 0;
        if (speedDiff < -0.1)
            speedDir = -1;
        if (speedDiff > 0.1)
            speedDir = 1;

        if (Math.abs(speedDiff) > 0.1) {
            speed = speed - (speedDiff*knockbackTime * type.drag);
        }
        freezeTime -= delta;
        if (freezeTime > 0)
            return;
        float nextx = getX() - speed * delta;

        this.setPosition(nextx, nexty);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_UNIT).draw(batch, parentAlpha);
        getActor(ACTOR_HEALTHBAR).draw(batch, parentAlpha);
    }

    @Override
    public String name() {
        return "UnitEnemy";
    }

    @Override
    public void update() {

    }

    @Override
    public String addTexture(String name, Texture texture) {
        return addActor(name, new Animation(texture, this.type.frameCount, this.type.width, this.type.height));
    }

    public static enum Type {
        Rider(60, 20, 300, 0.5f, 60f, 10, 10, "raw/enemy_horse_rider/Combined.png", 4, 100, 100,3),
        Wiking(30f, 10f, 70f, 0.5f, 50f, 5, 10, "raw/enemy_viking/combined.png", 7, 70, 68,0),
        Soldier(20f, 20f, 90f, 0.6f, 40f, 1, 5, "raw/enemy_soldier/combined.png", 4, 70, 68,0),
        Tower(1000f, 380f, 1000, 4f, 10f, 50, 50, "raw/tower/Combined.png", 5, 80, 128,5);
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
        public final int firstWave;

        Type(float hp, float armor, float mass, float drag, float speed, int points, int damage, String file, int frameCount, int width, int height, int firstWave) {
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
            this.firstWave = firstWave;
        }

        public static Type getRandom(int wavecount) {
            Type plannedType;
            do {
                plannedType = values()[MathUtils.random(0, values().length - 1)];
            } while (plannedType.firstWave > wavecount);
            return plannedType;
        }
    }
}
