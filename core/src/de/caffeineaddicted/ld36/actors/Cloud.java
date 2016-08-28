package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
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

    public Cloud() {
        this("kenney/cloud" + MathUtils.random(1, 9) + ".png");
    }

    public Cloud(String texture) {
        super(SGL.provide(Assets.class).get(texture, Texture.class));
        setScale(0.4f);
        SGL.game().log("Beautiful cloud: " + texture);
        positionChanged();
    }

    @Override
    public void positionChanged() {
        super.positionChanged();
        clearActions();
        addAction(action());
    }

    private Action action() {
        RepeatAction repeat = new RepeatAction();
        MoveToAction action = new MoveToAction();
        action.setPosition(targetX(), targetY());
        action.setDuration(duration());
        repeat.setAction(action);
        repeat.setCount(RepeatAction.FOREVER);
        return repeat;
    }

    private float speed() {
        return MathUtils.random(2, 10);
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
