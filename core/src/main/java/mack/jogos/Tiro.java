package mack.jogos;

import com.badlogic.gdx.graphics.Texture;

/**
 * Projétil disparado pela nave do jogador.
 * Sobe em linha reta e destrói Asteroide e SpecialAsteroide.
 * NÃO destrói Satelite.
 */
public class Tiro extends GameObject {

    private static final float WIDTH  = 0.3f;
    private static final float HEIGHT = 0.7f;
    private static final float SPEED  = 6f;

    public Tiro(Texture texture, float originX, float originY) {
        super(texture, WIDTH, HEIGHT);
        // Centraliza o tiro no ponto de disparo da nave
        sprite.setX(originX - WIDTH / 2f);
        sprite.setY(originY);
    }

    @Override
    public void update(float delta) {
        sprite.translateY(SPEED * delta);
    }

    public boolean isOutOfBounds(float worldHeight) {
        return sprite.getY() > worldHeight;
    }
}