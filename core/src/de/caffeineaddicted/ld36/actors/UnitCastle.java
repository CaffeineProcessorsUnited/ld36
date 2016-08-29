package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.HashMap;

public class UnitCastle extends UnitBase {

    private final static int baseHP = 100;
    private HashMap<Weapon.Type, Weapon> weapons = new HashMap<Weapon.Type, Weapon>();
    private Weapon.Type activeWeapon;
    private Weapon.Type lastWeapon;
    private Weapon.Type activeResearch;
    private float researchTime;
    private UnitWeapon unitWeapon;
    private float lastShot;
    private String ACTOR_BASE, ACTOR_WEAPON, ACTOR_HEALTHBAR, ACTOR_RESEARCHBAR, ACTOR_COOLDOWNBAR;


    public UnitCastle() {
        ACTOR_BASE = addTexture("kenney/castle.png");
        setWidth(getActor(ACTOR_BASE).getWidth());
        setHeight(getActor(ACTOR_BASE).getHeight());

        for (Weapon.Type weaponType : Weapon.Type.values()) {
            weapons.put(weaponType, new Weapon(weaponType));
        }

        unitWeapon = new UnitWeapon();
        ACTOR_WEAPON = addActor(unitWeapon);

        weapon(Weapon.Type.Stone).setAvailable(true);
        setActiveWeapon(Weapon.Type.Stone);

        getActor(ACTOR_WEAPON).setPosition(getActor(ACTOR_BASE).getWidth() - getActor(ACTOR_WEAPON).getWidth(), getActor(ACTOR_BASE).getWidth() / 2);

        ACTOR_HEALTHBAR = addActor(new ProgressBar(ProgressBar.Direction.HORIZONTAL, 10));
        getActor(ACTOR_HEALTHBAR).setWidth(getActor(ACTOR_BASE).getWidth() * 0.6f);
        getActor(ACTOR_HEALTHBAR).setPosition(getActor(ACTOR_BASE).getWidth() * 0.2f, getActor(ACTOR_BASE).getHeight());
        setMaxhp(baseHP);
        setHp(baseHP);
        activeResearch = null;

        ACTOR_RESEARCHBAR = addActor(new ProgressBar(ProgressBar.Direction.HORIZONTAL, 10));
        getActor(ACTOR_RESEARCHBAR).setWidth(getActor(ACTOR_BASE).getWidth() * 0.6f);
        getActor(ACTOR_RESEARCHBAR).setPosition(getActor(ACTOR_BASE).getWidth() * 0.2f, getActor(ACTOR_BASE).getHeight() + 20);
        getActor(ACTOR_RESEARCHBAR, ProgressBar.class).setStaticColor(new Color(0.f, 0.f, 1.f, 1.f));
        getActor(ACTOR_RESEARCHBAR).setVisible(false);

        ACTOR_COOLDOWNBAR = addActor(new ProgressBar(ProgressBar.Direction.VERTICAL, 10));
        getActor(ACTOR_COOLDOWNBAR).setHeight(getActor(ACTOR_BASE).getHeight() / 4);
        getActor(ACTOR_COOLDOWNBAR).setPosition(getActor(
                ACTOR_BASE).getWidth() / 2 - getActor(ACTOR_COOLDOWNBAR).getWidth(),
                getActor(ACTOR_BASE).getHeight() / 2 + getActor(ACTOR_COOLDOWNBAR).getHeight() / 2
        );
        getActor(ACTOR_COOLDOWNBAR, ProgressBar.class).setStaticColor(new Color(0.f, 0.f, 1.f, 1.f));
        getActor(ACTOR_COOLDOWNBAR).setVisible(true);

        update();
    }

    public Weapon weapon(Weapon.Type type) {
        if (type == null)
            return null;
        return weapons.get(type);
    }

    public Weapon getActiveWeapon() {
        return weapon(activeWeapon);
    }

    public boolean setActiveWeapon(Weapon.Type type) {
        if (weapon(type) == null)
            return false;

        if (weapon(type).isAvailable()) {
            lastWeapon = activeWeapon;
            activeWeapon = type;
            unitWeapon.select(weapon(type));
            getActor(ACTOR_WEAPON).setPosition(getActor(ACTOR_BASE).getWidth() - getActor(ACTOR_WEAPON).getWidth(), getActor(ACTOR_BASE).getWidth() / 2);
            return true;
        }
        return false;
    }

    public boolean startResearch(Weapon.Type type) {
        if (type == null)
            return false;

        if (weapon(type).levelUpAvailable() || !weapon(type).isAvailable()) {
            activeResearch = type;
            researchTime = type.getLevel(weapon(type).getLevel()).research_time;
            getActor(ACTOR_RESEARCHBAR).setVisible(true);
            getActor(ACTOR_RESEARCHBAR, ProgressBar.class).setPercentage(1);
            return true;
        }
        return false;
    }

    public void completeResearch() {
        if (activeResearch != null && researchTime < 0) {
            if (weapon(activeResearch).levelUpAvailable() && weapon(activeResearch).isAvailable())
                weapon(activeResearch).levelUp();
            weapon(activeResearch).setAvailable(true);
            activeResearch = null;
            getActor(ACTOR_RESEARCHBAR).setVisible(false);
        }
    }

    public boolean isResearching() {
        return activeResearch != null && researchTime >= 0;
    }

    public boolean isResearchReadyToComplete() {
        return getActiveResearch() != null && researchTime < 0;
    }

    public Weapon.Type getActiveResearch() {
        return activeResearch;
    }

    public float getResearchTime() {
        return Math.max(researchTime, 0);
    }

    public void fire(float angle) {
        if (!weapon(activeWeapon).isAvailable()) {
            setActiveWeapon(lastWeapon);
            lastWeapon = Weapon.Type.Stone;
        }
        Projectile projectile = getActiveWeapon().fire(angle);
        if (projectile == null)
            return;
        Animation fireAnimation = unitWeapon.getAnimation();
        if (fireAnimation != null) {
            fireAnimation.triggerAnimation();
        }
        projectile.setCenterPosition(getUnitWeapon().getActor().getCenterPoint().x, getUnitWeapon().getActor().getCenterPoint().y);
        SGL.provide(DemoModeSaveState.class).provide().addActor(projectile);
    }

    @Override
    public void update() {
    }

    @Override
    protected void onDie() {
        SGL.provide(DemoModeSaveState.class).provide().loseGame();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (isResearchReadyToComplete()) {
            completeResearch();
        }
        getActor(ACTOR_HEALTHBAR, ProgressBar.class).setPercentage(getHp() / getMaxHP());
        if (isResearching()) {
            float progress = getResearchTime() / getActiveResearch().getLevel(weapon(getActiveResearch()).getLevel()).research_time;
            getActor(ACTOR_RESEARCHBAR, ProgressBar.class).setPercentage(progress);
        }
        researchTime -= delta;
        for (Weapon weapon : weapons.values()) {
            weapon.act(delta);
        }
        getActor(ACTOR_COOLDOWNBAR).setVisible(unitWeapon.getWeapon().worthAReloadbar() && (unitWeapon.getWeapon().getReloadPercentage() > 0));
        getActor(ACTOR_COOLDOWNBAR, ProgressBar.class).setPercentage(unitWeapon.getWeapon().getReloadPercentage());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
        getActor(ACTOR_WEAPON).draw(batch, parentAlpha);
        getActor(ACTOR_HEALTHBAR).draw(batch, parentAlpha);
        getActor(ACTOR_RESEARCHBAR).draw(batch, parentAlpha);
        getActor(ACTOR_COOLDOWNBAR).draw(batch, parentAlpha);
    }

    public UnitWeapon getUnitWeapon() {
        return getActor(ACTOR_WEAPON, UnitWeapon.class);
    }

    public float angleTouchCastle(float screenX, float screenY) {
        return 180 - (float) MathUtils.angleToPoint(screenX, screenY, getUnitWeapon().getActor().getCenterPoint().x, getUnitWeapon().getCenterPoint().y);
    }
}
