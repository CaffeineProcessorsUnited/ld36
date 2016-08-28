package de.caffeineaddicted.ld36.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Pools;
import de.caffeineaddicted.ld36.actors.Actor;
import de.caffeineaddicted.ld36.actors.Projectile;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.ld36.utils.MathUtils;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.input.SGLInputProcessor;

/**
 * @author Malte Heinzelmann
 */
public class GameInputProcessor extends SGLInputProcessor {
    private GameScreen screen;
    private Vector2 lastTouched = new Vector2();
    private Actor lastOver;
    private boolean dragged = false;

    public GameInputProcessor(GameScreen screen) {
        this.screen = screen;
    }

    private float angleTouchCastle(int screenX, int screenY) {
        return 180 - (float) MathUtils.angleToPoint(screenX, screenY, screen.getCastle().getWeapon().getActor().getCenterPoint().x, screen.getCastle().getWeapon().getCenterPoint().y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouched.set(screenX, screenY);
        if (screenY >= screen.getHUD().getButtons().getY()) {
            screen.setHudAction(true);
        } else {
            screen.setHudAction(false);
            screen.getCastle().getWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screen.getHudAction()) {
            screen.setHudAction(false);
            // Only handle click if not dragged
            if (!dragged) {
                Actor clickedActor = screen.getHUD().getButtons().getActor(lastTouched);
                if (clickedActor != null) {
                    InputEvent event = Pools.obtain(InputEvent.class);
                    event.setType(InputEvent.Type.touchUp);
                    event.setStageX(screenX);
                    event.setStageY(screenY);
                    event.setPointer(pointer);
                    event.setButton(button);
                    clickedActor.fire(event);
                }
            }
        } else {
            // I don't care if you dragged me
            SGL.game().log("touchDragged" + screenX + ", " + screenY);
            float angle = angleTouchCastle(screenX, screenY);
            Projectile projectile = screen.getCastle().fire(angle);
            if (projectile != null) {
                screen.stage().addActor(projectile);
                screen.getCastle().getWeapon().getActor().setRotation(angle);
            }
        }
        dragged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screen.getHudAction()) {
            float distance = lastTouched.x - screenX;
            SGL.game().log(distance + "");
            screen.getHUD().getButtons().scrollBy(distance);
        } else {
            screen.getCastle().getWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        }
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
        //SGL.game().log(screen.getCastle().getWeapon().getActor().getCenterPoint().toString() + ", " + angle);
        dragged = true;
        lastTouched.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Actor hoveredActor = screen.getHUD().getButtons().getActor(new Vector2(screenX, screenY));
        boolean exitOld = (lastOver != null && lastOver != hoveredActor);
        boolean enterNew = (hoveredActor != null && hoveredActor != lastOver);
        if (exitOld) {
            InputEvent event = Pools.obtain(InputEvent.class);
            event.setType(InputEvent.Type.exit);
            event.setStageX(screenX);
            event.setStageY(screenY);
            lastOver.fire(event);
        }
        lastOver = hoveredActor;
        if (enterNew) {
            InputEvent event = Pools.obtain(InputEvent.class);
            event.setType(InputEvent.Type.enter);
            event.setStageX(screenX);
            event.setStageY(screenY);
            lastOver.fire(event);
        }
        screen.getCastle().getWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        return false;
    }

}