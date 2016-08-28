package de.caffeineaddicted.ld36.messages;

import de.caffeineaddicted.ld36.screens.MenuScreen;
import de.caffeineaddicted.sgl.messages.Bundle;
import de.caffeineaddicted.sgl.messages.Message;

/**
 * @author Malte Heinzelmann
 */
public class ShowMenuScreenMessage extends Message {

    public static final String BUNDLE_MENUTYPE = "bundle_menutype";

    public ShowMenuScreenMessage(MenuScreen.Menu.Type type) {
        super(new Bundle().put(ShowMenuScreenMessage.BUNDLE_MENUTYPE, type));
    }
}
