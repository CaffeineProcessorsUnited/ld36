package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import de.caffeineaddicted.sgl.SGL;

import java.util.*;

/**
 * @author Malte Heinzelmann
 */
public class Group extends Actor {

    private String name;
    private Map<String, Actor> children = new HashMap<String, Actor>();

    private boolean autoWidth = true;
    private boolean autoHeight = true;

    public String addActor(Actor a) {
        return addActor(a.getClass().getSimpleName(), a);
    }

    public String addActor(String name, Actor a) {
        if (a.parent() != null) a.parent().removeActor(a);
        int i = 0;
        while (children.containsKey(name + i)) {
            i++;
        }
        name = name + i;
        a.setName(name);
        children.put(name, a);
        a.parent(this);
        a.stage(stage());
        a.positionChanged();
        return name;
    }

    public Actor getActor(String name) {
        return children.get(name);
    }

    public <T> T getActor(String name, Class<T> type) {
        return type.cast(getActor(name));
    }

    public void removeActor(Actor a) {
        if (a == null) {
            return;
        }
        children.remove(a.getName());
        a.parent(null);
    }

    @Override
    public void act(float delta) {
        for (String n : children.keySet()) {
            children.get(n).act(delta);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())
            return;
        List<Actor> sortedChildren = new ArrayList<Actor>(children.values());
        Collections.sort(sortedChildren, new Comparator<Actor>() {
            public int compare(Actor a1, Actor a2) {
                return ((Integer) a1.zindex()).compareTo(a2.zindex());
            }
        });
        for (Actor child : sortedChildren) {
            child.draw(batch, parentAlpha);
        }
    }

    @Override
    public void clear() {
        super.clear();
        clearChildren();
    }

    private void clearChildren() {
        for (String actor : children.keySet()) {
            Actor child = children.get(actor);
            child.stage(null);
            child.parent(null);
        }
        children.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return "Group";
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        for (String n : children.keySet()) {
            children.get(n).positionChanged();
        }
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        for (String n : children.keySet()) {
            children.get(n).sizeChanged();
        }
    }

    @Override
    public float getWidth() {
        if (!autoWidth)
            return super.getWidth();
        float minX = Float.MAX_VALUE;
        for (String n : children.keySet()) {
            minX = Math.min(minX, children.get(n).getX());
        }

        float maxWidth = 0;
        for (String n : children.keySet()) {
            Actor child = children.get(n);
            if (child.getX() + child.getWidth() > minX + maxWidth) {
                maxWidth = child.getX() + child.getWidth() - minX;
            }
        }
        return maxWidth;
    }

    @Override
    public float getHeight() {
        if (!autoHeight)
            return super.getHeight();
        float minY = Float.MAX_VALUE;
        for (String n : children.keySet()) {
            minY = Math.min(minY, children.get(n).getY());
        }

        float maxHeight = 0;
        for (String n : children.keySet()) {
            Actor child = children.get(n);
            if (child.getY() + child.getHeight() > minY + maxHeight) {
                maxHeight = child.getY() + child.getHeight() - minY;
            }
        }
        return maxHeight;
    }

    public Group setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        return this;
    }

    public Group setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
        return this;
    }
}
