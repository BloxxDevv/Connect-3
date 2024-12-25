package com.bloxxdev.rows.field;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class Tile {

    Texture tex = new Texture("Tile.png");
    Texture outline = new Texture("Outline.png");

    private int x;
    private int y;

    private boolean empty = true;

    public boolean selected = false;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.drawTexture(tex, x*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, y*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, FieldConstants.TILE_SIZE, FieldConstants.TILE_SIZE);

        if (selected)
            g.drawTexture(outline, x*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, y*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, FieldConstants.TILE_SIZE, FieldConstants.TILE_SIZE);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void toggleEmpty(){
        this.empty = !this.empty;
    }
}
