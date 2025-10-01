package br.com.emmerich.core;

import br.com.emmerich.character.Creature;
import br.com.emmerich.character.Human;
import br.com.emmerich.environment.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class SeamlessWorld {
    private final Map<String, Location> rooms; // Key: "x,y" format
    private Creature player;
    private OrthographicCamera camera;

    // World grid settings
    public static final int ROOM_WIDTH = 1600;
    public static final int ROOM_HEIGHT = 1200;
    private static final int VIEW_RADIUS = 1; // How many adjacent rooms to render

    // Cache the currently visible rooms to avoid recalculating every frame
    private List<Location> currentlyVisibleLocations = new ArrayList<>();
    private int lastPlayerRoomX = Integer.MIN_VALUE;
    private int lastPlayerRoomY = Integer.MIN_VALUE;

    public SeamlessWorld() {
        this.rooms = new HashMap<>();
        initializeWorld();
    }

    private void initializeWorld() {
        // Create rooms in a grid pattern
        // Room at (0,0) - Forest
        rooms.put("0,0", new Location("forest", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.2f, 0.6f, 0.3f, 1f), 0, 0, this));

        // Room at (1,0) - Cave (east of forest)
        rooms.put("1,0", new Location("cave", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.3f, 0.3f, 0.4f, 1f), 1, 0, this));

        // Room at (0,1) - Beach (north of forest)
        rooms.put("0,1", new Location("beach", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.8f, 0.8f, 0.4f, 1f), 0, 1, this));

        rooms.put("1,1", new Location("beach", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.8f, 0.8f, 0.4f, 1f), 0, 1, this));

        // Room at (-1,0) - Volcano (west of forest)
        rooms.put("-1,0", new Location("volcano", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.6f, 0.2f, 0.1f, 1f), -1, 0, this));

        // Room at (0,-1) - Desert (south of forest)
        rooms.put("0,-1", new Location("desert", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.9f, 0.8f, 0.3f, 1f), 0, -1, this));

        rooms.put("-1,1", new Location("desert", ROOM_WIDTH, ROOM_HEIGHT, new Color(0.9f, 0.8f, 0.3f, 1f), 0, -1, this));

    }

    public void setPlayer(Creature player) {
        this.player = player;
        // Start player in center of room (0,0)
        if (player != null) {
            player.setWorldPosition(new WorldPosition(0, 0,
                    ROOM_WIDTH / 2 - player.getWidth() / 2,
                    ROOM_HEIGHT / 2 - player.getHeight() / 2));
        }
    }

    public void update(float deltaTime) {
        if (player != null) {
            player.update(deltaTime, this);
            updateCameraPosition();
            updateVisibleRoomsCache();
        }
    }

    private void updateVisibleRoomsCache() {
        if (player == null) return;

        WorldPosition playerPos = player.getWorldPosition();

        // Only update cache if player changed rooms
        if (playerPos.roomX != lastPlayerRoomX || playerPos.roomY != lastPlayerRoomY) {
            currentlyVisibleLocations.clear();

            // Find all rooms within view radius
            for (int dx = -VIEW_RADIUS; dx <= VIEW_RADIUS; dx++) {
                for (int dy = -VIEW_RADIUS; dy <= VIEW_RADIUS; dy++) {
                    String roomKey = (playerPos.roomX + dx) + "," + (playerPos.roomY + dy);
                    Location location = rooms.get(roomKey);
                    if (location != null) {
                        currentlyVisibleLocations.add(location);
                    }
                }
            }

            lastPlayerRoomX = playerPos.roomX;
            lastPlayerRoomY = playerPos.roomY;

            System.out.println("Visible rooms: " + currentlyVisibleLocations.size());
        }
    }

    public void render(SpriteBatch batch) {
        if (player == null) return;

        // Render only cached visible rooms (much faster!)
        for (Location location : currentlyVisibleLocations) {
            location.render(batch);
        }

        // Render player
        renderPlayer(batch);
    }

    private void renderPlayer(SpriteBatch batch) {
        WorldPosition pos = player.getWorldPosition();
        float worldX = pos.roomX * ROOM_WIDTH + pos.localX;
        float worldY = pos.roomY * ROOM_HEIGHT + pos.localY;

        TextureRegion sprite = player.getCurrentSprite();
        if (sprite != null) {
            // NO MORE FLIPPING - we have separate left/right animations
            batch.draw(sprite, worldX, worldY, player.getWidth(), player.getHeight());
        }
    }

    private void updateCameraPosition() {
        if (camera != null && player != null) {
            WorldPosition pos = player.getWorldPosition();
            float worldX = pos.roomX * ROOM_WIDTH + pos.localX + player.getWidth() / 2;
            float worldY = pos.roomY * ROOM_HEIGHT + pos.localY + player.getHeight() / 2;

            camera.position.set(worldX, worldY, 0);
        }
    }

    public boolean roomExists(int roomX, int roomY) {
        return rooms.containsKey(roomX + "," + roomY);
    }

    public void dispose() {
        for (Location location : rooms.values()) {
            location.dispose();
        }

        // Clear the room cache to help garbage collection
        currentlyVisibleLocations.clear();
        rooms.clear(); // This will allow rooms to be garbage collected

        System.out.println("SeamlessWorld disposed");
    }
}
