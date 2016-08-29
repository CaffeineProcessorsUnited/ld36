package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.utils.MathUtils;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class ScrollContainer extends Group {
    private final ArrayList<Actor> children = new ArrayList<Actor>();
    private final Direction direction;
    private float offset = 0;
    private float margin = 0;
    private float outerWidth;
    private float outerHeight;

    public ScrollContainer(Direction direction) {
        this.direction = direction;
        if (direction == Direction.VERTICAL) {
            throw new RuntimeException("Vertical ScrollContainer are not supported yet!");
        }
    }

    public void setMargin(float margin) {
        this.margin = margin;
        offsetChanged();
    }

    public void scroll(float position) {
        if (position > 0 && position != offset && position <= getContentWidth() - getOuterWidth() && getContentWidth() > getOuterWidth())
            offset = position;
        offsetChanged();
    }

    public void scrollBy(float amount) {
        scroll(offset + amount);
    }

    public int getChildrenCount() {
        return children.size();
    }

    @Override
    public float getWidth() {
        return getContentWidth();
    }

    public float getOuterWidth() {
        return outerWidth;
    }

    public void setOuterWidth(float width) {
        outerWidth = width;
    }

    public float getContentWidth() {
        float width = margin;
        if (direction == Direction.HORIZONTAL) {
            for (Actor a : children) {
                width += a.getWidth() + margin;
            }
        } else {
            for (Actor a : children) {
                if (a.getWidth() > width) {
                    width = a.getWidth();
                }
            }
        }
        return width;
    }

    @Override
    public float getHeight() {
        return getContentHeight();
    }

    public float getOuterHeight() {
        return outerHeight;
    }

    public void setOuterHeight(float height) {
        outerHeight = height;
    }

    public float getContentHeight() {
        float height = 0;
        if (direction == Direction.HORIZONTAL) {
            for (Actor a : children) {
                if (a.getHeight() > height) {
                    height = a.getHeight();
                }
            }
        } else {
            for (Actor a : children) {
                height += a.getHeight() + margin;
            }
        }
        return height;
    }

    public void add(Actor actor) {
        children.add(actor);
        actor.parent(this);
        actor.stage(stage());
        offsetChanged();
    }

    public Actor get(int i) {
        return children.get(i);
    }

    public void offsetChanged() {
        if (direction == Direction.HORIZONTAL) {
            float x = margin;
            for (Actor a : children) {
                a.setPosition(x - offset, -margin);
                //SGL.game().log(x - offset + "," + a.getY());
                x += a.getWidth() + margin;
            }
        } else {
            float y = 0;
            Actor a;
            for (int i = children.size() - 1; i >= 0; i--) {
                a = children.get(i);
                // TODO: do sth
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Actor a : children) {
            a.draw(batch, parentAlpha);
        }
    }

    /* Maybe more ArrayList functions for sorting and putting elements at certain positions. */

    public Actor getActor(Vector2 lastTouched) {
        Actor actor = null;
        for (Actor a : children) {
            //SGL.game().log(lastTouched.x + "," + lastTouched.y + "," + a.getX() + "," + a.getY() + "," + a.getTRX() + "," + a.getTRY());
            if (MathUtils.pointInRect(lastTouched.x, lastTouched.y, a.getX(), a.getY(), a.getTRX(), a.getTRY())) {
                actor = a;
                break;
            }
        }
        return actor;
    }

    public enum Direction {
        HORIZONTAL, VERTICAL
    }

}
