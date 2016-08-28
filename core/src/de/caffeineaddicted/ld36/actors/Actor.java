package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Malte Heinzelmann
 */
public abstract class Actor extends com.badlogic.gdx.scenes.scene2d.Actor {
    protected Vector2 center = new Vector2();
    protected Vector2 centerpoint = new Vector2();
    private Group parent;
    private Stage stage;

    public void init() {
        sizeChanged();
        positionChanged();
        rotationChanged();
    }

    public Group parent() {
        return parent;
    }

    public void parent(Group parent) {
        this.parent = parent;
    }

    public Stage stage() {
        return stage;
    }

    public void stage(Stage stage) {
        this.stage = stage;
    }

    public abstract String name();

    public float getX() {
        return super.getX() + ((parent() != null) ? parent().getX() : 0);
    }

    public float getY() {
        return super.getY() + ((parent() != null) ? parent().getY() : 0);
    }

    public float getTRX() {
        return getX() + getWidth();
    }

    public float getTRY() {
        return getY() + getHeight();
    }

    public void setRotation2(float degrees) {

    }

    public Vector2 getCenter() {
        return center;
    }

    public Vector2 getCenterPoint() {
        return centerpoint;
    }

    private void updateCenter() {
        center.set(getWidth() / 2, getHeight() / 2);
        updateCenterPoint();
    }

    private void updateCenterPoint() {
        centerpoint.set(center.x + getX(), center.y + getY());
    }

    @Override
    protected void positionChanged() {
        updateCenterPoint();
    }

    @Override
    protected void sizeChanged() {
        updateCenter();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "//" + getName();
    }

    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    public void setCenterPosition(float x, float y) {
        setX(x - getWidth() / 2);
        setY(y - getHeight() / 2);
    }
}
