package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

abstract public class UnitBase extends Group {
    private SGLStage stage;
    private float hp;

    public UnitBase(SGLStage stage) {
        super();
        this.stage = stage;
    }

    abstract public void update();

    abstract protected void onDie();

    public void addTexture(String texture) {
        addTexture(texture, SGL.provide(Assets.class).get(texture, Texture.class));
    }

    public void addTexture(String name, Texture texture) {
        addDrawable(name, new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void addDrawable(String name, Drawable drawable) {
        addActor(new Image(drawable));
    }

    public void clearTextures() {
        clearChildren();
    }

    void setHp(float hp) {
        this.hp = hp;
    }

    float getHp() {
        return hp;
    }

    void receiveDamage(float damage) {
        hp -= damage;
        if (hp < 0) {
            onDie();
        }
    }

    boolean alive() {
        return hp >= 0;
    }
}
