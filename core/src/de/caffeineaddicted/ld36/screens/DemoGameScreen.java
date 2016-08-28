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
public class DemoGameScreen extends GameScreen {

    public DemoGameScreen() {
        super(true);
    }

}
