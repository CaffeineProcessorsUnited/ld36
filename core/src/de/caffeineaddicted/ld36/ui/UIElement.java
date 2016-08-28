package de.caffeineaddicted.ld36.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import de.caffeineaddicted.sgl.impl.exceptions.RootElementNotProvidedException;

/**
 * @author Malte Heinzelmann
 */
public class UIElement<T extends Actor> {
    protected T rootElement;

    public UIElement(T element) {
        rootElement = element;
    }

    public T getRootElement() {
        if (rootElement == null) {
            throw new RootElementNotProvidedException(rootElement.getClass());
        }
        return rootElement;
    }

    public T build() {
        return getRootElement();
    }

    public UIElement addListener(EventListener listener) {
        getRootElement().addListener(listener);
        return this;
    }

    public UIElement setDisabled(boolean disabled) {
        ((Disableable) getRootElement()).setDisabled(disabled);
        return this;
    }
}
