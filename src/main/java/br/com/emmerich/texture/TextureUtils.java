package br.com.emmerich.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureUtils {
    // Cache colored textures to avoid creating new ones every frame
    private static Map<String, Texture> textureCache = new HashMap<>();

    public static Texture createColoredTexture(float r, float g, float b, float a) {
        String key = r + "," + g + "," + b + "," + a;

        // Return cached texture if it exists
        if (textureCache.containsKey(key)) {
            return textureCache.get(key);
        }

        // Create new texture and cache it
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        textureCache.put(key, texture);
        return texture;
    }

    public static Texture createColoredTexture(Color color) {
        return createColoredTexture(color.r, color.g, color.b, color.a);
    }

    // Call this when your game ends to clean up all textures
    public static void disposeAll() {
        for (Texture texture : textureCache.values()) {
            texture.dispose();
        }
        textureCache.clear();
    }
}