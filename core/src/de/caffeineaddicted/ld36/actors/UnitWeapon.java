package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.weapons.Weapon;

public class UnitWeapon extends Entity {

    private Weapon weapon;
    private Image texture;
    private String ACTOR_TEXTURE;

    public UnitWeapon() {
        texture = new Image();
        select(new Weapon(Weapon.Type.Stone));
    }

    public void select(Weapon definition) {
        this.weapon = definition;
        removeActor(getActor(ACTOR_TEXTURE));
        ACTOR_TEXTURE = addTexture(definition.type.getLevel(weapon.getLevel()).texture);
        getActor().setRotation(-90);
        update();
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    @Override
    public void update() {

    }

    public Actor getActor() {
        return (Actor) getActor(ACTOR_TEXTURE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_TEXTURE).draw(batch, parentAlpha);
    }

}
