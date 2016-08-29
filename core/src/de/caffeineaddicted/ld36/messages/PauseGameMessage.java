package de.caffeineaddicted.ld36.messages;

import de.caffeineaddicted.sgl.messages.Message;

/**
 * @author Malte Heinzelmann
 */
public class PauseGameMessage extends Message {
    public PauseGameMessage() {
        this(true);
    }

    public PauseGameMessage(boolean pausescreen) {
        put("pausescreen", pausescreen);
    }
}
