package de.caffeineaddicted.ld36.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.caffeineaddicted.ld36.LD36;

/**
 * @author Malte Heinzelmann
 */
public class Highscore {
    private Preferences preferences;

    public Highscore() {
        preferences = Gdx.app.getPreferences(LD36.CONSTANTS.PREFERENCE_NAME);
    }

    public void set(int score) {
        if (preferences.getInteger(LD36.CONSTANTS.PREF_KEY_HIGHSCORE, LD36.CONSTANTS.PREF_DEF_HIGHSCORE) < score) {
            preferences.putInteger(LD36.CONSTANTS.PREF_KEY_HIGHSCORE, score);
            preferences.flush();
        }
    }

    public int get() {
        return preferences.getInteger(LD36.CONSTANTS.PREF_KEY_HIGHSCORE, LD36.CONSTANTS.PREF_DEF_HIGHSCORE);
    }

}
