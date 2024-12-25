package com.bloxxdev.rows.particles;

import com.badlogic.gdx.graphics.Texture;
import com.bloxxdev.rows.constants.FieldConstants;
import com.bloxxdev.rows.constants.Textures;
import com.bloxxdev.rows.field.StoneType;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;
import java.util.Random;

public class ParticleEngine {

    ArrayList<Particle> particles = new ArrayList<Particle>();

    Random random = new Random();

    public void destroyStone(int x, int y, StoneType type){
        int posX = x * FieldConstants.TILE_SIZE + FieldConstants.OFFSET + FieldConstants.TILE_SIZE/2;
        int posY = y * FieldConstants.TILE_SIZE + FieldConstants.OFFSET + FieldConstants.TILE_SIZE/2;

        Texture texture = Textures.RED;

        switch (type){
            case RED:
                texture = Textures.RED;
                break;
            case BLUE:
                texture = Textures.BLUE;
                break;
            case GREEN:
                texture = Textures.GREEN;
                break;
            case WHITE:
                texture = Textures.WHITE;
                break;
            case ORANGE:
                texture = Textures.ORANGE;
                break;
            case YELLOW:
                texture = Textures.YELLOW;
                break;
        }

        for (int i = 0; i < 40; i++) {
            int xa = random.nextInt(11) - 5;
            int ya = random.nextInt(11) - 5;

            particles.add(new Particle(posX, posY, xa, ya, texture));
        }
    }

    public void tick(){
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i).removed) {
                particles.remove(i--);
                continue;
            }
            particles.get(i).tick();
        }
    }

    public void render(Graphics g){
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).render(g);
        }
    }
}
