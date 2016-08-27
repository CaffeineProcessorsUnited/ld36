package de.caffeineaddicted.ld36.wave;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.actors.UnitEnemy;
import de.caffeineaddicted.ld36.screens.GameScreen;
import de.caffeineaddicted.sgl.SGL;


public class WaveGeneratorBulk extends WaveGenerator {

    @Override
    protected void spawn() {
        Vector2 pos = GameScreen.spawnPosition;
        while (remainingSpawns >= 0) {
            UnitEnemy enemy = new UnitEnemy(UnitEnemy.Type.getRandom());
            SGL.provide(GameScreen.class).addActor(enemy);
            enemy.setPosition(pos);
            remainingSpawns--;
        }
    }
}
