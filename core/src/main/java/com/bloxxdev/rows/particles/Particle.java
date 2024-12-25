package com.bloxxdev.rows.particles;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.TextureRegion;

import java.util.Random;

public class Particle {

    Random random = new Random();

    private TextureRegion textureRegion;

    private int x;
    private int y;

    private float xa;
    private float ya;

    public boolean removed = false;

    public Particle(int x, int y, int xa, int ya, Texture texture){
        this.x = x;
        this.y = y;

        this.xa = xa;
        this.ya = ya;

        int u = random.nextInt(20) + 20;
        int v = random.nextInt(20) + 20;

        this.textureRegion = new TextureRegion(texture, u, v, 5, 5);
    }

    public void tick(){
        this.x += this.xa;
        this.y += this.ya;
        xa /= 1.04F;
        ya += 0.3F;

        if (Math.random() < 0.1F)
            remove();
    }

    private void remove(){
        this.removed = true;
    }

    public void render(Graphics g){
        g.drawTextureRegion(textureRegion, x, y);
    }

}
