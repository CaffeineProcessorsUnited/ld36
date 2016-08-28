package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

import java.util.ArrayList;

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
        if(getHp() < 0)
            return;
        super.receiveDamage(damage);
        knockbackTime = 0;
    }

    public void receiveDamage(float damage, float knockback) {
        SGL.game().debug("RECEIVED DAMAGE:d:"+damage+",k:"+knockback);
        receiveDamage(damage);
        speed -= knockback;
        knockbackTime = 0;

        if(getHp() < 0)
            onDie();

    }

    @Override
    public void update() {
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

        if(MathUtils.intersectRect(getX(), getY(),
                getX()+getWidth(),getY()+getHeight(),
                castle.getX(),castle.getY(),
                castle.getX()+castle.getWidth(),castle.getY()+castle.getHeight()))
        {
            castle.receiveDamage(type.damage);
            SGL.game().log("----UNICORN-----");
            SGL.provide(GameScreen.class).points -= type.points;
            onDie();
            return;
        }

        if(getY() > GameScreen.groundHeight){
            setY(Math.max(GameScreen.groundHeight, getY()-GameScreen.gravity*delta));
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

        setX(getX() - speed*delta);
    }

    @Override
    public String name() {
        return "UnitEnemy";
    }

    @Override
    public String addTexture(String name, Texture texture){
        return addActor(name, new AnimationRenderer(texture, 4, 100, 100));
    }

    public static enum Type {
        TEST(1, 20, 100, 0.5f, 200f, 1, 10, "raw/enemy_horse_rider/Combined.png");

        public final float hp;
        public final float armor;
        public final float mass;
        public final float drag;
        public final float speed;
        public final int points;
        public final int damage;
        public final String fileActive;

        Type(float hp, float armor, float mass, float drag, float speed, int points, int damage, String file) {
            this.hp = hp;
            this.armor = armor;
            this.mass = mass;
            this.drag = drag;
            this.speed = speed;
            this.points = points;
            this.damage = damage;
            this.fileActive = file;
        }

        public static Type getRandom(){
            return values()[MathUtils.random(0,values().length-1)];
        }
    }
}
