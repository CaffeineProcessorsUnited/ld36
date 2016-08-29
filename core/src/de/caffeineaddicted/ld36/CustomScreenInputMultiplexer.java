package de.caffeineaddicted.ld36;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Malte Heinzelmann
 */
public class CustomScreenInputMultiplexer extends SGLScreenInputMultiplexer {

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Set<SGLScreen> k = new HashSet<SGLScreen>(getProcessors().keySet());
        for (SGLScreen s : k) {
            if (s.isVisible() && getProcessors().get(s) != null) {
                Vector2 unprojected = SGL.provide(Viewport.class).unproject(new Vector2(screenX, screenY));
                getProcessors().get(s).touchDown((int) unprojected.x, (int) unprojected.y, pointer, button);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Set<SGLScreen> k = new HashSet<SGLScreen>(getProcessors().keySet());
        for (SGLScreen s : k) {
            if (s.isVisible() && getProcessors().get(s) != null) {
                Vector2 unprojected = SGL.provide(Viewport.class).unproject(new Vector2(screenX, screenY));
                getProcessors().get(s).touchUp((int) unprojected.x, (int) unprojected.y, pointer, button);
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Set<SGLScreen> k = new HashSet<SGLScreen>(getProcessors().keySet());
        for (SGLScreen s : k) {
            if (s.isVisible() && getProcessors().get(s) != null) {
                Vector2 unprojected = SGL.provide(Viewport.class).unproject(new Vector2(screenX, screenY));
                getProcessors().get(s).touchDragged((int) unprojected.x, (int) unprojected.y, pointer);
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Set<SGLScreen> k = new HashSet<SGLScreen>(getProcessors().keySet());
        for (SGLScreen s : k) {
            if (s.isVisible() && getProcessors().get(s) != null) {
                Vector2 unprojected = SGL.provide(Viewport.class).unproject(new Vector2(screenX, screenY));
                getProcessors().get(s).mouseMoved((int) unprojected.x, (int) unprojected.y);
            }
        }
        return false;
    }
}
