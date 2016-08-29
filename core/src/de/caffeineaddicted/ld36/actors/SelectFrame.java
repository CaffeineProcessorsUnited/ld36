package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class SelectFrame extends Entity {

    private BitmapFont font;
    private String ACTOR_BUTTON;

    public SelectFrame() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(24 * Gdx.graphics.getDensity());
        font = SGL.provide(FreeTypeFontGenerator.class).generateFont(fontParams);
        ACTOR_BUTTON = addActor(ImageButton.createImageButton(
                new String[]{"button_default.png", "switch_default.png"},
                new String[]{"button_active.png", "switch_active.png"},
                new String[]{"button_default.png", "disabled_default.png"},
                new String[]{"button_active.png", "disabled_active.png"}
        ));
        getActor(ACTOR_BUTTON, ImageButton.class).setSelection(new ImageButton.DrawableSelection() {
            @Override
            public int select(ImageButton button) {
                if (isAvailable()) {
                    return (button.isHovered()) ? 1 : 0;
                } else {
                    return (button.isHovered()) ? 3 : 2;
                }
            }
        });
        Actor a = getActor(ACTOR_BUTTON);
        a.setPosition(getWidth() / 2 - a.getWidth() / 2, 10);
    }

    public Actor getButton() {
        return getActor(ACTOR_BUTTON);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setVisible(SGL.provide(DemoModeSaveState.class).provide().getHUD().isMenuOpen());
        if (!isVisible()) {
            return;
        }
        getActor(ACTOR_BUTTON).draw(batch, parentAlpha);
    }

    public boolean isAvailable() {
        return SGL.provide(DemoModeSaveState.class).provide().getHUD().weaponAvailable();
    }


}
