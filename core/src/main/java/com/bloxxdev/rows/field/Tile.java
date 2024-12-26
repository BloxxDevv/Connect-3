package com.bloxxdev.rows.field;

import com.badlogic.gdx.graphics.Texture;
import com.bloxxdev.rows.constants.FieldConstants;
import com.bloxxdev.rows.constants.Textures;
import org.mini2Dx.core.graphics.Graphics;

public class Tile {

    private int x;
    private int y;

    private boolean empty = true;

    public boolean selected = false;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.drawTexture(Textures.TILE, x* FieldConstants.TILE_SIZE + FieldConstants.OFFSET, y*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, FieldConstants.TILE_SIZE, FieldConstants.TILE_SIZE);

        if (selected)
            g.drawTexture(Textures.SELECTION, x*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, y*FieldConstants.TILE_SIZE + FieldConstants.OFFSET, FieldConstants.TILE_SIZE, FieldConstants.TILE_SIZE);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(){
        this.empty = true;
    }

    public void toggleEmpty(){
        this.empty = !this.empty;
    }
}
