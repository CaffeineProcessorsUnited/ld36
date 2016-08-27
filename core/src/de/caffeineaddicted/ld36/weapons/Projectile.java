package de.caffeineaddicted.ld36.weapons;

/**
 * Created by felix on 27.08.16.
 */
public class Projectile {

    public final Type type;
    private float angle; // should be value between 0 and 360

    public Projectile(Type type) {
        this.type = type;
        angle = 0;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public static enum Type{
        TestProjectile(5f, 5f, "sample_projectile.png");

        public final float speed;
        public final float weight;
        public final String texture;

        Type(float speed, float weight, String texture){
            this.speed = speed;
            this.weight = weight;
            this.texture = texture;
        }
    }
}
