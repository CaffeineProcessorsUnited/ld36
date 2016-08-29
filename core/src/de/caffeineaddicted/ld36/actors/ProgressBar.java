package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.caffeineaddicted.sgl.SGL;

/**
 * Created by felix on 28.08.16.
 */
public class ProgressBar extends Actor {

    private UnitBase unit;

    private int bar_r;
    private int bar_g;
    private int bar_b;
    private int bar_a = 1;

    private int bar_width = 150;
    private int bar_height = 5;

    private float health_percent;

    @Override
    public String name() {
        return "ProgressBar";
    }

    ProgressBar(UnitBase unit) {
        this.setSize(bar_width, bar_height);
        this.unit = unit;
    }

    private float getHealtPercentage() {
        return (float)(unit.getHp() / unit.getMaxhp());
    }

    @Override
    public void act(float delta) {
        health_percent = getHealtPercentage();
        if(health_percent < 0.1 ){
            bar_r = 1;
            bar_b = 0;
            bar_g = 0;
            return;
        } else if (health_percent < 0.5) {
            bar_r = 1;
            bar_g = 1;
            bar_b = 0;
            return;
        } else {
            bar_r = 0;
            bar_g = 1;
            bar_b = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShapeRenderer shaperender = SGL.provide(ShapeRenderer.class);

        batch.end();
        shaperender.setColor(0,0,0,1 * parentAlpha);
        shaperender.begin(ShapeRenderer.ShapeType.Line);
        shaperender.rect(getX(), getY(), bar_width + 2, bar_height + 2);
        shaperender.end();
        //batch.end();
        //batch.begin();
        shaperender.setColor(bar_r, bar_g, bar_b, bar_a * parentAlpha);
        shaperender.begin(ShapeRenderer.ShapeType.Filled);
        shaperender.rect(getX() + 1,getY() + 1, bar_width * health_percent, bar_height);
        shaperender.end();
        batch.begin();
    }

    @Override
    public float getWidth() {
        return (float) bar_width;
    }

    @Override
    public float getHeight() {
        return (float) bar_height;
    }

    public void setWidth(int bar_width) {
        this.bar_width = bar_width;
        setSize(bar_width, bar_height);
    }

    public void setHeight(int bar_height) {
        this.bar_height = bar_height;
        setSize(bar_width, bar_height);
    }
}
