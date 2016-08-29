package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class HUD extends Entity {

    private String ACTOR_BUTTONS, ACTOR_UPGRADEFRAME, ACTOR_SELECTFRAME, ACTOR_PAUSE;

    private boolean menuOpen, dragging;
    private Vector2 autoFire = Vector2.Zero;
    private Weapon.Type currentWeaponType;

    private ArrayList<Float> projectilesToSpawn = new ArrayList<Float>();

    public HUD() {
        ACTOR_UPGRADEFRAME = addActor(new UpgradeFrame());
        ACTOR_SELECTFRAME = addActor(new SelectFrame());
        ACTOR_BUTTONS = addActor(new ScrollContainer(ScrollContainer.Direction.HORIZONTAL));
        getButtons().setMargin(10);
        for (Weapon.Type weapon : Weapon.Type.values()) {
            ImageButton button = ImageButton.createImageButton(
                    new String[]{"button_default.png", weapon.getLevel(0).preview},
                    new String[]{"button_active.png", weapon.getLevel(0).preview}
            );
            button.setUserObject(weapon);
            button.setSelection(new ImageButton.DrawableSelection() {
                @Override
                public int select(ImageButton button) {
                    return (button.isHovered() || (getWeaponType() != null && button.getUserObject() == getWeaponType())) ? 1 : 0;
                }
            });
            button.addListener(new InputListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    Weapon.Type weapon = (Weapon.Type) event.getTarget().getUserObject();
                    SGL.provide(GameScreen.class).getHUD().setWeaponType(weapon);
                    SGL.provide(GameScreen.class).getHUD().menuOpen();
                }
            });
            getButtons().add(button);
        }
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        SGL.game().log("positionChanged() =>" + getWidth() + "," + getHeight() + "," + getX() + "," + getY());
        Actor a;
        a = getActor(ACTOR_UPGRADEFRAME);
        ((UpgradeFrame) a).setAutoWidth(false);
        ((UpgradeFrame) a).setAutoHeight(false);
        a.setWidth(getWidth() * 0.3f);
        a.setHeight(getHeight() * 0.4f);
        a.setPosition(getWidth() * 0.6f, getHeight() * 0.3f);
        a = getActor(ACTOR_SELECTFRAME);
        ((SelectFrame) a).setAutoWidth(false);
        ((SelectFrame) a).setAutoHeight(false);
        a.setWidth(getWidth() * 0.2f);
        a.setHeight(getHeight() * 0.2f);
        a.setPosition(getWidth() * 0.15f, getHeight() * 0.4f);
        SGL.game().log("positionChanged() =>" + a.getWidth() + "," + a.getHeight() + "," + a.getX() + "," + a.getY());
        a = getButtons();
        getButtons().setOuterWidth(getWidth());
        getButtons().setOuterHeight(getHeight());
        getButtons().setPosition(0, getY() + getHeight() - getButtons().getContentHeight());
        SGL.game().log("positionChanged() =>" + a.getWidth() + "," + a.getHeight() + "," + a.getX() + "," + a.getY());
    }

    @Override
    public void update() {
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (autoFire.x != 0 && autoFire.y != 0) {
            fireProjectile(autoFire.x, autoFire.y);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())
            return;
        getActor(ACTOR_BUTTONS).draw(batch, parentAlpha);
        getActor(ACTOR_UPGRADEFRAME).draw(batch, parentAlpha);
        getActor(ACTOR_SELECTFRAME).draw(batch, parentAlpha);
    }

    public UpgradeFrame getUpgradeFrame() {
        return getActor(ACTOR_UPGRADEFRAME, UpgradeFrame.class);
    }

    public SelectFrame getSelectFrame() {
        return getActor(ACTOR_SELECTFRAME, SelectFrame.class);
    }

    public ScrollContainer getButtons() {
        return getActor(ACTOR_BUTTONS, ScrollContainer.class);
    }

    public void scroll(float amount) {
        getActor(ACTOR_BUTTONS, ScrollContainer.class).scrollBy(amount);
    }

    public void menuOpen() {
        menuOpen = true;
    }

    public void menuClose() {
        menuOpen = false;
        setWeaponType(null);
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void startDrag() {
        dragging = true;
    }

    public void stopDrag() {
        dragging = false;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void upgrade() {
        SGL.game().log("UPGRADE");
        if (SGL.provide(GameScreen.class).getCastle().startResearch(currentWeaponType)) {
            this.menuClose();
        }
    }

    public void select() {
        SGL.game().log("SELECT");
        if (SGL.provide(GameScreen.class).getCastle().setActiveWeapon(currentWeaponType)) {
            this.menuClose();
        }
    }

    public Weapon.Type getWeaponType() {
        return currentWeaponType;
    }

    public void setWeaponType(Weapon.Type type) {
        currentWeaponType = type;
    }

    public boolean weaponAvailable() {
        return SGL.provide(GameScreen.class).getCastle().weapon(currentWeaponType).isAvailable();
    }

    public Actor getActor(Vector2 touched) {
        Actor actor = getButtons().getActor(touched);
        if (actor != null) {
            return actor;
        }
        if (getUpgradeFrame().getButton().isInMe(touched)) {
            return getUpgradeFrame().getButton();
        }
        if (getSelectFrame().getButton().isInMe(touched)) {
            return getSelectFrame().getButton();
        }
        return null;
    }

    public void autoFire(int screenX, int screenY) {
        autoFire.set(screenX, screenY);
    }

    public void stopAutoFire() {
        autoFire.set(0, 0);
    }

    public void fireProjectile(float screenX, float screenY) {
        projectilesToSpawn.add(SGL.provide(DemoModeSaveState.class).provide().getCastle().angleTouchCastle(screenX, screenY));
    }

    public void spawnProjectiles() {
        for (Float angle: projectilesToSpawn) {
            SGL.provide(DemoModeSaveState.class).provide().getCastle().fire(angle);
        }
        projectilesToSpawn.clear();
    }
}
