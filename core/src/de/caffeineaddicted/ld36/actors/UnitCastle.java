package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.caffeineaddicted.ld36.messages.GameOverMessage;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

public class UnitCastle extends UnitBase {
    private Weapons weapons;
    private int activeWeapon;
    private int activeResearch;
    private float researchTime;
    private UnitWeapon weapon;
    private float lastShot;


    private String ACTOR_BASE = "base";
    private String ACTOR_WEAPON = "weapon";


    public UnitCastle(UnitCastle.Weapons weapons) {
        //setBounds(getX(), getY(), getWidth(), getHeight());
        this.weapons = weapons;
        ACTOR_BASE = addTexture("TowerBase.png");
        weapon = new UnitWeapon();
        ACTOR_WEAPON = addActor(weapon);
        setSize(getActor(ACTOR_WEAPON).getWidth(), getActor(ACTOR_WEAPON).getHeight());
        activeWeapon = 0;
        setHp(1);
        activeResearch = -1;

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
            weapon.select(weapon(type).type);
            lastShot = weapon(activeWeapon).type.reload_time;
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

    public boolean isResearching(){
        return getActiveResearch() >= 0 && getResearchTime() >= 0;
    }

    public int getActiveResearch(){
        return activeResearch;
    }

    public float getResearchTime() {
        return Math.max(researchTime, 0);
    }

    public Projectile fire(float angle){
        if(lastShot > 0)
            return null;
        lastShot = weapon(activeWeapon).type.reload_time;
        Projectile projectile = getActiveWeapon().fire(angle);
        //SGL.game().debug("xx:"+getWeapon().getActor().getCenterPoint().x+",yy:"+getWeapon().getActor().getCenterPoint().y);

        projectile.setPosition(getWeapon().getActor().getCenterPoint().x, getWeapon().getActor().getCenterPoint().y);
        return projectile;
    }

    @Override
    public void update() {
    }

    @Override
    protected void onDie() {
        GameOverMessage message = new GameOverMessage();
        message.put(GameOverMessage.POINTS, SGL.provide(GameScreen.class).points);
        SGL.message(message);
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
        SGL.game().log("HP:"+getHp());
        if (activeResearch >= 0 && researchTime < 0) {
            completeResearch();
        }
        activeResearch -= delta;

        lastShot -= delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
        getActor(ACTOR_WEAPON).draw(batch, parentAlpha);

    }

    public UnitWeapon getWeapon() {
        return getActor(ACTOR_WEAPON, UnitWeapon.class);
    }

    public static enum Weapons {
        DUMMY(),
        TEST(new Weapon(Weapon.Type.TestWeapon));
        private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

        Weapons(Weapon... ws) {
            for (Weapon w : ws) {
                weapons.add(w);
            }
        }

        public int length() {
            return weapons.size() - 1;
        }

        public Weapon get(int type) {
            return weapons.get(type);
        }
    }
}
