package de.caffeineaddicted.ld36.actors;

import de.caffeineaddicted.ld36.screens.GameScreen;
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
        this.type = type;
        update();
    }

    public void freeze(float freezeTime) {
        this.freezeTime = freezeTime;
        addTexture(type.fileFreeze);
    }

    public void unfreeze() {
        freezeTime = -1;
        addTexture(type.fileActive);
    }

    @Override
    public void receiveDamage(float damage) {
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
        SGL.provide(GameScreen.class).deleteLater.add(this);
        SGL.provide(GameScreen.class).points += type.points;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        SGL.game().log("---"+getHp());

        UnitCastle castle = SGL.provide(GameScreen.class).getCastle();

        //SGL.game().log("---###"+getWidth()+","+getHeight()+"//"+castle.getWidth()+","+castle.getHeight());
        if(MathUtils.intersectRect(getX(), getY(),
                getX()+getWidth(),getY()+getHeight(),
                castle.getX(),castle.getY(),
                castle.getX()+castle.getWidth(),castle.getY()+castle.getHeight()))
        {
            castle.receiveDamage(type.damage);
            SGL.game().log("----UNICORN-----");
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

    public static enum Type {
        TEST(100, 20, 100, 0.5f, 20f, 1, 10, "Enemy");

        public final float hp;
        public final float armor;
        public final float mass;
        public final float drag;
        public final float speed;
        public final int points;
        public final int damage;
        public final String fileActive;
        public final String fileFreeze;

        Type(float hp, float armor, float mass, float drag, float speed, int points, int damage, String file) {
            this.hp = hp;
            this.armor = armor;
            this.mass = mass;
            this.drag = drag;
            this.speed = speed;
            this.points = points;
            this.damage = damage;
            this.fileActive = file + "_active.png";
            this.fileFreeze = file + "_frozen.png";
        }

        public static Type getRandom(){
            return values()[MathUtils.random(0,values().length-1)];
        }
    }
}
