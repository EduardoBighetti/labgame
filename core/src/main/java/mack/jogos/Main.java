package mack.jogos;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Texture     backgroundTexture;

    private Texture naveTexture;
    private Texture asteroideTexture;
    private Texture specialAsteroideTexture;
    private Texture sateliteTexture;
    private Texture tiroTexture;

    private Player            player;
    private Array<Tiro>       tiros;
    
    // POLIMORFISMO: Uma única lista para todos os objetos que caem!
    private Array<GameObject> alvos; 

    private float spawnTimer;

    // --- Pontuação na tela ---
    private int score;
    private BitmapFont font;

    private Music music;
    private Sound somColisao;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport    = new FitViewport(8, 5);

        naveTexture             = new Texture(Gdx.files.internal("nave.png"));
        asteroideTexture        = new Texture(Gdx.files.internal("asteroide.png"));
        specialAsteroideTexture = new Texture(Gdx.files.internal("asteroideespecial.png")); 
        sateliteTexture         = new Texture(Gdx.files.internal("inimigo.png"));   
        tiroTexture             = new Texture(Gdx.files.internal("tironave.png"));
        backgroundTexture       = new Texture(Gdx.files.internal("espaco.jpg"));

        player = new Player(naveTexture, viewport);
        tiros  = new Array<>();
        alvos  = new Array<>();

        // Configurando a fonte do texto da pontuação
        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.02f); // Escala reduzida para caber no FitViewport(8,5)

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();
        somColisao = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));

        score = 0;
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        input(delta);
        logic(delta);
        draw();
    }

    private void input(float delta) {
        player.update(delta);

        if (player.isShootPressed()) {
            tiros.add(new Tiro(tiroTexture, player.getCenterX(), player.getTopY()));
        }
    }

    private void logic(float delta) {
        float worldWidth  = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // === Sistema de Spawn Inteligente ===
        spawnTimer += delta;
        if (spawnTimer >= 1f) {
            spawnTimer = 0;
            
            // Sorteia um número de 1 a 10 para decidir o que vai cair
            int sorteio = com.badlogic.gdx.math.MathUtils.random(1, 10);
            
            if (sorteio <= 5) {
                alvos.add(new Asteroide(asteroideTexture, worldWidth, worldHeight));
            } else if (sorteio <= 7) {
                alvos.add(new SpecialAsteroide(specialAsteroideTexture, worldWidth, worldHeight));
            } else if (sorteio <= 9) {
                alvos.add(new Satelite(sateliteTexture, worldWidth, worldHeight));
            } else {
                alvos.add(new PowerUp(sateliteTexture, worldWidth, worldHeight)); // Usa textura de inimigo, mas fica verde
            }
        }

        // === Loop ÚNICO para todos os inimigos e powerups ===
        for (int i = alvos.size - 1; i >= 0; i--) {
            GameObject alvo = alvos.get(i);
            alvo.update(delta);

            if (alvo.isOutOfBounds()) {
                alvos.removeIndex(i);
                continue;
            }

            // 1. Colisão com o Player
            if (alvo.getSprite().getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())) {
                alvos.removeIndex(i);
                somColisao.play();
                
                // Usando instanceof como pedido no documento
                if (alvo instanceof PowerUp) {
                    score += 10; // Pegou o bônus!
                } else {
                    score -= 5; // Bateu no inimigo, perde ponto
                }
                continue; // Pula para o próximo alvo
            }

            // 2. Colisão com Tiros (Apenas Asteroides normais e especiais)
            for (int j = tiros.size - 1; j >= 0; j--) {
                Tiro t = tiros.get(j);
                
                if (alvo.getSprite().getBoundingRectangle().overlaps(t.getSprite().getBoundingRectangle())) {
                    
                    // Satélite e PowerUp NÃO são destruídos por tiros!
                    if (!(alvo instanceof Satelite) && !(alvo instanceof PowerUp)) {
                        alvos.removeIndex(i);
                        tiros.removeIndex(j);
                        somColisao.play();

                        // Pontuação baseada no tipo usando instanceof
                        if (alvo instanceof SpecialAsteroide) {
                            score += SpecialAsteroide.PONTOS;
                        } else if (alvo instanceof Asteroide) {
                            score += Asteroide.PONTOS;
                        }
                        break; // Tiro destruído, sai do loop de tiros
                    }
                }
            }
        }

        // === Atualiza Tiros ===
        for (int i = tiros.size - 1; i >= 0; i--) {
            Tiro t = tiros.get(i);
            t.update(delta);
            if (t.isOutOfBounds(worldHeight)) tiros.removeIndex(i);
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, w, h);
        player.draw(spriteBatch);

        // Desenha todos os alvos com um único for!
        for (GameObject alvo : alvos) {
            alvo.draw(spriteBatch);
        }
        
        for (Tiro t : tiros) {
            t.draw(spriteBatch);
        }

        // Desenha a pontuação no topo esquerdo da tela
        font.draw(spriteBatch, "Pontos: " + score, 0.2f, h - 0.2f);

        spriteBatch.end();
    }

    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        naveTexture.dispose();
        asteroideTexture.dispose();
        specialAsteroideTexture.dispose();
        sateliteTexture.dispose();
        tiroTexture.dispose();
        backgroundTexture.dispose();
        music.dispose();
        somColisao.dispose();
        font.dispose(); // Não esqueça de dar dispose na fonte!
    }
}