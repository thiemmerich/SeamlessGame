package br.com.emmerich.character;

import br.com.emmerich.core.SeamlessWorld;
import br.com.emmerich.core.WorldPosition;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Elf extends Creature {

    public Elf(String playerName, float width, float height) {
        super( "assets/elf.png");
        super.setWidth(width);
        super.setHeight(height);
        super.setFacingRight(true);

        super.setName(playerName);
        super.setHealth(100);
        super.setMaxHealth(100);
        super.setSpeed(700);
    }

    public void dispose() {
        super.dispose();
    }
}
