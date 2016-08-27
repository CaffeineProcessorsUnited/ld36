package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

abstract public class UnitBase extends Entity {
    private float hp;

    public UnitBase(GameScreen screen) {
        super(screen);
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
