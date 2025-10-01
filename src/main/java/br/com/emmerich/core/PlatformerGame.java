package br.com.emmerich.core;

import br.com.emmerich.screen.GameScreen;
import br.com.emmerich.texture.TextureUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlatformerGame  extends Game {
    public SpriteBatch batch;
    public BitmapFont font; // Add this line

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Create default font
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }

        // Final cleanup
        TextureUtils.disposeAll();

        System.out.println("PlatformerGame fully disposed");
    }
}
