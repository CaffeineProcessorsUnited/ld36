package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.weapons.Weapon;

public class UnitWeapon extends Entity {

    private Weapon weapon;
    private String ACTOR_TEXTURE;
    private Animation animation;

    public UnitWeapon() {
    }

    public void select(Weapon weapon) {
        this.weapon = weapon;
        removeActor(getActor(ACTOR_TEXTURE));
        Weapon.Type.Level weaponLevel = weapon.type.getLevel(weapon.getLevel());
        ACTOR_TEXTURE = addTexture(weaponLevel.texture);
        //getActor().setRotation(-90);
        update();
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    @Override
    public void update() {

    }

    @Override
    public String addTexture(String name, Texture texture) {
        if (animation != null) {
            animation = null;
        }
        Weapon.Type.Level weaponLevel = weapon.type.getLevel(weapon.getLevel());
        if (weaponLevel.isAnimated) {
            animation = new Animation(texture, weaponLevel.animationCount, weaponLevel.animationWidth, weaponLevel.animationHeight, false);
            return addActor(name, animation);
        } else {
            return super.addTexture(name, texture);
        }
    }

    public Actor getActor() {
        return getActor(ACTOR_TEXTURE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public Animation getAnimation() {
        return this.animation;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_TEXTURE).draw(batch, parentAlpha);
    }

}
