package de.caffeineaddicted.ld36.screens;

import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.actors.Image;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * @author Malte Heinzelmann
 */
public class AboutScreen extends SGLStagedScreen<LD36> {

    private Image about;
    private boolean dirty = true;

    public AboutScreen() {
        about = new Image("about.png");
    }

    @Override
    public void beauty() {
        if (!dirty)
            return;
        stage.clear();
        if (about != null) {
            about.setWidth(stage.getViewWidth());
            about.setHeight(stage.getViewHeight());
            about.setPosition(0, 0);
            stage.addActor(this.about);
        }
        dirty = false;
    }

    @Override
    public void dispose() {

    }
}
