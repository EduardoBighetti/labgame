package mack.jogos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Asteroide comum.
 * Cai devagar. Vale 1 ponto quando destruído por um Tiro.
 */
public class Asteroide extends GameObject {

    private static final float SIZE   = 1f;
    private static final float SPEED  = 2f;
    public  static final int   PONTOS = 1;

    public Asteroide(Texture texture, float worldWidth, float worldHeight) {
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