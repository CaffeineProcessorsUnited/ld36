package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.caffeineaddicted.ld36.CustomStage;
import de.caffeineaddicted.ld36.CustomStagedScreen;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.actors.UnitCastle;
import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.input.GameInputProcessor;
import de.caffeineaddicted.ld36.messages.FinishedLoadingMessage;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static de.caffeineaddicted.sgl.SGL.provide;
import static de.caffeineaddicted.sgl.SGL.provides;

/**
 * @author Malte Heinzelmann
 */
public class GameScreen extends CustomStagedScreen {

    private Label text;
    private UnitCastle castle;
    private ArrayList<UnitEnemy> enemies;
    private Image cannon;

    public static int groundHeight = 100;
    public static float gravity = 9.81f;

    public String ACTOR_CASTLE;

    @Override
    public void create() {
        super.create();
        SGL.game().debug("Creating GameScreen");
        registerInputListener(new GameInputProcessor(this));
        text = new Label("TEST", SGL.provide(Assets.class).get("uiskin.json", Skin.class));
        stage().addActor(text);

        castle = new UnitCastle(UnitCastle.Weapons.TEST);
        castle.setPosition(0, groundHeight);
        ACTOR_CASTLE = stage().addActor(castle);
        stage().getActor(ACTOR_CASTLE).setPosition(100, 100);

        UnitEnemy enemy = new UnitEnemy(UnitEnemy.Type.TEST);
        enemy.setPosition(stage().getViewWidth()-100,groundHeight);
        enemies = new ArrayList<UnitEnemy>();
        enemies.add(enemy);
        //stage().addActor(enemy);

        cannon = new Image(SGL.provide(Assets.class).get("cannon.png", Texture.class));
        cannon.setPosition(16, (stage().getViewHeight() / 2.f) + 16);
        cannon.setWidth(64);
        cannon.setHeight(64);
        cannon.setRotation(0);
        //stage().addActor(cannon);

        //stage().addActor(castle.fire(0));
        //stage().addActor(castle.fire(45));
        //stage().addActor(castle.fire(90));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
