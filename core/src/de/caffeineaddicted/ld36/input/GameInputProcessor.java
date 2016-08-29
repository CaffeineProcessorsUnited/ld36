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
        return 180 - (float) MathUtils.angleToPoint(screenX, screenY, screen.getCastle().getUnitWeapon().getActor().getCenterPoint().x, screen.getCastle().getUnitWeapon().getCenterPoint().y);
    }

    private boolean inUpgradeFrame(int screenX, int screenY) {
        return screen.getHUD().getUpgradeFrame().isInMe(new Vector2(screenX, screenY));
    }

    private boolean inSelectFrame(int screenX, int screenY) {
        return screen.getHUD().getSelectFrame().isInMe(new Vector2(screenX, screenY));
    }

    private boolean inTopBar(int screenX, int screenY) {
        return (screenY >= screen.getHUD().getButtons().getY());
    }

    private boolean inHUD(int screenX, int screenY) {
        return (inUpgradeFrame(screenX, screenY) || inSelectFrame(screenX, screenY) || inTopBar(screenX, screenY));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouched.set(screenX, screenY);
        if (screen.getHUD().isMenuOpen()) {
            if (!inHUD(screenX, screenY)) {
                screen.getHUD().menuClose();
            }
        } else if (inTopBar(screenX, screenY)) {
            screen.getHUD().startDrag();
        } else {
            screen.getHUD().stopDrag();
            screen.getCastle().getUnitWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screen.getHUD().isMenuOpen() && !screen.getHUD().isDragging()) {
            if (inUpgradeFrame(screenX, screenY)) {
                screen.getHUD().upgrade();
            } else if (inSelectFrame(screenX, screenY)) {
                screen.getHUD().select();
            } else {
                screen.getHUD().menuClose();
            }
        } else if (screen.getHUD().isDragging()) {
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
            float angle = angleTouchCastle(screenX, screenY);
            Projectile projectile = screen.getCastle().fire(angle);
            if (projectile != null) {
                screen.stage().addActor(projectile);
                screen.getCastle().getUnitWeapon().getActor().setRotation(angle);
            }
        }
        screen.getHUD().stopDrag();
        dragged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screen.getHUD().isDragging()) {
            float distance = lastTouched.x - screenX;
            SGL.game().log(distance + "");
            screen.getHUD().getButtons().scrollBy(distance);
        } else {
            screen.getCastle().getUnitWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        }
        dragged = true;
        lastTouched.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Actor hoveredActor = screen.getHUD().getActor(new Vector2(screenX, screenY));
        if (hoveredActor != null) SGL.game().log(hoveredActor.toString());
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
        screen.getCastle().getUnitWeapon().getActor().setRotation(angleTouchCastle(screenX, screenY));
        return false;
    }

}