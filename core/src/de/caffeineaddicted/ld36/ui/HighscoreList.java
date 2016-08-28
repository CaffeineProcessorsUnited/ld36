package de.caffeineaddicted.ld36.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author Malte Heinzelmann
 */
public class HighscoreList extends Group {
    private final int amount;
    private final HighscoreList.Entry[] entries;
    private boolean dirty;
    private float marginBetween = 10;
    private float width = 500;

    public HighscoreList() {
        this(10);
    }

    public HighscoreList(int amount, HighscoreList.Entry... entries) {
        this.amount = amount;
        this.entries = new HighscoreList.Entry[this.amount];
        updateScores(entries);
    }

    public void updateScores(HighscoreList.Entry... entries) {
        for (int i = 0; i < entries.length && i < this.amount; i++) {
            this.entries[i] = entries[i];
        }
        dirty = true;
    }

    public void act(float delta) {
        super.act(delta);
        if (dirty) {
            clear();
            for (int i = entries.length - 1; i >= 0; i--) {
                Entry e = entries[i];
                if (e != null) {
                    e.setWidth(width);
                    e.setPosition(0, (i == entries.length - 1) ? 0 : entries[i + 1].getY() + entries[i + 1].getHeight() + marginBetween);
                    addActor(e);
                }
            }
            dirty = false;
        }
    }

    public final static class Entry extends Group {
        public final Label name, score;

        public Entry(String name, String score, Skin skin) {
            this.name = new Label(name, skin);
            addActor(this.name);
            this.score = new Label(score, skin);
            addActor(this.score);
        }

        @Override
        public void sizeChanged() {
            name.setPosition(0, 0);
            score.setPosition(getWidth() - score.getWidth(), 0);
        }
    }
}
