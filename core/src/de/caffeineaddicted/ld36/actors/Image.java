package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

/**
 * @author Malte Heinzelmann
 */
public class Image extends Actor {

    private Drawable drawable;

    public Image() {
        this((Drawable) null);
    }

    /**
     * Creates an image stretched, and aligned center.
     */
    public Image(Texture texture) {
        this(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    /**
     * @param drawable May be null.
     */
    public Image(Drawable drawable) {
        setDrawable(drawable);
    }

    public final static Drawable tex2draw(Texture texture) {
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public void draw(Batch batch, float parentAlpha) {
        draw(drawable, batch, parentAlpha);
    }

    public void draw(Drawable drawable, Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (drawable instanceof TransformDrawable) {
            if (getScaleX() != 1 || getScaleY() != 1 || getRotation() != 0) {
                ((TransformDrawable) drawable).draw(batch, getX(), getY(), (getWidth() / 2), (getHeight() / 2),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                return;
            }
        }
        if (drawable != null) drawable.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    /**
     * @return May be null.
     */
    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * @param drawable May be null.
     */
    public void setDrawable(Drawable drawable) {
        if (this.drawable == drawable) return;
        this.drawable = drawable;
        setSize(drawable.getMinWidth(), drawable.getMinHeight());
        setPosition(0, 0);
    }

    @Override
    public String name() {
        return "Image";
    }
}
