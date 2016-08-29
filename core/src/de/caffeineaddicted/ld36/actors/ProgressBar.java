package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.sgl.SGL;

/**
 * Created by felix on 28.08.16.
 */
public class ProgressBar extends Actor {

    private float percentage;
    private Color color = new Color(0, 0, 0, 1);
    private boolean staticColor = false;

    ProgressBar() {
        this(10);
    }

    ProgressBar(int height) {
        setHeight(height);
    }

    @Override
    public String name() {
        return "ProgressBar";
    }

    public void setStaticColor(Color color) {
        this.color = color;
        staticColor = true;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void percentageColor() {
        if (staticColor)
            return;
        if (getPercentage() < 0.1) {
            color.r = 1;
            color.g = 0;
            color.b = 0;
        } else if (getPercentage() < 0.5) {
            color.r = 1;
            color.g = 1;
            color.b = 0;
        } else {
            color.r = 0;
            color.g = 1;
            color.b = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())
            return;
        ShapeRenderer shaperender = SGL.provide(ShapeRenderer.class);
        shaperender.setProjectionMatrix(SGL.provide(Viewport.class).getCamera().combined);

        batch.end();
        shaperender.setColor(0, 0, 0, 1 * parentAlpha);
        shaperender.begin(ShapeRenderer.ShapeType.Line);
        shaperender.rect(getX(), getY(), getWidth(), getHeight());
        shaperender.end();
        //batch.end();
        //batch.begin();
        percentageColor();
        shaperender.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        shaperender.begin(ShapeRenderer.ShapeType.Filled);
        shaperender.rect(getX() + 1, getY() + 1, getWidth() * getPercentage() - 2, getHeight() - 2);
        shaperender.end();
        batch.begin();
    }

}
