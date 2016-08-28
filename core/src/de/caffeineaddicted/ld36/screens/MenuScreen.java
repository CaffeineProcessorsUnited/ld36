package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.caffeineaddicted.ld36.messages.ExitGameMessage;
import de.caffeineaddicted.ld36.messages.ShowMenuScreenMessage;
import de.caffeineaddicted.ld36.messages.StartGameMessage;
import de.caffeineaddicted.ld36.ui.HighscoreList;
import de.caffeineaddicted.ld36.ui.UIElement;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.Highscore;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.impl.exceptions.ProvidedObjectIsNullException;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Bundle;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Malte Heinzelmann
 */
public class MenuScreen extends SGLStagedScreen {

    public static final class Options {
        public final float marginTop;
        public final float marginLeft;
        public final float marginRight;
        public final float marginBetween;
        public final float marginTitle;
        public final Actor title;
        public final Actor background;

        public Options(float marginTop, float marginLeft, float marginRight, float marginBetween, float marginTitle, Actor title, Actor background) {
            this.marginTop = marginTop;
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            this.marginBetween = marginBetween;
            this.marginTitle = marginTitle;
            this.title = title;
            this.background = background;
        }

        public static final class Factory {
            private float marginTop = 0.05f;
            private float marginLeft = 0.3f;
            private float marginRight = 0.3f;
            private float marginBetween = 10;
            private float marginTitle = 20;
            private Actor title;
            private Actor background;

            public Options build() {
                return new Options(marginTop, marginLeft, marginRight, marginBetween, marginTitle, title, background);
            }

            public Factory marginTop(float marginTop) {
                this.marginTop = marginTop;
                return this;
            }

            public Factory marginLeft(float marginLeft) {
                this.marginLeft = marginLeft;
                return this;
            }

            public Factory marginRight(float marginRight) {
                this.marginRight = marginRight;
                return this;
            }

            public Factory marginBetween(float marginBetween) {
                this.marginBetween = marginBetween;
                return this;
            }

            public Factory marginTitle(float marginTitle) {
                this.marginTitle = marginTitle;
                return this;
            }

            public Factory title(Actor title) {
                this.title = title;
                return this;
            }

            public Factory background(Actor background) {
                this.background = background;
                return this;
            }
        }
    }

    public static final class Menu {
        public final Options options;
        public final com.badlogic.gdx.scenes.scene2d.Actor[] entries;

        public Menu(Options options, com.badlogic.gdx.scenes.scene2d.Actor... entries) {
            this.options = options;
            this.entries = entries;
        }

        public com.badlogic.gdx.scenes.scene2d.Actor get(int i) {
            return entries[i];
        }

        public <T> T get(int i, Class<T> type) {
            return type.cast(get(i));
        }

        public enum Type {
            NONE, MAINMENU, ABOUT, DEATH;
        }
    }

    public final static Map<Menu.Type, Menu> menus = new HashMap<Menu.Type, Menu>();

    private Menu.Type currentMenu = Menu.Type.NONE;

    @Override
    public void create() {
        super.create();
        SGL.game().debug("Creating MenuScreen");
        try {
            SGL.provide(SGLScreenInputMultiplexer.class).removeProcessor(this);
        } catch (ProvidedObjectIsNullException pone) {
            // don't register stage as InputProcessor
        }
        SGL.provide(InputMultiplexer.class).addProcessor(stage);
        Skin skin = SGL.provide(Assets.class).get("uiskin.json", Skin.class);
        menus.put(Menu.Type.MAINMENU, new Menu(
                new Options.Factory().title(new Label("Main Menu", skin)).background(new Image(SGL.provide(Assets.class).get("background.png", Texture.class))).build(),
                new UIElement<TextButton>(new TextButton("Start Game", skin)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new StartGameMessage());
                    }
                }).setDisabled(false).build(),
                new UIElement<TextButton>(new TextButton("Exit Game", skin)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ExitGameMessage());
                    }
                }).build()
        ));
        menus.put(Menu.Type.ABOUT, new Menu(
                new Options.Factory().title(new Label("About", skin)).build(),
                new UIElement<TextButton>(new TextButton("Start Game", skin)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new StartGameMessage());
                    }
                }).setDisabled(false).build(),
                new UIElement<TextButton>(new TextButton("Exit Game", skin)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ExitGameMessage());
                    }
                }).build()
        ));
        menus.put(Menu.Type.DEATH, new Menu(
                new Options.Factory().title(new Label("Game Over", skin)).build(),
                new UIElement<Label>(new Label("Highscore unavailable", skin)).build(),
                new UIElement<TextButton>(new TextButton("Back", skin)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ShowMenuScreenMessage(MenuScreen.Menu.Type.MAINMENU));
                    }
                }).build()
        ));
        SGL.registerMessageReceiver(ShowMenuScreenMessage.class, new MessageReceiver() {
            @Override
            public void receiveMessage(Message message) {
                if (message.hasKey(ShowMenuScreenMessage.BUNDLE_MENUTYPE)) {
                    currentMenu = message.get(ShowMenuScreenMessage.BUNDLE_MENUTYPE, Menu.Type.class);
                    dirty();
                } else {
                    SGL.game().warn("You didn't provide a menu type to display. MenuScreen will be hidden!");
                    SGL.provide(SGLRootScreen.class).hideScreen(getClass());
                }
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.game().provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.game().provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.2f);
        SGL.game().provide(ShapeRenderer.class).rect(stage.getViewOrigX(), stage.getViewOrigY(), stage.getWidth(), stage.getHeight());
        SGL.game().provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        super.draw();
    }

    @Override
    public void beauty() {
        /*
            QUICK "FIX" - PLS DON'T READ
         */
        Field field = null;
        try {
            field = SGLScreen.class.getDeclaredField("dirty");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(this, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        /*
            OK, you can continue
         */
        stage.clear();
        Menu menu = menus.get(currentMenu);
        if (currentMenu == Menu.Type.DEATH) {
            menu.get(0, Label.class).setText("Highscore: " + SGL.provide(Highscore.class).get());
        }
        if (currentMenu != Menu.Type.NONE) {
            float maxW = stage.getViewWidth();
            float maxH = stage.getViewHeight();
            float mT = maxH * menu.options.marginTop;
            float mL = maxW * menu.options.marginLeft;
            float mR = maxW * menu.options.marginRight;
            float m = menu.options.marginBetween;
            float mTitle = menu.options.marginTitle;
            float width = maxW - mL - mR;
            Actor a;
            /* Title */
            a = menu.options.title;
            float top = maxH - mT;
            if (a != null) {
                top -= a.getHeight();
                a.setPosition(maxW / 2 - a.getWidth() / 2, top);
                top -= mTitle;
                stage.addActor(a);
            }
            /* Title */
            a = menu.options.background;
            if (a != null) {
                a.setWidth(stage.getViewWidth());
                a.setHeight(stage.getViewHeight());
                a.setPosition(0, 0);
                stage.addActor(a);
            }
            for (int i = 0; i < menu.entries.length; i++) {
                a = menu.get(i);
                SGL.game().log(top + ", " + (((i == 0) ? top : (menu.get(i - 1).getY() - m)) - a.getHeight()));
                a.setWidth(width);
                a.setPosition(mL, ((i == 0) ? top : (menu.get(i - 1).getY() - m)) - a.getHeight());
                stage.addActor(a);
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
