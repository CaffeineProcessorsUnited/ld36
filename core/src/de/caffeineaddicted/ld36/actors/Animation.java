package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.sgl.SGL;

public class Animation extends Image {

    private Drawable[] frames;
    private float frameDuration = 0.1f;
    private float animationDuration;
    private float time = 0;
    Animation(Texture texture, int anzahl, int width, int height) {
        splitTexture(texture, anzahl, width, height);
        //this.animation = new Animation(0.25f, this.frames);
        //animation.setPlayMode(Animation.PlayMode.LOOP);
        setWidth(width);
        setHeight(height);
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
        return (int) Math.floor(time / frameDuration);
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
        SGL.game().log("Current Frame " + getCurrentFrame());
        while (time >= animationDuration && animationDuration > 0) {
            time -= animationDuration;
        }
        SGL.game().log("Current Frame " + getCurrentFrame());
    }


    private Drawable drawable() {
        return frames[getCurrentFrame()];
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(drawable(), batch, parentAlpha);
    }

}