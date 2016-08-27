package de.caffeineaddicted.ld36prep.input;

import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.messages.ExitGameMessage;
import de.caffeineaddicted.ld36.messages.ToggleFullscreenMessage;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * @author Malte Heinzelmann
 */
public class GlobalInputProcessor extends SGLInputProcessor {
    LD36 game;

    public GlobalInputProcessor(LD36 game) {
        this.game = game;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                SGL.message(new ExitGameMessage());
                break;
            case Input.Keys.F11:
                SGL.message(new ToggleFullscreenMessage());
                break;
        }
        return false;
    }

}