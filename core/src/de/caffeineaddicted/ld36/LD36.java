package de.caffeineaddicted.ld36;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.*;
import de.caffeineaddicted.ld36.messages.ExitGameMessage;
import de.caffeineaddicted.ld36.messages.FinishedLoadingMessage;
import de.caffeineaddicted.ld36.messages.ToggleFullscreenMessage;
import de.caffeineaddicted.ld36.screens.BackgroundScreen;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.screens.LoadingScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36prep.input.GlobalInputProcessor;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;

public class LD36 extends SGLGame {

	private static final ApplicationConfiguration applicationConfiguration =
            new ApplicationConfiguration()
                    .set(AttributeList.WIDTH, 1280)
                    .set(AttributeList.HEIGHT, 720)
                    .set(AttributeList.RESIZABLE, true);

    private boolean paused;

	@Override
	protected void initGame() {
	    Gdx.app.setLogLevel(Application.LOG_DEBUG);
        supply(Assets.class, new Assets());
        supply(GlobalInputProcessor.class, new GlobalInputProcessor(this));
        //supply(Viewport.class, new StretchViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT)));
        supply(Viewport.class, new FitViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT)));
        //supply(Viewport.class, new ExtendViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT)));
        supply(SpriteBatch.class, new SpriteBatch());
        supply(ShapeRenderer.class, new ShapeRenderer());
        multiplexer.addProcessor(provide(GlobalInputProcessor.class));
		SGL.registerMessageReceiver(ExitGameMessage.class, new MessageReceiver() {
			@Override
			public void receiveMessage(Message message) {
				Gdx.app.exit();
			}
		});
        SGL.registerMessageReceiver(ToggleFullscreenMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(
                            config().get(AttributeList.WIDTH),
                            config().get(AttributeList.HEIGHT)
                    );
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
            }
        });
        SGL.registerMessageReceiver(FinishedLoadingMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                supply(Music.class, provide(Assets.class).get("theme.ogg", Music.class));
                provide(Music.class).setLooping(true);
                if (!paused)
                    provide(Music.class).play();
                /*
                    While the libryry calls SGLScreen#create() in SGLScreen#<init>()
                    we have to load the screens after all Assets are loaded
                 */
                provide(SGLRootScreen.class).loadScreen(new GameScreen());
                /*
                    ... future versions of the library will fix that
                 */
                provide(SGLRootScreen.class).hideScreen(LoadingScreen.class);
                provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });

    }

	@Override
	protected void initScreens() {
        provide(SGLRootScreen.class).loadScreen(new BackgroundScreen());
        provide(SGLRootScreen.class).loadScreen(new LoadingScreen());
	}

	@Override
	protected void startGame() {
        System.out.println("Test");
        provide(SGLRootScreen.class).showScreen(BackgroundScreen.class, SGLRootScreen.ZINDEX.FAREST);
        provide(SGLRootScreen.class).showScreen(LoadingScreen.class, SGLRootScreen.ZINDEX.MID);
	}

	@Override
	public ApplicationConfiguration config() {
		return applicationConfiguration;
	}

    @Override
    public void pause() {
        paused = true;
        if (provides(Music.class)) provide(Music.class).pause();
        provide(SGLRootScreen.class).pause();
    }

    @Override
    public void resume() {
        paused = false;
        if (provides(Music.class)) provide(Music.class).play();
        provide(SGLRootScreen.class).resume();
    }
}
