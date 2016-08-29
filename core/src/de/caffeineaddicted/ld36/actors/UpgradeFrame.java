package de.caffeineaddicted.ld36.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.Assets;
import de.caffeineaddicted.ld36.utils.DemoModeSaveState;
import de.caffeineaddicted.ld36.weapons.Weapon;
import de.caffeineaddicted.sgl.SGL;

/**
 * @author Malte Heinzelmann
 */
public class UpgradeFrame extends Entity {

    private BitmapFont font;
    private float margin = 10;
    private String ACTOR_BUTTON;

    public UpgradeFrame() {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = Math.round(24 * Gdx.graphics.getDensity());
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("RobotoMono-Medium.ttf"));
        font = generator.generateFont(fontParams);
        ACTOR_BUTTON = addActor(ImageButton.createImageButton(
                new String[]{"upgrade_default.png"},
                new String[]{"upgrade_active.png"}
        ));
    }

    public Actor getButton() {
        return getActor(ACTOR_BUTTON);
    }

    @Override
    public void sizeChanged() {
        Image image = new Image(SGL.provide(Assets.class).get("upgrade_default.png", Texture.class));
        getActor(ACTOR_BUTTON).setWidth(image.getWidth());
        getActor(ACTOR_BUTTON).setHeight(image.getHeight());
        getActor(ACTOR_BUTTON).setPosition(getWidth() / 2 - image.getWidth() / 2, 10);
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
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        SGL.provide(ShapeRenderer.class).begin(ShapeRenderer.ShapeType.Filled);
        SGL.provide(ShapeRenderer.class).setColor(0f, 0f, 0f, 0.6f);
        SGL.provide(ShapeRenderer.class).rect(getX(), getY(), getWidth(), getHeight());
        SGL.provide(ShapeRenderer.class).end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        writeText(batch, "Weapon Information:", 0);
        writeText(batch, "Name:            " + getSelectedWeaponType().name(), 1);
        writeText(batch, "Damage:          " + getWeaponProjectile().damage, 2);
        writeText(batch, "Reload Time:     " + getSelectedWeaponTypeLevel().reload_time, 3);
        writeText(batch, "Research Time:   " + getSelectedWeaponTypeLevel().research_time, 4);
        writeText(batch, "Knockback:       " + getWeaponProjectile().knockback, 5);
        writeText(batch, "Armor Piercing:  " + getWeaponProjectile().armor_piercing, 6);
        writeText(batch, "Crit Hit Chance: " + getWeaponProjectile().crit_hit_chance, 7);
        writeText(batch, "Freeze Chance:   " + getWeaponProjectile().freeze_chance, 8);
        getActor(ACTOR_BUTTON).draw(batch, parentAlpha);
    }

    private Weapon.Type getSelectedWeaponType () {
        return SGL.provide(GameScreen.class).getHUD().getWeaponType();
    }

    private UnitCastle getCastle () {
        return SGL.provide(GameScreen.class).getCastle();
    }

    private Weapon.Type.Level getSelectedWeaponTypeLevel () {
        return getCastle().weapon(getSelectedWeaponType()).type.getLevel(getCastle().weapon(getSelectedWeaponType()).getLevel());
    }

    private Projectile.Type getWeaponProjectile () {
        return getSelectedWeaponTypeLevel().projectile;
    }

    public void writeText(Batch batch, String text, int i) {
        //SGL.game().log("draw " + text + " at " + (getX() + 10) + "," + textY(i));
        font.draw(
                batch,
                text,
                getX() + 10,
                textY(i));
    }

    public float textY(int i) {
        return getY() + getHeight() - margin - (i * (font.getLineHeight() + margin));
    }


}
