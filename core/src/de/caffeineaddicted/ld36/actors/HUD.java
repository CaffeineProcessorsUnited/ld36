package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class HUD extends Entity {

    private ArrayList<String> weaponSelectors = new ArrayList<String>();
    private String ACTOR_BUTTONS, ACTOR_UPGRADEFRAME, ACTOR_NOTSELECTABLE, ACTOR_PAUSE;

    public HUD() {
        ACTOR_UPGRADEFRAME = addActor(new UpgradeFrame());
        ACTOR_BUTTONS = addActor(new ScrollContainer(ScrollContainer.Direction.HORIZONTAL));
        getButtons().setMargin(608);
        ImageButton pauseButton = createImageButton("pause.png", "pause_active.png");
        getButtons().add(pauseButton);
    }

    private ImageButton createImageButton(String textureUp, String textureOver) {
        Assets assets = SGL.game().provide(Assets.class);
        return createImageButton(assets.get(textureUp, Texture.class), assets.get(textureOver, Texture.class));
    }

    private ImageButton createImageButton(Texture textureUp, Texture textureOver) {
        return new ImageButton(textureUp, textureOver);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        SGL.game().log("positionChanged() =>" + getWidth() + "," + getHeight() + "," + getX() + "," + getY());
        Actor a;
        a = getActor(ACTOR_UPGRADEFRAME);
        ((UpgradeFrame) a).setAutoWidth(false);
        ((UpgradeFrame) a).setAutoHeight(false);
        a.setWidth(getWidth() * 0.4f);
        a.setHeight(getHeight() * 0.4f);
        a.setPosition(getWidth() * 0.55f, getHeight() * 0.3f);
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

}
