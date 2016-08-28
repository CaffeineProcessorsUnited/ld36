package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

/**
 * Created by felix on 28.08.16.
 */
public class AnimationRenderer extends Actor{

    @Override
    public String name() {
        return "AnimationRenderer";
    }

    private TextureRegion[] frames;
    private Animation animation;
    private int currentFrame;
    private ArrayList<Image> test;

    AnimationRenderer(Texture texture, int anzahl, int width, int height) {
        test = new ArrayList<Image>(anzahl);
        splitTexture(texture, anzahl, width, height);
        this.animation = new Animation(0.025f, this.frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        setWidth(width);
        setHeight(height);
    }

    private void splitTexture(Texture texture,int anzahl, int width, int height) {
        this.frames = new TextureRegion[anzahl];
        TextureRegion[][] tmp = TextureRegion.split(texture, width, height);
        for( int i = 0; i < anzahl; i++) {
            this.frames[i] = tmp[0][i];
            this.test.add(new Image(new TextureRegionDrawable(frames[i])));
        }
    }

    @Override
    public void act(float delta){
        this.currentFrame = animation.getKeyFrameIndex(delta);
        SGL.game().log("Current Frame " + currentFrame);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        test.get(currentFrame).draw(batch, parentAlpha);
    }

}