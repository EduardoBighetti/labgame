package mack.jogos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Satelite — obstáculo do cenário.
 * Cai igual ao Asteroide comum.
 * NÃO é destruído por Tiros.
 * TODO: definir o que acontece quando colide com o Player (dano, game over, etc.)
 */
public class Satelite extends GameObject {

    private static final float SIZE  = 1f;
    private static final float SPEED = 2f;

    public Satelite(Texture texture, float worldWidth, float worldHeight) {
        super(texture, SIZE, SIZE);
        sprite.setX(MathUtils.random(0f, worldWidth - SIZE));
        sprite.setY(worldHeight);
    }

    @Override
    public void update(float delta) {
        sprite.translateY(-SPEED * delta);
    }

    public boolean isOutOfBounds() {
        return sprite.getY() < -SIZE;
    }
}