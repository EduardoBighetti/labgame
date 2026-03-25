package mack.jogos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Asteroide especial.
 * Cai mais rápido que o Asteroide comum.
 * Vale mais pontos quando destruído por um Tiro (valor a definir).
 */
public class SpecialAsteroide extends GameObject {

    private static final float SIZE   = 1f;
    private static final float SPEED  = 4f;   // mais rápido que Asteroide (2f)
    public  static final int   PONTOS = 3;    // TODO: ajustar valor conforme gameplay

    public SpecialAsteroide(Texture texture, float worldWidth, float worldHeight) {
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