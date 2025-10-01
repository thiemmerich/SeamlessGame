package br.com.emmerich.character;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Human extends Creature {

    public Human(String playerName, float width, float height) {
        super( "assets/elf.png");
        super.setWidth(width);
        super.setHeight(height);
        super.setFacingRight(true);

        super.setName(playerName);
        super.setHealth(100);
        super.setMaxHealth(100);
        super.setSpeed(400);
    }

    public void dispose() {
        super.dispose();
    }
}
