package mack.jogos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class PowerUp extends GameObject {

    private static final float SIZE  = 0.8f;
    private static final float SPEED = 3f;

    public PowerUp(Texture texture, float worldWidth, float worldHeight) {
        super(texture, SIZE, SIZE);
        sprite.setX(MathUtils.random(0f, worldWidth - SIZE));
        sprite.setY(worldHeight);
    }

    @Override
    public void update(float delta) {
        sprite.translateY(-SPEED * delta);
    }
}