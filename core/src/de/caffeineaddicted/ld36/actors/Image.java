package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class Image extends Actor {

    private Drawable drawable;

    public Image () {
        this((Drawable)null);
    }

    /** Creates an image stretched, and aligned center. */
    public Image (Texture texture) {
        this(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    /** @param drawable May be null. */
    public Image (Drawable drawable) {
        setDrawable(drawable);
    }

    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (drawable instanceof TransformDrawable) {
            if (getScaleX() != 1 || getScaleY() != 1 || getRotation() != 0) {
                ((TransformDrawable)drawable).draw(batch, getX(), getY(), (getWidth() / 2),  (getHeight() / 2),
                        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                return;
            }
        }
        if (drawable != null) drawable.draw(batch, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    /** @param drawable May be null. */
    public void setDrawable (Drawable drawable) {
        if (this.drawable == drawable) return;
        this.drawable = drawable;
        SGL.game().log("centerpoint " + getName() + "//" + getClass().getSimpleName() + ":" + drawable.getMinWidth() + "," + drawable.getMinHeight());
        setSize(drawable.getMinWidth(), drawable.getMinHeight());
        setPosition(0, 0);
    }

    /** @return May be null. */
    public Drawable getDrawable () {
        return drawable;
    }

    @Override
    public String name() {
        return "Image";
    }
}
