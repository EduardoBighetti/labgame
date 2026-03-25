package mack.jogos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Player extends GameObject {

    private static final float SIZE  = 1f;
    private static final float SPEED = 8f;

    private final FitViewport viewport;

    public Player(Texture texture, FitViewport viewport) {
        super(texture, SIZE, SIZE);
        this.viewport = viewport;
        // Posição inicial: centralizado na base da tela
        sprite.setX(viewport.getWorldWidth() / 2f - SIZE / 2f);
        sprite.setY(0.5f);
    }

    @Override
    public void update(float delta) {
        handleMovement(delta);
        clampToBounds();
    }

    private void handleMovement(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) sprite.translateX( SPEED * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) sprite.translateX(-SPEED * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) sprite.translateY( SPEED * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) sprite.translateY(-SPEED * delta);
    }

    private void clampToBounds() {
        float maxX = viewport.getWorldWidth()  - sprite.getWidth();
        float maxY = viewport.getWorldHeight() - sprite.getHeight();
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, maxX));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, maxY));
    }

    /** Retorna true no frame exato em que SPACE foi pressionado. */
    public boolean isShootPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    /** Centro horizontal da nave (ponto de origem do tiro). */
    public float getCenterX() {
        return sprite.getX() + sprite.getWidth() / 2f;
    }

    /** Topo da nave (ponto Y de origem do tiro). */
    public float getTopY() {
        return sprite.getY() + sprite.getHeight();
    }
}