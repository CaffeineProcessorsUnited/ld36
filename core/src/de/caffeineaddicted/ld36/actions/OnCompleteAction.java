package de.caffeineaddicted.ld36.actions;

import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;

/**
 * @author Malte Heinzelmann
 */
public class OnCompleteAction extends DelegateAction {
    private OnComplete callback;
    private boolean finished;

    protected boolean delegate(float delta) {
        if (action.act(delta)) {
            if (finished) return true;
            if (callback == null) return true;
            if (callback.complete(this)) return true;
            if (action != null) action.restart();
        }
        return false;
    }

    /**
     * Causes the action to not repeat again.
     */
    public void finish() {
        finished = true;
    }

    public void restart() {
        super.restart();
        finished = false;
    }

    public void setCallback(OnComplete callback) {
        this.callback = callback;
    }

    public interface OnComplete {
        boolean complete(OnCompleteAction action);
    }
}
