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
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

import java.util.HashMap;
import java.util.Map;

abstract public class Entity extends Group {
    private Map<String, Actor> children = new HashMap<String, Actor>();

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
