package de.caffeineaddicted.ld36;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.impl.exceptions.ProvidedObjectIsNullException;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

/**
 * @author Malte Heinzelmann
 */
public class CustomStagedScreen extends SGLStagedScreen<LD36> {

    @Override
    public void beauty() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void create() {
        Viewport viewport = null;
        SpriteBatch batch = null;
        try {
            viewport = SGL.provide(Viewport.class);
            batch = SGL.provide(SpriteBatch.class);
        } catch (ProvidedObjectIsNullException pone) {
            // either viewport or batch is null
        }
        if (viewport == null) {
            stage = new CustomStage();
        } else {
            if (batch == null) {
                stage = new CustomStage(viewport);
            } else {
                stage = new CustomStage(viewport, batch);
            }
        }
        camera = stage.getCamera();
        SGL.game().log(camera.project(new Vector3(0, 0, 0)).toString());
        SGL.game().log(camera.project(new Vector3(100, 0, 0)).toString());
        SGL.game().log(viewport.project(new Vector2(0, 0)).toString());
        SGL.game().log(viewport.project(new Vector2(100, 0)).toString());
        try {
            SGL.provide(SGLScreenInputMultiplexer.class).addProcessor(this, stage);
        } catch (ProvidedObjectIsNullException pone) {
            // don't register stage as InputProcessor
        }
    }

    public CustomStage stage() {
        return (CustomStage) stage;
    }
}
