package br.com.emmerich.environment;

import br.com.emmerich.core.SeamlessWorld;
import br.com.emmerich.texture.TextureUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private String roomId;
    private float width, height;
    private Color backgroundColor;
    private int gridX, gridY; // Grid position
    private SeamlessWorld world;

    // Cache boundary textures
    private static Texture redBoundaryTexture;
    private static Texture darkGrayBorderTexture;

    static {
        // Create boundary textures once
        redBoundaryTexture = TextureUtils.createColoredTexture(1, 0, 0, 0.5f);
        darkGrayBorderTexture = TextureUtils.createColoredTexture(0.3f, 0.3f, 0.3f, 1f);
    }

    public Location(
            String roomId,
            float width,
            float height,
            Color backgroundColor,
            int gridX,
            int gridY,
            SeamlessWorld world) {
        this.roomId = roomId;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.gridX = gridX;
        this.gridY = gridY;
        this.world = world;
    }

    public void render(SpriteBatch batch) {
        // Calculate world position for this room
        float worldX = gridX * width;
        float worldY = gridY * height;

        // Draw room background
        batch.setColor(backgroundColor);
        batch.draw(TextureUtils.createColoredTexture(backgroundColor),
                worldX, worldY, width, height);
        batch.setColor(Color.WHITE);

        // Draw room border
        drawRoomBorder(batch, worldX, worldY);

        // Draw boundary warnings for edges without connected rooms
        drawBoundaryWarnings(batch, worldX, worldY);

        // Draw room label
        drawRoomLabel(batch, worldX, worldY);
    }

    private void drawBoundaryWarnings(SpriteBatch batch, float worldX, float worldY) {
        // Check each direction for missing rooms and draw warnings
        if (!world.roomExists(gridX - 1, gridY)) {
            drawWestBoundary(batch, worldX, worldY); // No room to the west
        }
        if (!world.roomExists(gridX + 1, gridY)) {
            drawEastBoundary(batch, worldX, worldY); // No room to the east
        }
        if (!world.roomExists(gridX, gridY - 1)) {
            drawSouthBoundary(batch, worldX, worldY); // No room to the south
        }
        if (!world.roomExists(gridX, gridY + 1)) {
            drawNorthBoundary(batch, worldX, worldY); // No room to the north
        }
    }

    private void drawRoomBorder(SpriteBatch batch, float worldX, float worldY) {
        batch.setColor(Color.DARK_GRAY);
        // Use cached texture instead of creating new ones
        batch.draw(darkGrayBorderTexture, worldX, worldY, width, 2); // bottom
        batch.draw(darkGrayBorderTexture, worldX, worldY + height - 2, width, 2); // top
        batch.draw(darkGrayBorderTexture, worldX, worldY, 2, height); // left
        batch.draw(darkGrayBorderTexture, worldX + width - 2, worldY, 2, height); // right
        batch.setColor(Color.WHITE);
    }

    private void drawWestBoundary(SpriteBatch batch, float worldX, float worldY) {
        batch.setColor(1, 0, 0, 0.3f);
        for (int i = 0; i < height; i += 40) {
            batch.draw(redBoundaryTexture, worldX, worldY + i, 10, 20);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawEastBoundary(SpriteBatch batch, float worldX, float worldY) {
        batch.setColor(1, 0, 0, 0.3f);
        for (int i = 0; i < height; i += 40) {
            batch.draw(redBoundaryTexture, worldX + width - 10, worldY + i, 10, 20);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawSouthBoundary(SpriteBatch batch, float worldX, float worldY) {
        batch.setColor(1, 0, 0, 0.3f);
        for (int i = 0; i < width; i += 40) {
            batch.draw(redBoundaryTexture, worldX + i, worldY, 20, 10);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawNorthBoundary(SpriteBatch batch, float worldX, float worldY) {
        batch.setColor(1, 0, 0, 0.3f);
        for (int i = 0; i < width; i += 40) {
            batch.draw(redBoundaryTexture, worldX + i, worldY + height - 10, 20, 10);
        }
        batch.setColor(Color.WHITE);
    }

    // Static cleanup method for Room class
    public static void disposeStaticResources() {
        if (redBoundaryTexture != null) {
            redBoundaryTexture.dispose();
            redBoundaryTexture = null;
        }
        if (darkGrayBorderTexture != null) {
            darkGrayBorderTexture.dispose();
            darkGrayBorderTexture = null;
        }
    }

    private void drawRoomLabel(SpriteBatch batch, float worldX, float worldY) {
        // You'll need to use a font here - this is just placeholder
        // game.font.draw(batch, roomId + " (" + gridX + "," + gridY + ")",
        //               worldX + 10, worldY + height - 20);
    }

    public void dispose() {
        disposeStaticResources();
    }
}
