package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.caffeineaddicted.ld36.CustomStagedScreen;
import de.caffeineaddicted.ld36.actors.Actor;
import de.caffeineaddicted.ld36.actors.Entity;
import de.caffeineaddicted.ld36.actors.UnitCastle;
import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.input.GameInputProcessor;
import de.caffeineaddicted.ld36.utils.Assets;
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

    private Label text;
    private Label labelScore;
    private Label labelWave;
    private Label labelTime;
    private UnitCastle castle;
    private ArrayList<UnitEnemy> enemies;
    private Image cannon;

    public int points;
    public static int groundHeight = 100;
    public static float gravity = 9.81f;
    public static Vector2 spawnPosition;

    public String ACTOR_CASTLE;
    public ArrayList<Actor> deleteLater = new ArrayList<Actor>();
    private WaveGenerator waveGenerator;

    private final boolean demo;

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
        registerInputListener(new GameInputProcessor(this));
        text = new Label("TEST", SGL.provide(Assets.class).get("uiskin.json", Skin.class));
        labelScore = new Label("A", SGL.provide(Assets.class).get("uiskin.json", Skin.class));
        labelWave = new Label("B", SGL.provide(Assets.class).get("uiskin.json", Skin.class));
        labelTime = new Label("C", SGL.provide(Assets.class).get("uiskin.json", Skin.class));
        stage().addActor(text);
        stage().addActor(labelScore);
        stage().addActor(labelWave);
        stage().addActor(labelTime);

        spawnPosition = new Vector2(stage().getViewWidth()-100,groundHeight);

        castle = new UnitCastle(UnitCastle.Weapons.TEST);
        castle.setPosition(0, groundHeight);
        ACTOR_CASTLE = stage().addActor(castle);
        stage().getActor(ACTOR_CASTLE).setPosition(100, 100);

        UnitEnemy enemy = new UnitEnemy(UnitEnemy.Type.TEST);
        enemy.setPosition(stage().getViewWidth()-100,groundHeight);
        enemies = new ArrayList<UnitEnemy>();
        enemies.add(enemy);
        stage().addActor(enemy);

        cannon = new Image(SGL.provide(Assets.class).get("cannon.png", Texture.class));
        cannon.setPosition(16, (stage().getViewHeight() / 2.f) + 16);
        cannon.setWidth(64);
        cannon.setHeight(64);
        cannon.setRotation(0);

        points = 0;

        waveGenerator = new WaveGeneratorDefer();
        waveGenerator.setTickDeferTimer(1);
        waveGenerator.setTickWaitTimer(60);
        waveGenerator.setCurrentWaitTimer(50);
        waveGenerator.setMinSpawn(1);
        waveGenerator.setMaxSpawn(1);
    }

    public void addActor(Actor actor){
        stage().addActor(actor);
    }

    @Override
    public void act(float delta) {
        Iterator<Actor> iterator = deleteLater.iterator();
        while (iterator.hasNext()){
            Actor actor = iterator.next();
            actor.parent().removeActor(actor);
            iterator.remove();
        }

        super.act(delta);
        waveGenerator.setMaxSpawn(waveGenerator.getWaveCount());
        waveGenerator.tick(delta);

        int alive = 0;
        for (Entity entity : Entity.entities) {
            if (entity instanceof UnitEnemy) {
                UnitEnemy enemy = (UnitEnemy)entity;
                if (enemy.alive())
                    alive++;
            }
        }
        if (alive == 0) {
            waveGenerator.skipToNextWave();
        }

        labelScore.setText("Score: "+points);
        labelWave.setText("Current wave: "+waveGenerator.getWaveCount());
        labelTime.setText("Time to next wave: " + (int) waveGenerator.getRemainingTime());
    }

    @Override
    public void draw() {
        super.draw();
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
        text.setPosition(stage().getViewOrigX() + 100, stage().getViewOrigY() + 100);
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
}
