package de.caffeineaddicted.ld36;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.caffeineaddicted.sgl.ApplicationConfiguration;
import de.caffeineaddicted.sgl.AttributeList;
import de.caffeineaddicted.sgl.SGLGame;

public class LD36 extends SGLGame {

	private static final ApplicationConfiguration applicationConfiguration =
            new ApplicationConfiguration()
                    .set(AttributeList.WIDTH, 1280)
                    .set(AttributeList.HEIGHT, 720)
                    .set(AttributeList.RESIZABLE, false);

	@Override
	protected void initGame() {

	}

	@Override
	protected void initScreens() {

	}

	@Override
	protected void startGame() {

	}

	@Override
	public ApplicationConfiguration config() {
		return applicationConfiguration;
	}
}
