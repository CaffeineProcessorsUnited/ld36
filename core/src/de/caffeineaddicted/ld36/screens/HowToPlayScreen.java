package de.caffeineaddicted.ld36.screens;

import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.actors.Image;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * @author Malte Heinzelmann
 */
public class HowToPlayScreen extends SGLStagedScreen<LD36> {

    private Image instructions;
    private boolean dirty = true;

    public HowToPlayScreen() {
        instructions = new Image("howtoplay.png");
    }

    @Override
    public void beauty() {
        if (!dirty)
            return;
        stage.clear();
        if (instructions != null) {
            instructions.setWidth(stage.getViewWidth());
            instructions.setHeight(stage.getViewHeight());
            instructions.setPosition(0, 0);
            stage.addActor(this.instructions);
        }
        dirty = false;
    }

    @Override
    public void dispose() {

    }
}
