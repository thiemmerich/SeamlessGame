package br.com.emmerich.core;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorldPosition {
    public int roomX, roomY; // Grid coordinates
    public float localX, localY; // Position within current room

    public WorldPosition(int roomX, int roomY, float localX, float localY) {
        this.roomX = roomX;
        this.roomY = roomY;
        this.localX = localX;
        this.localY = localY;
    }

    public void setInicialPosition (int roomX, int roomY, float localX, float localY) {
        this.roomX = roomX;
        this.roomY = roomY;
        this.localX = localX;
        this.localY = localY;
    }

    public WorldPosition copy() {
        return new WorldPosition(roomX, roomY, localX, localY);
    }
}
