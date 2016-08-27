package de.caffeineaddicted.ld36.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author Malte Heinzelmann
 */
public class Assets extends AssetManager {

    public void preload() {
        load("background.png", Texture.class);
        load("cpu.png", Texture.class);
        load("logo.png", Texture.class);
        load("uiskin.json", Skin.class);
    }

    public void load() {
        load("theme.ogg", Music.class);
        load("TowerBase.png", Texture.class);
        load("Enemy_active.png", Texture.class);
        load("Enemy_frozen.png", Texture.class);
        load("sample.png", Texture.class);
        load("sample_projectile.png", Texture.class);
    }
}
