package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import de.caffeineaddicted.ld36.actions.OnCompleteAction;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class Cloud extends Image {

    public final static int maxCloud = 9;

    public final Vector2 target = new Vector2();
    public final Vector2 initial = new Vector2();

    public Cloud() {
        this("kenney/cloud" + MathUtils.random(1, 9) + ".png");
    }

    public Cloud(String texture) {
        super(SGL.provide(Assets.class).get(texture, Texture.class));
        zindex(MathUtils.random(0, 10));
        setScale(0.4f);
    }

    private void reset() {
        setPosition(initial.x, initial.y);
        init();
    }

    public void setInitialPosition(float x, float y) {
        initial.set(x, y);
        setPosition(x, y);
    }

    @Override
    public void init() {
        super.init();
        clearActions();
        if (target != Vector2.Zero)
            addAction(action());
    }

    private Action action() {
        OnCompleteAction after = new OnCompleteAction();
        MoveToAction action = new MoveToAction();
        action.setPosition(targetX(), targetY());
        action.setDuration(duration());
        action.setInterpolation(Interpolation.linear);
        after.setAction(action);
        after.setCallback(new OnCompleteAction.OnComplete() {
            @Override
            public boolean complete(OnCompleteAction action) {
                if (action.getTarget() instanceof Cloud) {
                    Cloud cloud = (Cloud) action.getTarget();
                    cloud.reset();
                }
                return true;
            }
        });
        return after;
    }

    private float speed() {
        return MathUtils.random(1, 100) / 10;
    }

    private float duration() {
        return duration(speed());
    }

    private float duration(float speed) {
        return SGL.provide(DemoModeSaveState.class).provide().screenWidth() / speed;
    }

    private float targetX() {
        return target.set(SGL.provide(DemoModeSaveState.class).provide().screenWidth(), target.y).x;
    }

    private float targetY() {
        return target.set(target.x, getY()).y;
    }

    @Override
    public String name() {
        return "Cloud";
    }
}
