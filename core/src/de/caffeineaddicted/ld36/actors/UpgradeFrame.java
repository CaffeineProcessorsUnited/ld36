package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class UpgradeFrame extends Entity {

    private BitmapFont font;
    private float margin = 10;
    private String ACTOR_BUTTON;

    public UpgradeFrame() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(24 * Gdx.graphics.getDensity());
        font = SGL.provide(FreeTypeFontGenerator.class).generateFont(fontParams);
        ACTOR_BUTTON = addActor(ImageButton.createImageButton(
                new String[]{"upgrade_default.png"},
                new String[]{"upgrade_active.png"}
        ));
    }

    public Actor getButton() {
        return getActor(ACTOR_BUTTON);
    }

    @Override
    public void sizeChanged() {
        Image image = new Image(SGL.provide(Assets.class).get("upgrade_default.png", Texture.class));
        getActor(ACTOR_BUTTON).setWidth(image.getWidth());
        getActor(ACTOR_BUTTON).setHeight(image.getHeight());
        getActor(ACTOR_BUTTON).setPosition(getWidth() / 2 - image.getWidth() / 2, 10);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setVisible(SGL.provide(DemoModeSaveState.class).provide().getHUD().isMenuOpen());
        if (!isVisible()) {
            return;
        }
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.6f);
        SGL.provide(ShapeRenderer.class).rect(getX(), getY(), getWidth(), getHeight());
        SGL.provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        writeText(batch, "AAAAAAAAAAAA", 0);
        writeText(batch, SGL.provide(GameScreen.class).getHUD().getWeaponType().name(), 1);
        writeText(batch, "CCCCCCCCCCCC", 2);
        getActor(ACTOR_BUTTON).draw(batch, parentAlpha);
    }

    public void writeText(Batch batch, String text, int i) {
        //SGL.game().log("draw " + text + " at " + (getX() + 10) + "," + textY(i));
        font.draw(
                batch,
                text,
                getX() + 10,
                textY(i));
    }

    public float textY(int i) {
        return getY() + getHeight() - margin - (i * (font.getLineHeight() + margin));
    }


}
