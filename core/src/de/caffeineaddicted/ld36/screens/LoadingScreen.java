package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.messages.FinishedLoadingMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

import static de.caffeineaddicted.sgl.SGL.provide;

/**
 * @author Malte Heinzelmann
 */
public class LoadingScreen extends SGLStagedScreen<LD36> {
    private float time = 0;
    private float wait_time = 0.5f;
    private float min_time = 1.5f;

    private Image background, logo;
    private ProgressBar progress;

    @Override
    public void create() {
        SGL.game().debug("Creating LoadingScreen");
        SGL.provide(Assets.class).preload();
        SGL.provide(Assets.class).finishLoading();

        super.create();
        background = new Image(provide(Assets.class).get("splash.png", Texture.class));
        stage.addActor(background);
        logo = new Image(provide(Assets.class).get("logo.png", Texture.class));
        stage.addActor(logo);
        progress = new ProgressBar(0, 100, 1, false, SGL.provide(Assets.class).get("uiskin.json", Skin.class), "default");
        stage.addActor(progress);

        SGL.provide(Assets.class).load();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (time >= wait_time) {
            if (SGL.provide(Assets.class).update()) {
                if (time >= min_time) {
                    progress.setValue(100);
                    SGL.game().debug("Finished loading assets");
                    SGL.message(new FinishedLoadingMessage());
                } else {
                    progress.setValue((time / min_time) * 100);
                }
            } else {
                progress.setValue(SGL.provide(Assets.class).getProgress() * 100);
            }
        }

        time += delta;
    }

    @Override
    public void beauty() {
        background.setWidth(stage.getViewWidth());
        background.setHeight(stage.getViewHeight());
        background.setPosition(stage.getViewOrigX(), stage.getViewOrigY());
        logo.setWidth(stage.getViewWidth() / 4);
        logo.setHeight(stage.getViewWidth() / 4);
        logo.setPosition(stage.getViewOrigX() + ((stage.getViewWidth() / 2) - (logo.getWidth() / 2)), stage.getViewOrigY() + ((stage.getViewHeight() / 2) - (logo.getHeight() / 2)));
        progress.setWidth((stage.getViewWidth() / 10) * 8);
        progress.setHeight(20);
        progress.setPosition((stage.getViewWidth() / 10), (stage.getViewWidth() / 10));
    }

    @Override
    public void dispose() {

    }
}
