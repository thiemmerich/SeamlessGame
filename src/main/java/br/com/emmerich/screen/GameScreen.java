package br.com.emmerich.screen;

import br.com.emmerich.character.Creature;
import br.com.emmerich.character.Elf;
import br.com.emmerich.character.Human;
import br.com.emmerich.core.PlatformerGame;
import br.com.emmerich.core.SeamlessWorld;
import br.com.emmerich.core.WorldPosition;
import br.com.emmerich.texture.TextureUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

    private final PlatformerGame game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final SeamlessWorld world;
    private final Creature player;

    private float fps = 0;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;

    public GameScreen(PlatformerGame game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        // Create seamless world
        world = new SeamlessWorld();

        // Create player
        player = new Elf("Human", 64, 64);

        // Connect everything
        world.setPlayer(player);
        world.setCamera(camera);
    }

    @Override
    public void render(float delta) {
        long currentTime = System.nanoTime();
        frameCount++;

        // Update FPS counter every second
        if (currentTime - lastFpsUpdate > 1000000000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsUpdate = currentTime;
        }

        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        handleInput(delta);

        // Update world
        long updateStart = System.nanoTime();
        world.update(delta);
        long updateTime = System.nanoTime() - updateStart;

        // Update camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw everything
        long renderStart = System.nanoTime();
        batch.begin();
        world.render(batch);
        drawUI();
        drawPerformanceInfo(updateTime, System.nanoTime() - renderStart);
        batch.end();

    }

    private void drawPerformanceInfo(long updateTime, long renderTime) {
        // Get memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();

        game.font.draw(batch, "FPS: " + (int) fps, 10, 400);
        game.font.draw(batch, "Update: " + updateTime / 1000 + "μs", 10, 380);
        game.font.draw(batch, "Render: " + renderTime / 1000 + "μs", 10, 360);
        game.font.draw(batch, "Memory: " + (usedMemory / 1024 / 1024) + "MB / " + (maxMemory / 1024 / 1024) + "MB", 10, 340);
        game.font.draw(batch, "Textures cached: " + getTextureCacheSize(), 10, 320);

        WorldPosition pos = player.getWorldPosition();
        if (pos != null) {
            game.font.draw(batch, "Room: (" + pos.roomX + "," + pos.roomY + ")", 10, 580);
            game.font.draw(batch, "Local: " + (int) pos.localX + ", " + (int) pos.localY, 10, 560);
        }
    }

    private int getTextureCacheSize() {
        // You'll need to make textureCache public or add a getter in TextureUtils
        // return TextureUtils.getCacheSize();
        return 0; // Placeholder
    }

    private void handleInput(float delta) {
        // Movement
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            player.moveRight();
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            player.moveUp();
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            player.moveDown();
        }
    }

    private void drawUI() {
        WorldPosition pos = player.getWorldPosition();
        if (pos != null) {
            game.font.draw(batch, "Room: (" + pos.roomX + "," + pos.roomY + ")", 10, 580);
            game.font.draw(batch, "Local: " + (int)pos.localX + ", " + (int)pos.localY, 10, 560);
            game.font.draw(batch, "Direction: " + player.getCurrentDirection(), 10, 540);
            game.font.draw(batch, "Moving: " + player.isMoving(), 10, 520);
            game.font.draw(batch, "Anim Time: " + String.format("%.2f", player.getAnimationStateTime()), 10, 500);
        }

        // Performance info at bottom
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        game.font.draw(batch, "FPS: " + (int)fps, 10, 40);
        game.font.draw(batch, "Memory: " + (usedMemory / 1024 / 1024) + "MB", 10, 20);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (world != null) {
            world.dispose();
        }

        // Dispose static resources
        player.dispose();
        TextureUtils.disposeAll();

        System.out.println("GameScreen disposed all resources");
    }

    @Override
    public void show() {
    }
}