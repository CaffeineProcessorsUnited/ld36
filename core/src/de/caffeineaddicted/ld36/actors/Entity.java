package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class Entity extends Group {
    private Map<String, Actor> children = new HashMap<String, Actor>();

    public static ArrayList<Entity> entities = new ArrayList<Entity>();

    public static ArrayList<Entity> GetEntitiesInRect(float x1, float y1, float x2, float y2) {
        ArrayList<Entity> list = new ArrayList<Entity>();

        for (Entity entity : entities) {
            if (MathUtils.intersectRect(entity.getX(), entity.getY(),
                    entity.getX() + entity.getHeight(), entity.getY() + entity.getHeight(),
                    x1, y1, x2, y2)) {
                list.add(entity);
            }
        }
        return list;
    }

    public static ArrayList<Entity> getEntitiesInRange(float x, float y, float range) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (Entity entity : entities) {
            if (MathUtils.intersectCircleRect(x, y, range,
                    entity.getX(), entity.getY(),
                    entity.getX() + entity.getHeight(), entity.getY() + entity.getHeight())) {
                list.add(entity);
            }
        }
        return list;
    }

    public Entity(){
        entities.add(this);
    }

    public void destroy(){
        entities.remove(this);
    }

    protected Vector2 center = new Vector2();
    protected Vector2 centerpoint = new Vector2();

    private void updateCenter() {
        center.set(getWidth() / 2, getHeight() / 2);
        updateCenterPoint();
    }

    private void updateCenterPoint() {
        centerpoint.set(center.x + getX(), center.y + getY());
    }

    abstract public void update();

    public void addTexture(String texture) {
        addTexture(texture, SGL.provide(Assets.class).get(texture, Texture.class));
    }

    public void addTexture(String name, Texture texture) {
        children.put(name, new Image(texture));
        addActor(children.get(name));
    }

    public void clearTextures() {
        clearChildren();
    }

    public Vector2 getCenter() {
        return center;
    }

    public Vector2 getCenterPoint() {
        return centerpoint;
    }

    @Override
    protected void positionChanged () {
        updateCenterPoint();
    }

    @Override
    protected void sizeChanged () {
        updateCenter();
    }


}
