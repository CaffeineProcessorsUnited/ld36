package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import de.caffeineaddicted.ld36.CustomStagedScreen;
import de.caffeineaddicted.ld36.actors.*;
import de.caffeineaddicted.ld36.input.GameInputProcessor;
import de.caffeineaddicted.ld36.messages.GameOverMessage;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.ld36.wave.WaveGenerator;
import de.caffeineaddicted.ld36.wave.WaveGeneratorDefer;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Malte Heinzelmann
 */
public class GameScreen extends CustomStagedScreen {
    public static int groundHeight = 75;
    public static float gravity = 9.81f;
    public static int cloudCount = 20;
    public static Vector2 spawnPosition;
    private final boolean demo;
    public int points;
    public String ACTOR_CASTLE, ACTOR_HUD;
    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private UnitCastle castle;
    private Image cannon;
    private BitmapFont font;
    private boolean shouldReset = false;
    private WaveGenerator waveGenerator;
    private boolean hudAction;

    public GameScreen() {
        this(false);
    }

    protected GameScreen(boolean demo) {
        this.demo = demo;
    }

    @Override
    public void create() {
        super.create();
        SGL.game().debug("Creating GameScreen");
        if (!demo) {
            registerInputListener(new GameInputProcessor(this));
        }
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(24 * Gdx.graphics.getDensity());
        font = SGL.provide(FreeTypeFontGenerator.class).generateFont(fontParams);

        spawnPosition = new Vector2(stage().getViewWidth(), groundHeight);
        reset();
    }

    @Override
    public void show() {
        super.show();
        SGL.provide(BackgroundScreen.class).setBackground("kenney/background.png");
    }

    public void addActor(Actor actor) {
        stage().addActor(actor);
    }

    @Override
    public void act(float delta) {
        Iterator<Actor> iterator = deleteLater.iterator();
        while (iterator.hasNext()) {
            Actor actor = iterator.next();
            if (actor.parent() != null)
                actor.parent().removeActor(actor);
            else
                actor.remove();
            iterator.remove();
        }

        super.act(delta);
        waveGenerator.setMaxSpawn(waveGenerator.getWaveCount());
        waveGenerator.tick(delta);

        int alive = 0;
        for (Entity entity : Entity.entities) {
            if (entity instanceof UnitEnemy) {
                UnitEnemy enemy = (UnitEnemy) entity;
                if (enemy.alive())
                    alive++;
            }
        }
        if (alive == 0) {
            waveGenerator.skipToNextWave();
        }

        if (shouldReset) {
            reset();
            shouldReset = false;
        }
    }

    @Override
    public void draw() {
        super.draw();
        if (!demo) {
            stage().getBatch().begin();
            font.draw(stage().getBatch(), "Score: " + points, 10, stage().getCamera().viewportHeight - 10);
            font.draw(stage().getBatch(), "Current wave: " + waveGenerator.getWaveCount(), 10, stage().getCamera().viewportHeight - font.getCapHeight() - 20);
            font.draw(stage().getBatch(), "Time to next wave: " + (int) waveGenerator.getRemainingTime(), 10, stage().getCamera().viewportHeight - 2 * font.getCapHeight() - 30);
            stage().getBatch().end();
        }
    }

    @Override
    public void beauty() {
        /*
            QUICK "FIX" - PLS DON'T READ
         */
        Field field = null;
        try {
            field = SGLScreen.class.getDeclaredField("dirty");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(this, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        /*
            OK, you can continue
         */
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public float screenWidth() {
        return stage().getViewWidth();
    }

    public UnitCastle getCastle() {
        return stage().getActor(ACTOR_CASTLE, UnitCastle.class);
    }

    public HUD getHUD() {
        return stage().getActor(ACTOR_HUD, HUD.class);
    }

    public boolean getHudAction() {
        return hudAction;
    }

    public void setHudAction(boolean hudAction) {
        this.hudAction = hudAction;
    }

    public void loseGame() {
        if (!demo) {
            GameOverMessage message = new GameOverMessage();
            message.put(GameOverMessage.POINTS, SGL.provide(GameScreen.class).points);
            SGL.message(message);
        } else {
            shouldReset = true;
        }
    }

    public void reset() {
        SGL.game().log(isCreated() + "");
        if (!isCreated()) {
            return;
        }
        SGL.provide(DemoModeSaveState.class).set(demo);
        Iterator<Entity> iterator = Entity.entities.iterator();
        while (iterator.hasNext()) {
            Actor actor = iterator.next();
            if (actor.parent() != null)
                actor.parent().removeActor(actor);
            else
                actor.remove();
            iterator.remove();
        }

        castle = new UnitCastle(UnitCastle.Weapons.TEST);
        ACTOR_CASTLE = stage().addActor(castle);
        stage().getActor(ACTOR_CASTLE).setPosition(0, groundHeight);
        hudAction = false;
        ACTOR_HUD = stage().addActor(new HUD());
        stage().getActor(ACTOR_HUD, HUD.class).setAutoWidth(false);
        stage().getActor(ACTOR_HUD, HUD.class).setAutoHeight(false);
        stage().getActor(ACTOR_HUD).setWidth(stage().getViewWidth());
        stage().getActor(ACTOR_HUD).setHeight(stage().getViewHeight());
        stage().getActor(ACTOR_HUD).setPosition(0, 0);
        stage().getActor(ACTOR_HUD).init();
        stage().getActor(ACTOR_HUD).setVisible(!demo);

        Cloud cloud;
        for (int i = 0; i < cloudCount; i++) {
            cloud = stage().getActor(stage().addActor(new Cloud()), Cloud.class);
            cloud.setPosition(
                    (MathUtils.random(
                            - (int) cloud.getWidth(),
                            (int) (cloud.target.x - cloud.getWidth())
                    )),
                    stage().getHeight() - MathUtils.random(
                            (int) Math.ceil(cloud.getHeight()),
                            (int) stage().getHeight() / 2
                    )
            );
        }

        cannon = new Image(SGL.provide(Assets.class).get("cannon.png", Texture.class));
        cannon.setPosition(16, (stage().getViewHeight() / 2.f) + 16);
        cannon.setWidth(64);
        cannon.setHeight(64);
        cannon.setRotation(0);

        points = 0;

        waveGenerator = new WaveGeneratorDefer();
        waveGenerator.setTickDeferTimer(1);
        waveGenerator.setTickWaitTimer(60);
        waveGenerator.setCurrentWaitTimer(55);
        waveGenerator.setMinSpawn(1);
        waveGenerator.setMaxSpawn(1);
    }
}
