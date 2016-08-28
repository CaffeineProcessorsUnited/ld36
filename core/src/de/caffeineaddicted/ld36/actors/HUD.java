package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class HUD extends Entity {

    private ArrayList<String> weaponSelectors = new ArrayList<String>();
    private String ACTOR_BUTTONS, ACTOR_UPGRADEFRAME, ACTOR_NOTSELECTABLE, ACTOR_PAUSE;

    private boolean menuOpen, dragging;
    private Weapon.Type currentWeaponType;

    public HUD() {
        ACTOR_UPGRADEFRAME = addActor(new UpgradeFrame());
        ACTOR_BUTTONS = addActor(new ScrollContainer(ScrollContainer.Direction.HORIZONTAL));
        getButtons().setMargin(10);
        for (Weapon.Type weapon: Weapon.Type.values()) {
            ImageButton button = createImageButton(
                    new String[]{"button_default.png", weapon.getLevel(0).texture },
                    new String[]{"button_active.png", weapon.getLevel(0).texture }
            );
            button.setUserObject(weapon);
            button.setSelection(new ImageButton.DrawableSelection() {
                @Override
                public int select(ImageButton button) {
                    return (getWeaponType() != null && button.getUserObject() == getWeaponType()) ? 1 : 0;
                }
            });
            button.addListener(new InputListener() {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    Weapon.Type weapon = (Weapon.Type) event.getTarget().getUserObject();
                    SGL.provide(GameScreen.class).getHUD().setWeaponType(weapon);
                    SGL.provide(GameScreen.class).getHUD().menuOpen();
                }
            });
            getButtons().add(button);
        }
        /*getButtons().add(createImageButton(
                new String[]{"button_default.png" },
                new String[]{"button_active.png" }
        ));*/
    }

    private ImageButton createImageButton(String[]... names) {
        Assets assets = SGL.game().provide(Assets.class);
        Texture[][] textures = new Texture[names.length][];
        for (int i = 0; i < names.length; i++) {
            textures[i] = new Texture[names[i].length];
            for (int j = 0; j < names[i].length; j++) {
                textures[i][j] = assets.get(names[i][j], Texture.class);
            }
        }
        return createImageButton(textures);
    }

    private ImageButton createImageButton(Texture[]... textures) {
        return new ImageButton(textures);
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
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())
            return;
        getActor(ACTOR_BUTTONS).draw(batch, parentAlpha);
        getActor(ACTOR_UPGRADEFRAME).draw(batch, parentAlpha);
    }

    public Actor getUpgradeFrame() {
        return getActor(ACTOR_UPGRADEFRAME);
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

    }

    public void select() {

    }

    public void setWeaponType(Weapon.Type type) {
        currentWeaponType = type;
    }

    public Weapon.Type getWeaponType() {
        return currentWeaponType;
    }

}
