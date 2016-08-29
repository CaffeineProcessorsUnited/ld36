package de.caffeineaddicted.ld36.input;

import de.caffeineaddicted.ld36.messages.ShowMenuScreenMessage;
import de.caffeineaddicted.ld36.screens.MenuScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * @author Malte Heinzelmann
 */
public class MenuInputProcessor extends SGLInputProcessor {
    private MenuScreen screen;

    public MenuInputProcessor(MenuScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screen.getMenuType() == MenuScreen.Menu.Type.HOWTOPLAY || screen.getMenuType() == MenuScreen.Menu.Type.ABOUT) {
            SGL.message(new ShowMenuScreenMessage(MenuScreen.Menu.Type.MAINMENU));
        }
        return false;
    }


}