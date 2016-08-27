package de.caffeineaddicted.ld36.input;

import de.caffeineaddicted.ld36.utils.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * @author Malte Heinzelmann
 */
public class GameInputProcessor extends SGLInputProcessor {
    private GameScreen screen;
    private Vector2 lastTouch = new Vector2();

    public GameInputProcessor(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        SGL.game().log("touchDown" + lastTouch.toString());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SGL.game().log("touchDragged" + screenX + ", " + screenY);
        float angle = 180 - (float) MathUtils.angleToPoint(screenX, screenY, screen.getCastle().getWeapon().getActor().getCenterPoint().x, screen.getCastle().getWeapon().getCenterPoint().y);
        screen.stage().addActor(screen.getCastle().fire(angle));
        screen.getCastle().getWeapon().getActor().setRotation(angle);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        /*
        float dist = (lastTouch.x - screenX);
        SGL.game().log("dist: " + dist);
        if (Math.abs(dist) < 20) {
            return false;
        }
        lastTouch.set(screenX, screenY);
        float newOrigX = (screen.stage().getViewOrigX() + dist);
        SGL.game().log("newOrigX: " + newOrigX);
        if (newOrigX >= 0 && newOrigX < screen.screenWidth()) {
            screen.stage().getCamera().translate(dist, 0, 0);
            screen.stage().getCamera().update();
            SGL.game().log("camera: " + screen.stage().getCamera().position.toString());
        }
        SGL.game().log("touchDragged: " + lastTouch.toString());
        */
        float angle = 180 - (float) MathUtils.angleToPoint(screenX, screenY, screen.getCastle().getWeapon().getActor().getCenterPoint().x, screen.getCastle().getWeapon().getActor().getCenterPoint().y);
        //SGL.game().log(screen.getCastle().getWeapon().getActor().getCenterPoint().toString() + ", " + angle);
        screen.getCastle().getWeapon().getActor().setRotation(angle);
        return false;
    }

}