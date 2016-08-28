package de.caffeineaddicted.ld36.messages;

import de.caffeineaddicted.sgl.messages.Bundle;
import de.caffeineaddicted.sgl.messages.Message;

/**
 * @author Malte Heinzelmann
 */
public class ShowMenuScreenMessage extends Message {

    public static final String BUNDLE_MENUTYPE = "bundle_menutype";

    public ShowMenuScreenMessage(Bundle bundle) {
        super(bundle);
    }
}
