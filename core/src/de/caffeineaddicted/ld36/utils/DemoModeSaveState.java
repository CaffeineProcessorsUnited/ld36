package de.caffeineaddicted.ld36.utils;

import de.caffeineaddicted.ld36.screens.DemoGameScreen;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class DemoModeSaveState {
    private boolean demo;

    public void normal() {
        demo = false;
    }

    public void demo() {
        demo = true;
    }

    public void set(boolean demo) {
        this.demo = demo;
    }

    public boolean get() {
        return demo;
    }

    public GameScreen provide() {
        return get() ? SGL.provide(DemoGameScreen.class) : SGL.provide(GameScreen.class);
    }
}
