package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class UpgradeFrame extends Entity {

    private BitmapFont font;
    private float margin = 10;

    public UpgradeFrame() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(24 * Gdx.graphics.getDensity());
        font = SGL.provide(FreeTypeFontGenerator.class).generateFont(fontParams);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setVisible(SGL.provide(DemoModeSaveState.class).provide().getHudAction());
        if (!isVisible()) {
            return;
        }/*
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.game().provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.game().provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.2f);
        SGL.game().provide(ShapeRenderer.class).rect(getX(), getY(), getWidth(), getHeight());
        SGL.game().provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);*/
        writeText(batch, "A", 0);
        writeText(batch, "B", 1);
        writeText(batch, "C", 2);
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
