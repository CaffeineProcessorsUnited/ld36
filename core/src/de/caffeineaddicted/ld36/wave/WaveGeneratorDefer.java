package de.caffeineaddicted.ld36.wave;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.sgl.SGL;

/**
 * Created by Niels on 21.08.2016.
 */
public class WaveGeneratorDefer extends WaveGenerator {
    @Override
    protected void spawn() {
        if (remainingSpawns >= 0) {
            UnitEnemy enemy = new UnitEnemy(UnitEnemy.Type.getRandom());
            Vector2 pos = GameScreen.spawnPosition;
            SGL.provide(GameScreen.class).addActor(enemy);
            enemy.setPosition(pos);
            remainingSpawns -= 1;
        }
    }
}
