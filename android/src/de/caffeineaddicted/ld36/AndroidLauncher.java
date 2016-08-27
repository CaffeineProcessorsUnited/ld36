package de.caffeineaddicted.ld36;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.impl.exceptions.GameNotInitializedException;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		try {
			SGL.game();
		} catch(GameNotInitializedException gnie) {
			new LD36();
		}
		initialize(SGL.game(), config);
	}
}
