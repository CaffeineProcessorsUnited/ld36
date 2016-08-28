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
    }
}
