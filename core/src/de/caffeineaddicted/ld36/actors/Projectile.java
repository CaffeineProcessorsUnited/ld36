package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.weapons.Damage;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

public class Projectile extends Entity {
    public final Type type;
    private float directionX, directionY;
    private boolean finished;

    public Projectile(Type type) {
        super();
        this.type = type;
        directionX = directionY = 0;
        finished = false;
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
        float distance = enemy.getX() - this.getX();
        float knockbackForce = this.type.knockback / (distance * distance + 1);
        float knockbackSpeed = knockbackForce / enemy.type.mass;
        if (distance < 0) {
            knockbackSpeed *= -1;
        }
        enemydamage.setKnockback(knockbackSpeed);

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
        directionY -= GameScreen.gravity * delta * delta;
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
        SGL.game().log("+++++"+type.texture);addTexture(type.texture);
    }

    @Override
    public void act(float delta) {
        if (finished) {
            onDie();
            return;
        }
        super.act(delta);

        Vector2 pos = nextPosition(getCenterPoint(), delta);
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
        StoneProjectile(200f, 50f, "raw/Stone/Stone1.png", 5.0f, 0f, 0.05f, 20f, 0.1f, 0f),
        BowArrow(400f, 2f, "raw/arrow/arrow1.png", 10f, 0f, 0.1f, 5f, 0f, 0f),
        CrossbowBolt(400f, 2f, "raw/crossbow/bolt.png", 30f, 0.3f, 0.2f, 0f, 0.2f, 0f),
        Javelin(250f, 100f, "raw/Javelin/Javelin1.png", 50f, 0.5f, 0.25f, 40f, 0.1f, 0f),
        Shuriken(200f, 1f, "raw/shuriken/Shuriken1.png", 30f, 0f, 0.8f, 0f, 0f, 0f),
        CatapultAmmo(100f, 200f, "raw/Stone/Stone3.png", 100f, 0.5f, 0.9f, 50f, 0.5f, 10f),
        BallisteArrow(400f, 150f, "raw/Javelin/Javelin3.png", 120f, 0.7f, 0.7f, 400f, 0.8f, 0f),
        CannonBall(300f, 200f, "raw/cannon/cannonball.png", 150f, 1f, 0f, 90f, 0.5f, 20f),
        ExplosiveBarrel(40f, 50f, "raw/barrel/Barrel.png", 60f, 0.2f, 0.3f, 1000f, 1f, 200f),
        Tomahawk(300f, 50f, "raw/axe/axe1.png", 80f, 0.1f, 0.4f, 0f, 0f, 0f),
        Trident(230f, 150f, "raw/trident/Trident.png", 100f, 1f, 1f, 0f, 0f, 0f),
        Unicorn(100f, 1000f, "unicornutl.png", 10000f, 1f, 1f, 0f, 0f, 2000f);

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
