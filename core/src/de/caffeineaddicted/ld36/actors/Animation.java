package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Animation extends Image {

    private Drawable[] frames;
    private float frameDuration = 0.12f;
    private float animationDuration;
    private float time = 0;
    private boolean loop;
    private boolean done = true;


    Animation(Texture texture, int anzahl, int width, int height) {
        splitTexture(texture, anzahl, width, height);
        setWidth(width);
        setHeight(height);
        loop = true;
    }

    Animation(Texture texture, int anzahl, int width, int height, boolean loop) {
        this(texture, anzahl, width, height);
        this.loop = loop;
    }

    @Override
    public String name() {
        return "Animation";
    }

    public void setFrames(Drawable... frames) {
        this.frames = frames;
        animationChanged();
    }

    public int getCurrentFrame() {
        if (done && !loop) {
            return 0;
        }
        int currentFrame = (int) Math.floor(time / frameDuration);
        if (currentFrame == frames.length - 1 && !loop) {
            done = true;
        }
        return currentFrame;
    }

    private void splitTexture(Texture texture, int anzahl, int width, int height) {
        Drawable[] frames = new Drawable[anzahl];
        TextureRegion[] regions = new TextureRegion[anzahl];
        TextureRegion[][] tmp = TextureRegion.split(texture, width, height);
        for (int i = 0; i < anzahl; i++) {
            frames[i] = new TextureRegionDrawable(tmp[0][i]);
        }
        setFrames(frames);
    }

    protected void animationChanged() {
        animationDuration = frames.length * frameDuration;
    }

    @Override
    public void act(float delta) {
        time += delta;
        while (time >= animationDuration && animationDuration > 0) {
            time -= animationDuration;
        }
    }

    public void triggerAnimation() {
        done = false;
    }


    private Drawable drawable() {
        return frames[getCurrentFrame()];
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(drawable(), batch, parentAlpha);
    }

}