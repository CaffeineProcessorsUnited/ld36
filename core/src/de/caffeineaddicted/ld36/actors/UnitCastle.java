package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.HashMap;

public class UnitCastle extends UnitBase {

    private final static int baseHP = 100;
    private HashMap<Weapon.Type, Weapon> weapons = new HashMap<Weapon.Type, Weapon>();
    private Weapon.Type activeWeapon;
    private Weapon.Type activeResearch;
    private float researchTime;
    private UnitWeapon unitWeapon;
    private float lastShot;
    private String ACTOR_BASE, ACTOR_WEAPON, ACTOR_HEALTHBAR, ACTOR_RESEARCHBAR;


    public UnitCastle() {
        ACTOR_BASE = addTexture("kenney/castle.png");

        for (Weapon.Type weaponType : Weapon.Type.values()) {
            weapons.put(weaponType, new Weapon(weaponType));
            SGL.game().log("+++" + weaponType + "//" + weapon(weaponType));
        }

        unitWeapon = new UnitWeapon();
        weapon(Weapon.Type.Bow).setAvailable(true);
        setActiveWeapon(Weapon.Type.Bow);
        SGL.game().log("------" + getActiveWeapon().toString());
        unitWeapon.select(getActiveWeapon());

        ACTOR_WEAPON = addActor(unitWeapon);
        setSize(getActor(ACTOR_WEAPON).getWidth(), getActor(ACTOR_WEAPON).getHeight());

        ACTOR_HEALTHBAR = addActor(new ProgressBar());
        getActor(ACTOR_HEALTHBAR).setWidth(getActor(ACTOR_BASE).getWidth() * 0.6f);
        getActor(ACTOR_HEALTHBAR).setPosition(getActor(ACTOR_BASE).getWidth() * 0.2f, getActor(ACTOR_BASE).getHeight());
        setMaxhp(baseHP);
        setHp(baseHP);
        activeResearch = null;

        ACTOR_RESEARCHBAR = addActor(new ProgressBar());
        getActor(ACTOR_RESEARCHBAR).setWidth(getActor(ACTOR_BASE).getWidth() * 0.6f);
        getActor(ACTOR_RESEARCHBAR).setPosition(getActor(ACTOR_BASE).getWidth() * 0.2f, getActor(ACTOR_BASE).getHeight()+1);
        getActor(ACTOR_RESEARCHBAR,ProgressBar.class).setStaticColor(new Color(0.f,1.f,0.f,0.f));
        getActor(ACTOR_RESEARCHBAR).setVisible(false);

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
            activeWeapon = type;
            unitWeapon.select(weapon(type));
            lastShot = weapon(activeWeapon).type.getLevel(unitWeapon.getWeapon().getLevel()).reload_time;
            return true;
        }
        return false;
    }

    public void startResearch(Weapon.Type type) {
        if (type == null)
            return;
        if (weapon(type).isAvailable()) {
            activeResearch = type;
            researchTime = type.getLevel(weapon(type).getLevel()).research_time;
            getActor(ACTOR_RESEARCHBAR).setVisible(true);
            getActor(ACTOR_RESEARCHBAR,ProgressBar.class).setPercentage(1);
        }
    }

    public void completeResearch() {
        if (activeResearch != null && researchTime < 0) {
            weapon(activeResearch).setAvailable(true);
            weapon(activeResearch).levelUp();
            activeResearch = null;
            getActor(ACTOR_RESEARCHBAR).setVisible(false);
        }
    }

    public boolean isResearching() {
        return getActiveResearch() != null && getResearchTime() >= 0;
    }

    public boolean isResearchReadyToComplete() {
        return getActiveResearch() != null && getResearchTime() < 0;
    }

    public Weapon.Type getActiveResearch() {
        return activeResearch;
    }

    public float getResearchTime() {
        return Math.max(researchTime, 0);
    }

    public Projectile fire(float angle) {
        if (lastShot > 0)
            return null;
        lastShot = getActiveWeapon().type.getLevel(getActiveWeapon().getLevel()).reload_time;
        Projectile projectile = getActiveWeapon().fire(angle);
        Animation fireAnimation = unitWeapon.getAnimation();
        if (fireAnimation != null) {
            fireAnimation.triggerAnimation();
        }
        //SGL.game().debug("xx:"+getUnitWeapon().getActor().getCenterPoint().x+",yy:"+getUnitWeapon().getActor().getCenterPoint().y);

        projectile.setCenterPosition(getUnitWeapon().getActor().getCenterPoint().x, getUnitWeapon().getActor().getCenterPoint().y);
        return projectile;
    }

    @Override
    public void update() {
    }

    @Override
    protected void onDie() {
        SGL.provide(DemoModeSaveState.class).provide().loseGame();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        Actor a = getActor(ACTOR_WEAPON);
        a.setPosition(getWidth() - a.getWidth(), getHeight() - a.getHeight());
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isResearchReadyToComplete()) {
            completeResearch();
        }
        getActor(ACTOR_HEALTHBAR, ProgressBar.class).setPercentage(getHp() / getMaxhp());
        if(getActiveResearch() != null){
            getActor(ACTOR_RESEARCHBAR, ProgressBar.class).setPercentage(getResearchTime() / getActiveResearch().getLevel(weapon(getActiveResearch()).getLevel()).research_time);
        }
        researchTime -= delta;
        lastShot -= delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
        getActor(ACTOR_WEAPON).draw(batch, parentAlpha);
        getActor(ACTOR_HEALTHBAR).draw(batch, parentAlpha);
    }

    public UnitWeapon getUnitWeapon() {
        return getActor(ACTOR_WEAPON, UnitWeapon.class);
    }
}
