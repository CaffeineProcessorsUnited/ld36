package de.caffeineaddicted.ld36.input;

import com.badlogic.gdx.Input;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.messages.*;
import de.caffeineaddicted.ld36.screens.AboutScreen;
import de.caffeineaddicted.ld36.screens.DemoGameScreen;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.screens.MenuScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;

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
                if (SGL.provide(SGLRootScreen.class).get(GameScreen.class).isVisible()) {
                    SGL.message(new PauseGameMessage());
                } else {
                    SGL.message(new ShowMenuScreenMessage(MenuScreen.Menu.Type.MAINMENU));
                }
                break;
            case Input.Keys.F11:
                SGL.message(new ToggleFullscreenMessage());
                break;
        }
        return false;
    }

}