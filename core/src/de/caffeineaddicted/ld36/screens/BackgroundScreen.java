package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.actors.Image;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * @author Malte Heinzelmann
 */
public class BackgroundScreen extends SGLStagedScreen<LD36> {

    private Image background;
    private boolean dirty = true;

    public BackgroundScreen() {
        this(null);
    }

    public BackgroundScreen(Texture background) {
        setBackground(background);
    }

    public void setBackground(String background) {
        setBackground(SGL.provide(Assets.class).get(background, Texture.class));
    }

    public void setBackground(Texture background) {
        if (background != null) {
            stage.clear();
            this.background = new Image(background);
            dirty = true;
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor( 0.2f, 0.2f, 0.2f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        super.draw();
    }

    @Override
    public void beauty() {
        if (!dirty)
            return;
        if (this.background != null) {
            this.background.setWidth(stage.getViewWidth());
            this.background.setHeight(stage.getViewHeight());
            this.background.setPosition(0, 0);
            stage.addActor(this.background);
        }
        dirty = false;
    }

    @Override
    public void dispose() {

    }
}
