package de.caffeineaddicted.ld36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.caffeineaddicted.ld36.input.GameInputProcessor;
import de.caffeineaddicted.ld36.input.MenuInputProcessor;
import de.caffeineaddicted.ld36.messages.ExitGameMessage;
import de.caffeineaddicted.ld36.messages.ShowHowToPlayMessage;
import de.caffeineaddicted.ld36.messages.ShowMenuScreenMessage;
import de.caffeineaddicted.ld36.messages.StartGameMessage;
import de.caffeineaddicted.ld36.ui.UIElement;
import de.caffeineaddicted.ld36.utils.Highscore;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.impl.exceptions.ProvidedObjectIsNullException;
import de.caffeineaddicted.sgl.input.SGLScreenInputMultiplexer;
import de.caffeineaddicted.sgl.messages.Message;
import de.caffeineaddicted.sgl.messages.MessageReceiver;
import de.caffeineaddicted.sgl.ui.screens.SGLRootScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLScreen;
import de.caffeineaddicted.sgl.ui.screens.SGLStagedScreen;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Malte Heinzelmann
 */
public class MenuScreen extends SGLStagedScreen {

    public final static Map<Menu.Type, Menu> menus = new HashMap<Menu.Type, Menu>();
    private Menu.Type currentMenu = Menu.Type.NONE;
    private BitmapFont font, titleFont;

    private Label.LabelStyle labelFont(Skin skin, BitmapFont font) {
        Label.LabelStyle style = skin.get(Label.LabelStyle.class);
        style.font = font;
        return style;
    }

    private TextButton.TextButtonStyle textButtonStyle(Skin skin, BitmapFont font) {
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        return style;
    }

    public Menu.Type getMenuType() {
        return currentMenu;
    }

    @Override
    public void create() {
        super.create();
        SGL.game().debug("Creating MenuScreen");
        try {
            SGL.provide(SGLScreenInputMultiplexer.class).removeProcessor(this);
        } catch (ProvidedObjectIsNullException pone) {
            // don't register stage as InputProcessor
        }
        registerInputListener(new MenuInputProcessor(this));

        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(28 * Gdx.graphics.getDensity());
        font = SGL.provide(FreeTypeFontGenerator.class).generateFont(fontParams);
        FreeTypeFontGenerator.FreeTypeFontParameter titleFontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleFontParams.size = Math.round(50 * Gdx.graphics.getDensity());
        titleFont = SGL.provide(FreeTypeFontGenerator.class).generateFont(titleFontParams);


        Label.LabelStyle labelStyle = labelFont(SGL.provide(Skin.class), font);
        Label.LabelStyle titleLabelStyle = labelFont(SGL.provide(Skin.class), titleFont);
        TextButton.TextButtonStyle textButtonStyle = textButtonStyle(SGL.provide(Skin.class), font);
        menus.put(Menu.Type.MAINMENU, new Menu(
                new Options.Factory().title(new Label("Main Menu", titleLabelStyle)).build(),
                new UIElement<TextButton>(new TextButton("Start Game", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new StartGameMessage());
                    }
                }).build(),
                new UIElement<TextButton>(new TextButton("How To Play", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ShowMenuScreenMessage(Menu.Type.HOWTOPLAY));
                        SGL.message(new ShowHowToPlayMessage());
                    }
                }).build(),
                new UIElement<TextButton>(new TextButton("About", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ShowMenuScreenMessage(Menu.Type.ABOUT));
                    }
                }).build(),
                new UIElement<TextButton>(new TextButton("Exit Game", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ExitGameMessage());
                    }
                }).build()
        ));
        menus.put(Menu.Type.HOWTOPLAY, new Menu(
                new Options.Factory().margin(0).build()
        ));
        menus.put(Menu.Type.ABOUT, new Menu(
                new Options.Factory().marginLeft(0.2f).title(new Label("About", titleLabelStyle)).build(),
                new UIElement<Label>(new Label("The MINISTRY OF SILLY WALKS proudly presents", labelStyle)).build(),
                new UIElement<Label>(new Label("a COFFEINE PROCESSORS UNITED production:", labelStyle)).build(),
                new UIElement<Label>(new Label("", labelStyle)).build(),
                new UIElement<Label>(new Label("PINK FLUFFY DEFENSE", labelStyle)).build(),
                new UIElement<Label>(new Label("", labelStyle)).build(),
                new UIElement<Label>(new Label("A game where you need to utilize the full technological strength", labelStyle)).build(),
                new UIElement<Label>(new Label("of ancient civilications to defend the precious last unicorn", labelStyle)).build(),
                new UIElement<Label>(new Label("against the invading barbaric hordes.", labelStyle)).build(),
                new UIElement<Label>(new Label("Credits:", labelStyle)).build(),
                new UIElement<Label>(new Label("\t\tFelix Richter(@felix5721): Game logic stuff", labelStyle)).build(),
                new UIElement<Label>(new Label("\t\tNiels Bernlöhr(@k0rmarun): Textures and Game logic", labelStyle)).build(),
                new UIElement<Label>(new Label("\t\tMalte Heinzelmann(@hnzlmnn): Framework, GUI and fluffy clouds", labelStyle)).build(),
                
                new UIElement<TextButton>(new TextButton("Return to main menu", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ShowMenuScreenMessage(Menu.Type.MAINMENU));
                    }
                }).setDisabled(false).build()
        ));
        menus.put(Menu.Type.DEATH, new Menu(
                new Options.Factory().title(new Label("Game Over", titleLabelStyle)).build(),
                new UIElement<Label>(new Label("Highscore unavailable", labelStyle)).build(),
                new UIElement<TextButton>(new TextButton("Back", textButtonStyle)).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        SGL.message(new ShowMenuScreenMessage(Menu.Type.MAINMENU));
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
        if (menus.get(currentMenu).options.dim) {
            ShapeRenderer shapeRenderer = SGL.game().provide(ShapeRenderer.class);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(SGL.provide(Viewport.class).getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.4f);
            shapeRenderer.rect(stage.getViewOrigX(), stage.getViewOrigY(), stage.getViewWidth(), stage.getViewHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
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

    @Override
    public void show() {
        super.show();
        SGL.provide(InputMultiplexer.class).addProcessor(stage);
    }

    @Override
    public void hide() {
        super.hide();
        SGL.provide(InputMultiplexer.class).removeProcessor(stage);
    }

    public static final class Options {
        public final float marginTop;
        public final float marginLeft;
        public final float marginRight;
        public final float marginBetween;
        public final float marginTitle;
        public final Actor title;
        public final Actor background;
        public final boolean dim;

        public Options(float marginTop, float marginLeft, float marginRight, float marginBetween, float marginTitle, Actor title, Actor background, boolean dim) {
            this.marginTop = marginTop;
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            this.marginBetween = marginBetween;
            this.marginTitle = marginTitle;
            this.title = title;
            this.background = background;
            this.dim = dim;
        }

        public static final class Factory {
            private float marginTop = 0.05f;
            private float marginLeft = 0.3f;
            private float marginRight = 0.3f;
            private float marginBetween = 10;
            private float marginTitle = 20;
            private Actor title;
            private Actor background;
            private boolean dim = true;

            public Options build() {
                return new Options(marginTop, marginLeft, marginRight, marginBetween, marginTitle, title, background, dim);
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

            public Factory margin(float margin) {
                marginTop(margin);
                marginLeft(margin);
                marginRight(margin);
                marginBetween(margin);
                marginTitle(margin);
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

            public Factory dim(boolean dim) {
                this.dim = dim;
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
            NONE, MAINMENU, ABOUT, DEATH, HOWTOPLAY;
        }
    }
}
