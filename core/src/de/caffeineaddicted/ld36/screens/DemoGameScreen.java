package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.caffeineaddicted.ld36.CustomStagedScreen;
import de.caffeineaddicted.ld36.actors.UnitCastle;
import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.input.GameInputProcessor;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class DemoGameScreen extends GameScreen {

    public DemoGameScreen() {
        super(true);
    }

}
