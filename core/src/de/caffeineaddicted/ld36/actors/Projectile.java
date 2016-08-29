package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.ld36.weapons.Damage;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

public class Projectile extends Entity {
    public final Type type;
    private float directionX, directionY;
    private boolean finished;
    private String ACTOR_PROJECTILE;

    public Projectile(Type type) {
        super();
        this.type = type;
        directionX = directionY = 0;
        finished = false;
        zindex(MathUtils.random(0, 10));
        ACTOR_PROJECTILE = addTexture(type.texture);
        update();
    }

    public void onDie() {
        SGL.provide(DemoModeSaveState.class).provide().deleteLater.add(this);
    }

    public void setAngle(float angle) {
        directionX = (float) -Math.sin(Math.toRadians(angle));
        directionY = (float) Math.cos(Math.toRadians(angle));
    }

    public Damage calculateDamage(UnitEnemy enemy) {
        Damage enemydamage = new Damage();
        //Calculate Hp Damage
        float hpDamage = 0;
        //chanse to pierce armor -> full damage
        if (Math.random() < this.type.armor_piercing) {
            hpDamage = this.type.damage;
        } else {
            float damageReduction = (float) (1 - (Math.sqrt(enemy.type.armor) / 20));
            hpDamage = this.type.damage * damageReduction;
        }
        if (Math.random() < this.type.crit_hit_chance) {
            hpDamage *= 2;
        }
        enemydamage.setHp_damage(hpDamage);

        // Calculate Knockback
        enemydamage.setKnockback((float) Math.random() * type.knockback);

        //Calculate Sleep Time
        float maxsleeptime = 4f;
        float minsleeptime = 1f;
        boolean sleep = false;
        if (Math.random() < this.type.freeze_chance) {
            sleep = true;
        }
        if (sleep) {
            enemydamage.setSleep((float) ((maxsleeptime - minsleeptime) * Math.random() + minsleeptime));
        } else {
            enemydamage.setSleep(0);
        }
        return enemydamage;
    }

    public Vector2 nextPosition(Vector2 pos, float delta) {
        directionY -= type.weight * GameScreen.gravity * delta * delta;
        float newX = pos.x + type.speed * directionX * delta;
        float newY = pos.y + type.speed * directionY * delta;
        if (newY < GameScreen.groundHeight) {
            float percentage = (pos.y - GameScreen.groundHeight) / (pos.y - newY);
            newX = pos.x + percentage * (newX - pos.x);
            newY = GameScreen.groundHeight;
        }
        return new Vector2(newX, newY);
    }

    @Override
    public void update() {
    }

    @Override
    public void act(float delta) {
        if (finished) {
            onDie();
            return;
        }
        super.act(delta);

        Vector2 pos = nextPosition(getCenterPoint(), delta);

        Actor a = getActor(ACTOR_PROJECTILE);
        if (a != null) {
            a.setRotation(MathUtils.between(-45, 30, directionY * 90));
        }

        setX(pos.x);
        setY(pos.y);

        ArrayList<Entity> entities = Entity.getEntitiesInRange(pos.x, pos.y, type.range);
        for (Entity entity : entities) {
            if (entity instanceof UnitEnemy) {
                UnitEnemy enemy = (UnitEnemy) entity;
                if (!enemy.alive())
                    continue;
                finished = true;
                Damage damage = calculateDamage(enemy);
                enemy.freeze(damage.getSleep());
                enemy.receiveDamage(damage.getHp_damage(), damage.getKnockback());
            }
        }

        if (pos.y <= GameScreen.groundHeight) {
            finished = true;
        }
    }

    public static enum Type {
        StoneProjectile(300f, 3f, "raw/Stone/Stone1.png", 5.0f, 0f, 0.05f, 20f, 0.1f, 0f),
        StoneProjectile1(400f, 2.5f, "raw/Stone/Stone2.png", 10.0f, 0f, 0.05f, 20f, 0.1f, 0f),
        BowArrow(400f, 2f, "raw/arrow/arrow1.png", 5f, 0f, 0.1f, 5f, 0f, 0f),
        BowArrow1(400f, 1.5f, "raw/arrow/arrow2.png", 15f, 0f, 0.1f, 5f, 0f, 0f),
        CrossbowBolt(400f, 2f, "raw/crossbow/bolt.png", 20f, 0.3f, 0.2f, 0f, 0.2f, 0f),
        CrossbowBolt1(400f, 1.5f, "raw/crossbow/bolt.png", 40f, 0.3f, 0.2f, 0f, 0.2f, 0f),
        Javelin(300f, 6f, "raw/Javelin/Javelin1.png", 50f, 0.5f, 0.25f, 40f, 0.1f, 0f),
        Javelin1(400f, 4f, "raw/Javelin/Javelin2.png", 70f, 0.5f, 0.25f, 100f, 0.1f, 0f),
        Shuriken(200f, 1.5f, "raw/shuriken/Shuriken1.png", 7f, 0f, 0.8f, 0f, 0f, 0f),
        Shuriken1(300f, 1.0f, "raw/shuriken/Shuriken1.png", 17f, 0f, 0.8f, 0f, 0f, 0f),
        CatapultAmmo(2000f, 4f, "raw/Stone/Stone3.png", 80f, 0.5f, 0.9f, 50f, 0.5f, 10f),
        CatapultAmmo1(2000f, 3f, "raw/Stone/Stone3.png", 160f, 0.5f, 0.9f, 70f, 0.5f, 10f),
        BallisteArrow(2000f, 4f, "raw/Javelin/Javelin3.png", 120f, 0.7f, 0.7f, 400f, 0.8f, 0f),
        BallisteArrow1(2000f, 4f, "raw/Javelin/Javelin3.png", 180f, 0.7f, 0.7f, 500f, 0.8f, 0f),
        CannonBall(3000f, 3f, "raw/cannon/cannonball.png", 150f, 1f, 0f, 90f, 0.5f, 20f),
        CannonBall1(3500f, 3f, "raw/cannon/cannonball.png", 400f, 1f, 0f, 200f, 0.5f, 20f),
        ExplosiveBarrel(700f, 5f, "raw/barrel/Barrel.png", 0f, 0.2f, 0.3f, 5000f, 1f, 200f),
        ExplosiveBarrel1(800f, 4f, "raw/barrel/Barrel.png", 50f, 0.2f, 0.3f, 7000f, 1f, 200f),
        Tomahawk(300f, 5f, "raw/axe/axe1.png", 80f, 0.1f, 0.4f, 0f, 0f, 0f),
        Tomahawk1(300f, 4f, "raw/axe/axe3.png", 140f, 0.1f, 0.4f, 0f, 0f, 0f),
        Trident(230f, 5f, "raw/trident/Trident.png", 200f, 1f, 1f, 0f, 0f, 0f),
        Trident1(200f, 3.5f, "raw/trident/Trident.png", 400f, 1f, 1f, 0f, 0f, 0f),
        Unicorn(100f, 1000f, "raw/UnicornUltimate.png", 10000f, 1f, 1f, 0f, 0f, 2000f);

        public final float speed;
        public final float weight;
        public final String texture;

        public final float damage;
        public final float armor_piercing;
        public final float crit_hit_chance; //should be a value between 0 and 1
        public final float knockback;
        public final float freeze_chance; //should be a value between 0 and 1
        public final float range;


        Type(float speed, float weight, String texture, float damage, float armor_piercing, float crit_hit_chance, float knockback, float freeze_chance, float range) {
            this.speed = speed;
            this.weight = weight;
            this.texture = texture;

            this.damage = damage;
            this.armor_piercing = armor_piercing;
            this.crit_hit_chance = crit_hit_chance;
            this.knockback = knockback;
            this.freeze_chance = freeze_chance;
            this.range = range;
        }
    }
}
