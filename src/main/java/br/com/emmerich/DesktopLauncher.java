package br.com.emmerich;

import br.com.emmerich.core.PlatformerGame;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Hello world!
 *
 */
public class DesktopLauncher
{
    public static void main( String[] args )
    {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Seemless World Game");
        config.setWindowedMode(800, 600);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new PlatformerGame(), config);
    }
}
