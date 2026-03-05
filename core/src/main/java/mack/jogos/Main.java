package mack.jogos;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    //nave
    Texture naveTexture;
    Sprite naveSprite;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    
//da
    //asteoride 
    Texture asteroideTexture;

    //Fundo do Jogo
    Texture espacoTexture;
    Texture backgroundTexture;
    Texture dropTexture;
    Array<Sprite> dropSprites;  

    Vector2 touchPos;
    Array<Sprite> asteroideSprites;
    float dropTimer;
    createAsteroids();

    @Override
    public void create() {
        //nave creador
        naveTexture = new Texture(Gdx.files.internal("nave.png"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        naveSprite = new Sprite(naveTexture);
        naveSprite.setSize(1, 1);

        //creador asteroide

        asteroideTexture = new Texture(Gdx.files.internal("asteroide.png"));    
        //creador fundo do jogo
        espacoTexture = new Texture(Gdx.files.internal("espaco.png"));

        touchPos = new Vector2();


        
        //inicializa o array de asteroides
        asteroideSprites = new Array<Sprite>();

        

        // Prepare your application here.
        
    }

    @Override
    public void resize(int width, int height) {
        
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        // Resize your application here. The parameters represent the new window size.
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
        // Draw your application here.
    }

//metodo para criar os asteroides
    private void createAsteroids() {
        // Define o tamanho dos asteroides e do mundo
        float asteroideWidth = 1;
        float asteroideHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        //posicionamento asteroide
        Sprite asteroideSprite = new Sprite(asteroideTexture);
        asteroideSprite.setSize(asteroideWidth, asteroideHeight);
        //asteroideSprite.setX(0);
        asteroideSprite.setX(MathUtils.random(0f, worldWidth - asteroideWidth)); // Posiciona aleatoriamente no eixo X
        asteroideSprite.setY(worldHeight);
        asteroideSprites.add(asteroideSprite);

    }


    private void logic() {
    
    float worldWidth = viewport.getWorldWidth();
    float worldHeight = viewport.getWorldHeight();
    naveSprite.setX(MathUtils.clamp(naveSprite.getX(), 0, worldWidth - naveWidth()));

    float delta = Gdx.graphics.getDeltaTime(); // Tempo desde o último frame


    for (Sprite droSprite : dropSprites) {
        droSprite.translateY(-2f * delta); // Move o asteroide para baixo
    }
    
    createAsteroids(); // Cria novos asteroides

    }

    private void input() {
    float speed = 4f;

    float delta = Gdx.graphics.getDeltaTime();

    if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
        naveSprite.translateX(speed * delta);
    }

    if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
        naveSprite.translateX(-speed * delta);
    }

    if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
        naveSprite.translateY(speed * delta);
    }

    if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
        naveSprite.translateY(-speed * delta);
    }
}



    private void draw() {
//limpa a tela
    ScreenUtils.clear(Color.BLACK);
    viewport.apply();
    spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

 //desenha o fundo, a nave e os asteroides
    spriteBatch.begin();
    float worldWidth = viewport.getWorldWidth();
    float worldHeight = viewport.getWorldHeight(); 
    spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
    naveSprite.draw(spriteBatch);
    spriteBatch.end();

    for (Sprite droSprite : dropSprites) {
        droSprite.draw(spriteBatch);
    }

    spriteBatch.end();

}

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}