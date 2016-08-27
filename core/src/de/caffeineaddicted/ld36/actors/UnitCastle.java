package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

import java.util.ArrayList;

public class UnitCastle extends UnitBase {
    private Weapons weapons;
    private int activeWeapon;
    private float lastShot;
    private int activeResearch;
    private float researchTime;

    public UnitCastle(SGLStage stage, UnitCastle.Weapons weapons) {
        super(stage);
        this.weapons = weapons;
        activeWeapon = 0;
        setHp(1000);
        lastShot = 0;
        activeWeapon = -1;

        update();
    }

    public Weapon weapon(int type) {
        return weapons.get(type);
    }

    public Weapon getActiveWeapon() {
        return weapon(activeWeapon);
    }

    public void setActiveWeapon(int type) {
        if (weapon(type).isAvailable()) {
            activeWeapon = type;
            lastShot = 0;
        }
    }

    public void startResearch(int wid) {
        if (wid < weapons.length()) {
            if (!weapon(wid).isAvailable()) {
                activeResearch = wid;
                researchTime = weapon(wid).type.research_time;
            }
        }
    }

    public void completeResearch() {
        if (activeResearch >= 0 && researchTime < 0) {
            weapon(activeResearch).setAvailable(true);
        }
    }

    @Override
    public void update() {
        addTexture("TowerBase.png");
        addTexture(getActiveWeapon().type.texture);
        lastShot = getActiveWeapon().type.reload_time;
    }

    @Override
    protected void onDie() {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (activeResearch >= 0 && researchTime < 0) {
            completeResearch();
        }
        activeResearch -= delta;
        lastShot -= delta;

        if (lastShot < 0) {
            lastShot = getActiveWeapon().type.reload_time;
            //TODO: Fire new Projectile
        }
    }

    public static enum Weapons {
        TEST(new Weapon(Weapon.Type.TestWeapon));
        private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

        Weapons(Weapon... ws) {
            for (Weapon w : ws) {
                weapons.add(w);
            }
        }

        public int length() {
            return weapons.size();
        }

        public Weapon get(int type) {
            return weapons.get(type);
        }
    }
}
