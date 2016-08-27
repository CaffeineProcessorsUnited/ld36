package de.caffeineaddicted.ld36;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.ld36.actors.Actor;
import de.caffeineaddicted.ld36.actors.Group;
import de.caffeineaddicted.sgl.ui.screens.SGLStage;

/**
 * @author Malte Heinzelmann
 */
public class CustomStage extends SGLStage {

    private Group root;

    public CustomStage() {
        super();
        init();
    }

    public CustomStage(Viewport viewport) {
        super(viewport);
        init();
    }

    public CustomStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        init();
    }

    private void init() {
        root = new Group();
        root.stage(this);
    }

    public String addActor(Actor a) {
        return root.addActor(a);
    }

    public String addActor(String name, Actor a) {
        return root.addActor(name, a);
    }

    public Actor getActor(String name) {
        return root.getActor(name);
    }

    public <T> T getActor(String name, Class<T> type) {
        return type.cast(root.getActor(name));
    }

    public void removeActor(Actor a) {
        root.removeActor(a);
    }


    public void draw () {
        Camera camera = getViewport().getCamera();
        camera.update();

        if (!root.isVisible()) return;

        if (getBatch() != null) {
            getBatch().setProjectionMatrix(camera.combined);
            getBatch().begin();
            root.draw(getBatch(), 1);
            getBatch().end();
        }
    }
}
