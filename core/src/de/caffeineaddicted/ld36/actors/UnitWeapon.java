package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

public class UnitWeapon extends Entity {

    private Weapon.Type definition;
    private Image texture;
    private float lastShot;

    private String ACTOR_TEXTURE;

    public UnitWeapon() {
        texture = new Image();
        select(Weapon.Type.TestWeapon);
    }

    public void select(Weapon.Type definition) {
        this.definition = definition;
        removeActor(getActor(ACTOR_TEXTURE));
        ACTOR_TEXTURE = addTexture(definition.texture);
        getActor().setRotation(-90);
        update();
    }

    @Override
    public void update() {
        lastShot = definition.reload_time;
    }

    public Actor getActor() {
        return getActor(ACTOR_TEXTURE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        lastShot -= delta;

        if (lastShot < 0) {
            lastShot = definition.reload_time;
            //TODO: Fire new Projectile
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_TEXTURE).draw(batch, parentAlpha);
    }

}