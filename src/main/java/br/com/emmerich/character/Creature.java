package br.com.emmerich.character;

import br.com.emmerich.core.SeamlessWorld;
import br.com.emmerich.core.WorldPosition;
import br.com.emmerich.environment.Location;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Creature {

    private WorldPosition worldPosition;
    private SeamlessWorld world;
    private Texture characterTexture;
    private TextureRegion[][] frames;
    private Animation<TextureRegion>[] characterAnimations;

    // Animation fields
    private Animation<TextureRegion> frontWalkAnimation;
    private Animation<TextureRegion> rightWalkAnimation;
    private Animation<TextureRegion> leftWalkAnimation;
    private Animation<TextureRegion> backWalkAnimation;
    private Animation<TextureRegion> currentAnimation;

    private float animationStateTime = 0;
    private Human.PlayerDirection currentDirection = Human.PlayerDirection.FRONT;
    private boolean isMoving = false;

    // Position and size
    private float x, y;
    private float width, height;
    private int speed;

    // Movement
    private float velocityX, velocityY;
    private boolean facingRight;

    // Sprite
    private TextureRegion currentSprite;
    private static boolean texturesLoaded = false;

    // Add a small buffer to prevent boundary oscillation
    private static final float BOUNDARY_BUFFER = 0.1f;

    // Health bar properties
    private float healthBarWidth = 50f;
    private float healthBarHeight = 6f;
    private float healthBarOffsetY = 70f; // Position above the character

    // Name tag properties
    private float nameOffsetY = 85f;

    // Attributes
    private String name;
    private int health;
    private int maxHealth;

    // Player direction enum
    public enum PlayerDirection {
        FRONT, RIGHT, LEFT, BACK
    }

    public Creature(String spriteFile) {
        if (!texturesLoaded) {
            characterTexture = new Texture(Gdx.files.internal(spriteFile));

            int FRAME_COLS = 4;
            int FRAME_ROWS = 4;
            int FRAME_WIDTH = 64 / FRAME_COLS;  // 16 pixels
            int FRAME_HEIGHT = 96 / FRAME_ROWS; // 24 pixels

            frames = TextureRegion.split(characterTexture, FRAME_WIDTH, FRAME_HEIGHT);
            texturesLoaded = true;

            // Create animations for each direction
            createAnimations();
        }

        // Set initial animation
        currentAnimation = frontWalkAnimation;
        if (Objects.nonNull(frontWalkAnimation)) {
            this.setCurrentSprite(frontWalkAnimation.getKeyFrame(0));
        }
    }

    private void createAnimations() {
        float frameDuration = 0.1f; // 100ms per frame

        // Row 0: Walking front (facing camera)
        frontWalkAnimation = new Animation<>(frameDuration, frames[0]);

        // Row 1: Walking right
        rightWalkAnimation = new Animation<>(frameDuration, frames[1]);

        // Row 2: Walking left
        leftWalkAnimation = new Animation<>(frameDuration, frames[2]);

        // Row 3: Walking back (away from camera)
        backWalkAnimation = new Animation<>(frameDuration, frames[3]);
    }

    public void update(float deltaTime, SeamlessWorld world) {
        this.world = world;

        if (worldPosition == null) return;

        // Update animation time if moving
        if (isMoving) {
            animationStateTime += deltaTime;
        } else {
            animationStateTime = 0; // Reset when not moving
        }

        // Calculate proposed movement
        float proposedLocalX = worldPosition.localX + velocityX * deltaTime;
        float proposedLocalY = worldPosition.localY + velocityY * deltaTime;

        // Handle room transitions and boundaries
        handleRoomTransitions(proposedLocalX, proposedLocalY);

        // Update animation based on movement direction
        updateAnimation();

        // Reset velocity for next frame
        velocityX = 0;
        velocityY = 0;
        isMoving = false; // Reset movement flag
    }

    private void handleRoomTransitions(float proposedLocalX, float proposedLocalY) {
        int currentRoomX = worldPosition.roomX;
        int currentRoomY = worldPosition.roomY;

        // Use buffered checks to prevent oscillation
        boolean tryingToLeaveWest = proposedLocalX < -BOUNDARY_BUFFER;
        boolean tryingToLeaveEast = proposedLocalX + width > SeamlessWorld.ROOM_WIDTH + BOUNDARY_BUFFER;
        boolean tryingToLeaveSouth = proposedLocalY < -BOUNDARY_BUFFER;
        boolean tryingToLeaveNorth = proposedLocalY + height > SeamlessWorld.ROOM_HEIGHT + BOUNDARY_BUFFER;

        // If not trying to leave room, just update position
        if (!tryingToLeaveWest && !tryingToLeaveEast && !tryingToLeaveSouth && !tryingToLeaveNorth) {
            worldPosition.localX = proposedLocalX;
            worldPosition.localY = proposedLocalY;
            return;
        }

        // Handle each direction separately with boundary clamping
        if (tryingToLeaveWest) {
            if (world.roomExists(currentRoomX - 1, currentRoomY)) {
                // Room exists - transition west
                worldPosition.roomX = currentRoomX - 1;
                worldPosition.localX = proposedLocalX + SeamlessWorld.ROOM_WIDTH;
                worldPosition.localY = proposedLocalY;
                System.out.println("Entered room: (" + worldPosition.roomX + "," + worldPosition.roomY + ")");
            } else {
                // Room doesn't exist - block at boundary with buffer
                worldPosition.localX = BOUNDARY_BUFFER;
                worldPosition.localY = proposedLocalY;
            }
        } else if (tryingToLeaveEast) {
            if (world.roomExists(currentRoomX + 1, currentRoomY)) {
                // Room exists - transition east
                worldPosition.roomX = currentRoomX + 1;
                worldPosition.localX = proposedLocalX - SeamlessWorld.ROOM_WIDTH;
                worldPosition.localY = proposedLocalY;
                System.out.println("Entered room: (" + worldPosition.roomX + "," + worldPosition.roomY + ")");
            } else {
                // Room doesn't exist - block at boundary with buffer
                worldPosition.localX = SeamlessWorld.ROOM_WIDTH - width - BOUNDARY_BUFFER;
                worldPosition.localY = proposedLocalY;
            }
        } else if (tryingToLeaveSouth) {
            if (world.roomExists(currentRoomX, currentRoomY - 1)) {
                // Room exists - transition south
                worldPosition.roomY = currentRoomY - 1;
                worldPosition.localY = proposedLocalY + SeamlessWorld.ROOM_HEIGHT;
                worldPosition.localX = proposedLocalX;
                System.out.println("Entered room: (" + worldPosition.roomX + "," + worldPosition.roomY + ")");
            } else {
                // Room doesn't exist - block at boundary with buffer
                worldPosition.localY = BOUNDARY_BUFFER;
                worldPosition.localX = proposedLocalX;
            }
        } else {
            if (world.roomExists(currentRoomX, currentRoomY + 1)) {
                // Room exists - transition north
                worldPosition.roomY = currentRoomY + 1;
                worldPosition.localY = proposedLocalY - SeamlessWorld.ROOM_HEIGHT;
                worldPosition.localX = proposedLocalX;
                System.out.println("Entered room: (" + worldPosition.roomX + "," + worldPosition.roomY + ")");
            } else {
                // Room doesn't exist - block at boundary with buffer
                worldPosition.localY = SeamlessWorld.ROOM_HEIGHT - height - BOUNDARY_BUFFER;
                worldPosition.localX = proposedLocalX;
            }
        }

        // Ensure position is within valid bounds after transition
        clampPositionToRoom();
    }

    private void clampPositionToRoom() {
        // Make sure player stays within room boundaries with buffer
        worldPosition.localX = Math.max(
                BOUNDARY_BUFFER,
                Math.min(worldPosition.localX, SeamlessWorld.ROOM_WIDTH - width - BOUNDARY_BUFFER));
        worldPosition.localY = Math.max(
                BOUNDARY_BUFFER,
                Math.min(worldPosition.localY, SeamlessWorld.ROOM_HEIGHT - height - BOUNDARY_BUFFER));
    }

    private void updateAnimation() {
        // Determine direction based on velocity
        if (velocityX > 0) {
            currentDirection = PlayerDirection.RIGHT;
            currentAnimation = rightWalkAnimation;
            isMoving = true;
        } else if (velocityX < 0) {
            currentDirection = PlayerDirection.LEFT;
            currentAnimation = leftWalkAnimation;
            isMoving = true;
        } else if (velocityY > 0) {
            currentDirection = PlayerDirection.BACK;
            currentAnimation = backWalkAnimation;
            isMoving = true;
        } else if (velocityY < 0) {
            currentDirection = PlayerDirection.FRONT;
            currentAnimation = frontWalkAnimation;
            isMoving = true;
        }

        // Update current sprite based on animation
        if (isMoving) {
            this.setCurrentSprite(currentAnimation.getKeyFrame(animationStateTime, true));
        } else {
            // When not moving, use the first frame of current direction (idle pose)
            switch (currentDirection) {
                case FRONT:
                    this.setCurrentSprite(frontWalkAnimation.getKeyFrames()[0]);
                    break;
                case RIGHT:
                    this.setCurrentSprite(rightWalkAnimation.getKeyFrames()[0]);
                    break;
                case LEFT:
                    this.setCurrentSprite(leftWalkAnimation.getKeyFrames()[0]);
                    break;
                case BACK:
                    this.setCurrentSprite(backWalkAnimation.getKeyFrames()[0]);
                    break;
            }
        }
    }

    private float checkHorizontalBounds(float proposedX, Location location) {
        float roomWidth = location.getWidth();

        // Check left boundary
        if (proposedX < 0) {
            return 0;
        }

        // Check right boundary
        if (proposedX + width > roomWidth) {
            return roomWidth - width;
        }

        return proposedX;
    }

    private float checkVerticalBounds(float proposedY, Location location) {
        float roomHeight = location.getHeight();

        // Check bottom boundary
        if (proposedY < 0) {
            return 0;
        }

        // Check top boundary
        if (proposedY + height > roomHeight) {
            return roomHeight - height;
        }

        return proposedY;
    }

    // Update the existing checkBounds method to work with room boundaries
    public void checkBounds(Location currentLocation) {
        if (currentLocation != null) {
            x = checkHorizontalBounds(x, currentLocation);
            y = checkVerticalBounds(y, currentLocation);
        }
    }

    // Update movement methods to set the moving flag
    public void moveLeft() {
        velocityX = -speed;
        facingRight = false;
        isMoving = true;
    }

    public void moveRight() {
        velocityX = speed;
        facingRight = true;
        isMoving = true;
    }

    public void moveUp() {
        velocityY = speed;
        isMoving = true;
    }

    public void moveDown() {
        velocityY = -speed;
        isMoving = true;
    }

    // World position management
    public WorldPosition getWorldPosition() {
        return worldPosition != null ? worldPosition.copy() : null;
    }

    public void setWorldPosition(WorldPosition position) {
        this.worldPosition = position.copy();
    }

    // Getters for current absolute position (for camera, etc.)
    public float getAbsoluteX() {
        return worldPosition != null ?
                worldPosition.roomX * SeamlessWorld.ROOM_WIDTH + worldPosition.localX : 0;
    }

    public float getAbsoluteY() {
        return worldPosition != null ?
                worldPosition.roomY * SeamlessWorld.ROOM_HEIGHT + worldPosition.localY : 0;
    }

    public float getCenterX() {
        return x + width / 2;
    }

    public float getCenterY() {
        return y + height / 2;
    }

    public void dispose() {
        if (Objects.nonNull(characterTexture)) {
            characterTexture.dispose();
            texturesLoaded = false;
        }
    }
}
