package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class ImageButton extends Image {

    private Drawable[][] drawables;
    private boolean hovered;
    private Object userObject;
    private DrawableSelection selection;

    public ImageButton() {
        this((Drawable[][]) null);
    }

    /**
     * Creates an image stretched, and aligned center.
     */
    public ImageButton(Texture[]... textures) {
        this(texA2drawA(textures));
    }

    /**
     * @param drawables May be null.
     */
    public ImageButton(Drawable[]... drawables) {
        super();
        SGL.game().log(drawables.length + " -> " + drawables[0].length);
        this.drawables = drawables;
        if (drawables != null && drawables.length > 0) {
            if (drawables[0] != null && drawables[0].length > 0) {
                setDrawable(drawables[0][0]);
            }
        }
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                hovered = true;
            }

            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                hovered = false;
            }
        });
    }

    public final static Texture[] texArray(Texture... textures) {
        return textures;
    }

    public final static Drawable[][] texA2drawA(Texture[]... texture) {
        if (texture == null)
            return null;
        Drawable[][] drawables = new Drawable[texture.length][];
        for (int i = 0; i < texture.length; i++) {
            drawables[i] = new Drawable[texture[i].length];
            for (int j = 0; j < texture[i].length; j++) {
                drawables[i][j] = new TextureRegionDrawable(new TextureRegion(texture[i][j]));
            }
        }
        return drawables;
    }

    private Drawable[] drawables() {
        if (selection != null) {
            return drawables[selection.select(this)];
        }
        if (drawables.length == 1) {
            return drawables[0];
        }
        return drawables[new DrawableSelection() {
            public int select(ImageButton button) {
                return (button.hovered) ? 1 : 0;
            }
        }.select(this)];
    }

    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < drawables().length; i++) {
            draw(drawables()[i], batch, parentAlpha);
        }
    }

    public void setSelection(DrawableSelection selection) {
        this.selection = selection;
    }

    public boolean isHovered() {
        return hovered;
    }

    @Override
    public String name() {
        return "ImageButton";
    }

    public interface DrawableSelection {
        int select(ImageButton button);
    }
}
