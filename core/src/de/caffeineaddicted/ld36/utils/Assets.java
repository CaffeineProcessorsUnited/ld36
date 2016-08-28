package de.caffeineaddicted.ld36.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author Malte Heinzelmann
 */
public class Assets extends AssetManager {

    public void setup() {
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(getFileHandleResolver()));
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(getFileHandleResolver()));
    }

    public void preload() {
        load("splash.png", Texture.class);
        load("cpu.png", Texture.class);
        load("logo.png", Texture.class);
        load("uiskin.json", Skin.class);
    }

    private Color fromRGB(int r, int g, int b) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1);
    }

    private Color fromRGBA(int r, int g, int b, int a) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    private FreetypeFontLoader.FreeTypeFontLoaderParameter genFontParams(String file, int size, Color color) {
        if (color == null) color = fromRGB(255, 80, 162);
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = file;
        params.fontParameters.size = size;
        params.fontParameters.color = color;
        return params;
    }

    public void load() {
        load("missing.png", Texture.class);
        load("pause.png", Texture.class);
        load("pause_active.png", Texture.class);
        load("kenney/background.png", Texture.class);
        load("theme.ogg", Music.class);
        load("kenney/castle.png", Texture.class);
        load("kenney/cloud1.png", Texture.class);
        load("kenney/cloud2.png", Texture.class);
        load("kenney/cloud3.png", Texture.class);
        load("kenney/cloud4.png", Texture.class);
        load("kenney/cloud5.png", Texture.class);
        load("kenney/cloud6.png", Texture.class);
        load("kenney/cloud7.png", Texture.class);
        load("kenney/cloud8.png", Texture.class);
        load("kenney/cloud9.png", Texture.class);
        load("Enemy_active.png", Texture.class);
        load("Enemy_frozen.png", Texture.class);
        load("sample.png", Texture.class);
        load("sample_projectile.png", Texture.class);
        load("cannon.png", Texture.class);
        load("raw/Stone/Stone1.png", Texture.class);
        load("raw/Stone/Stone2.png", Texture.class);
        load("raw/Stone/Stone3.png", Texture.class);
        load("raw/Stone/Stone4.png", Texture.class);
        load("raw/Stone/Stone5.png", Texture.class);
        load("raw/arrow/arrow1.png", Texture.class);
        load("raw/arrow/arrow2.png", Texture.class);
        load("raw/arrow/arrow3.png", Texture.class);
        load("raw/crossbow/bolt.png", Texture.class);
        load("raw/Javelin/Javelin1.png", Texture.class);
        load("raw/Javelin/Javelin2.png", Texture.class);
        load("raw/Javelin/Javelin3.png", Texture.class);
        load("raw/shuriken/Shuriken1.png", Texture.class);
        load("raw/shuriken/Shuriken2.png", Texture.class);
        load("raw/shuriken/Shuriken3.png", Texture.class);
        load("raw/cannon/cannonball.png", Texture.class);
        load("raw/barrel/Barrel.png", Texture.class);
        load("raw/axe/axe1.png", Texture.class);
        load("raw/axe/axe2.png", Texture.class);
        load("raw/axe/axe3.png", Texture.class);
        load("raw/trident/Trident.png", Texture.class);
        load("raw/enemy_horse_rider/Combined.png", Texture.class);
        load("raw/enemy_viking/combined.png", Texture.class);
        load("raw/enemy_soldier/combined.png", Texture.class);
    }
}
