package mack.jogos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {

    protected Sprite sprite;
    protected Texture texture;

    public GameObject(Texture texture, float width, float height) {
        this.texture = texture;
        this.sprite  = new Sprite(texture);
        this.sprite.setSize(width, height);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // Método comum para verificar se saiu da tela por baixo
    public boolean isOutOfBounds() {
        return sprite.getY() < -sprite.getHeight();
    }

    /** Chamado a cada frame com o tempo delta. */
    public abstract void update(float delta);
}