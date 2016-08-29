package de.caffeineaddicted.ld36;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.ld36.input.GlobalInputProcessor;
import de.caffeineaddicted.ld36.messages.*;
import de.caffeineaddicted.ld36.screens.*;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.Highscore;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;

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
        supply(SGLScreenInputMultiplexer.class, new CustomScreenInputMultiplexer());
        multiplexer.clear();
        multiplexer.addProcessor(provide(SGLScreenInputMultiplexer.class));
        supply(DemoModeSaveState.class, new DemoModeSaveState());
        supply(Assets.class, new Assets());
        supply(GlobalInputProcessor.class, new GlobalInputProcessor(this));
        //supply(Viewport.class, new StretchViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT)));
        supply(OrthographicCamera.class, new OrthographicCamera());
        provide(OrthographicCamera.class).setToOrtho(false, config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT));
        supply(Viewport.class, new FitViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT), provide(OrthographicCamera.class)));
        //supply(Viewport.class, new ExtendViewport(config().get(AttributeList.WIDTH), config().get(AttributeList.HEIGHT), provide(OrthographicCamera.class)));
        supply(SpriteBatch.class, new SpriteBatch());
        supply(ShapeRenderer.class, new ShapeRenderer());
        supply(InputMultiplexer.class, multiplexer);
        supply(Highscore.class, new Highscore());
        provide(InputMultiplexer.class).addProcessor(provide(GlobalInputProcessor.class));
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
                supply(FreeTypeFontGenerator.class, new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf")));
                Skin skin = provide(Assets.class).get("uiskin.json", Skin.class);
                //skin.rep
                supply(Skin.class, skin);
                supply(Music.class, provide(Assets.class).get("theme.ogg", Music.class));
                provide(Music.class).setLooping(true);
                if (!paused)
                    provide(Music.class).play();
                provide(SGLRootScreen.class).showScreen(BackgroundScreen.class, SGLRootScreen.ZINDEX.FAREST);
                /*
                    While the library calls SGLScreen#create() in SGLScreen#<init>()
                    we have to load the screens after all Assets are loaded
                 */
                supply(GameScreen.class, new GameScreen());
                loadScreen(provide(GameScreen.class));
                supply(DemoGameScreen.class, new DemoGameScreen());
                loadScreen(provide(DemoGameScreen.class));
                supply(MenuScreen.class, new MenuScreen());
                loadScreen(provide(MenuScreen.class));
                supply(HowToPlayScreen.class, new HowToPlayScreen());
                loadScreen(provide(HowToPlayScreen.class));
                supply(AboutScreen.class, new AboutScreen());
                loadScreen(provide(AboutScreen.class));

                provide(GameScreen.class).reset();
                provide(DemoGameScreen.class).reset();
                /*
                    ... future versions of the library will fix that
                 */
                provide(SGLRootScreen.class).hideScreen(LoadingScreen.class);
                provide(SGLRootScreen.class).showScreen(DemoGameScreen.class, SGLRootScreen.ZINDEX.MID);
                SGL.message(new ShowMenuScreenMessage(MenuScreen.Menu.Type.MAINMENU));
            }
        });
        SGL.registerMessageReceiver(ShowMenuScreenMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                provide(SGLRootScreen.class).hideScreen(HowToPlayScreen.class);
                provide(SGLRootScreen.class).hideScreen(AboutScreen.class);
                provide(DemoGameScreen.class).setDrawHud(false);
                provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAR);
            }
        });
        SGL.registerMessageReceiver(StartGameMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                provide(GameScreen.class).setDrawHud(true);
                provide(GameScreen.class).reset();
                provide(SGLRootScreen.class).hideScreen(DemoGameScreen.class);
                provide(SGLRootScreen.class).hideScreen(MenuScreen.class);
                provide(SGLRootScreen.class).showScreen(GameScreen.class, SGLRootScreen.ZINDEX.MID);
            }
        });
        SGL.registerMessageReceiver(PauseGameMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                provide(DemoGameScreen.class).setDrawHud(true);
                provide(GameScreen.class).pause();
                SGL.message(new ShowMenuScreenMessage(MenuScreen.Menu.Type.PAUSE));
            }
        });
        SGL.registerMessageReceiver(ContinueGameMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                provide(SGLRootScreen.class).hideScreen(MenuScreen.class);
                provide(SGLRootScreen.class).get(GameScreen.class).resume();
            }
        });
        SGL.registerMessageReceiver(GameOverMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                SGL.provide(DemoModeSaveState.class).provide().setDrawHud(true);
                provide(SGLRootScreen.class).get(GameScreen.class).pause();
                provide(Highscore.class).set(message.get(GameOverMessage.POINTS, Integer.class, 0));
                provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAREST);
            }
        });
        SGL.registerMessageReceiver(ShowHowToPlayMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                SGL.provide(DemoModeSaveState.class).provide().setDrawHud(true);
                provide(SGLRootScreen.class).showScreen(DemoGameScreen.class, SGLRootScreen.ZINDEX.MID);
                provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAR);
                provide(SGLRootScreen.class).showScreen(HowToPlayScreen.class, SGLRootScreen.ZINDEX.NEAREST);
            }
        });
        SGL.registerMessageReceiver(ShowAboutMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                SGL.provide(DemoModeSaveState.class).provide().setDrawHud(false);
                provide(SGLRootScreen.class).showScreen(DemoGameScreen.class, SGLRootScreen.ZINDEX.MID);
                provide(SGLRootScreen.class).showScreen(MenuScreen.class, SGLRootScreen.ZINDEX.NEAR);
                provide(SGLRootScreen.class).showScreen(AboutScreen.class, SGLRootScreen.ZINDEX.NEAREST);
            }
        });
    }

    @Override
    protected void initScreens() {
        supply(BackgroundScreen.class, new BackgroundScreen());
        loadScreen(provide(BackgroundScreen.class));
        supply(LoadingScreen.class, new LoadingScreen());
        loadScreen(provide(LoadingScreen.class));
    }

    public void loadScreen(SGLScreen screen) {
        screen.show();
        screen.hide();
        provide(SGLRootScreen.class).loadScreen(screen);
    }

    @Override
    protected void startGame() {
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

    public float p2dp(float p) {
        return (p / config().get(AttributeList.WIDTH)) * Gdx.graphics.getWidth();
    }

    public float dp2p(float p) {
        return Gdx.graphics.getDensity();
    }

    public static class CONSTANTS {

        public final static String PREFERENCE_NAME = "profile";

        /*
        Preferences keys
         */
        public final static String PREF_KEY_HIGHSCORE = "highscore";
        public final static int PREF_DEF_HIGHSCORE = 0;
        /*
        Bundle keys
         */
        public static final String BUNDLE_SCORE = "score";
        public static final String BUNDLE_HARDCORE = "hardcore";
    }
}
