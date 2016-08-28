package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Malte Heinzelmann
 */
public class ImageButton extends Image {

    private Drawable[] drawables;
    private boolean hovered;

    public ImageButton() {
        this((Drawable) null);
    }

    /**
     * Creates an image stretched, and aligned center.
     */
    public ImageButton(Texture... textures) {
        this(texA2drawA(textures));
    }

    /**
     * @param drawables May be null.
     */
    public ImageButton(Drawable... drawables) {
        super(drawables[0]);
        this.drawables = drawables;
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                hovered = true;
            }

            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                hovered = false;
            }
        });
    }

    public final static Drawable[] texA2drawA(Texture... texture) {
        Drawable[] drawables = new Drawable[texture.length];
        for (int i = 0; i < texture.length; i++) {
            drawables[i] = new TextureRegionDrawable(new TextureRegion(texture[i]));
        }
        return drawables;
    }

    private Drawable drawable() {
        if (hovered && drawables.length > 1) {
            return drawables[1];
        }
        return drawables[0];
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(drawable(), batch, parentAlpha);
    }

    /**
     * @return May be null.
     */
    public Drawable getDrawable() {
        return drawable();
    }

    @Override
    public String name() {
        return "ImageButton";
    }
}
