package de.caffeineaddicted.ld36.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.caffeineaddicted.ld36.LD36;
import de.caffeineaddicted.ld36.AttributeList;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.SGLGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        SGLGame game = new LD36();
        config.width = SGL.game().config().get(AttributeList.WIDTH) + 100;
        config.height = SGL.game().config().get(AttributeList.HEIGHT);
        config.resizable = SGL.game().config().get(AttributeList.RESIZABLE);
        new LwjglApplication(game, config);
	}
}
