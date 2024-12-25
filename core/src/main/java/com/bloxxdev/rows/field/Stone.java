package com.bloxxdev.rows.field;

import com.badlogic.gdx.graphics.Texture;
import com.bloxxdev.rows.constants.Textures;
import org.mini2Dx.core.graphics.Graphics;

public class Stone {

    public static final float SPEED = 1.4F;

    public int x;
    public int y;

    private float xa = 0;
    private float ya = 0;

    float xbuffer = 0;
    float ybuffer = 0;

    public int targetX;
    public int targetY;

    private int revertX = -1;
    private int revertY = -1;

    private StoneType stoneType;

    public boolean isMoving = false;

    public Stone(int x, int y, StoneType stoneType, int targetY){
        this.x = x;
        this.y = y;

        this.targetX = x;
        this.targetY = targetY;

        this.stoneType = stoneType;
    }

    public void tick(){
        int iYa = 0;
        int iXa = 0;
        if (y != targetY){
            float yMove = 0.0F;
            if (targetY > y)
                yMove = 1;
            if (targetY < y) {
                yMove = -1;
            }
            ya += yMove + ybuffer;

            ya *= SPEED;

            ya *= 0.7F;

            iYa = (int) ya;

            if (iYa == 0)
                iYa = (int) yMove;

            ybuffer = ya - iYa;
        }else{
            ya = 0;
            ybuffer = 0;
        }

        if (x != targetX){
            float xMove = 0.0F;
            if (targetX > x)
                xMove = 1;
            if (targetX < x)
                xMove = -1;

            xa += xMove + xbuffer;

            xa *= SPEED;

            xa *= 0.7F;

            iXa = (int) xa;

            if (iXa == 0)
                iXa = (int) xMove;

            xbuffer = xa - iXa;
        }else {
            xa = 0;
            xbuffer = 0;
        }

        move(iXa, iYa);
    }

    private void move(int xa, int ya){
        int xOrg = x;
        int yOrg = y;

        if (ya > 0 && y+ya > targetY) {
            y = targetY;
            this.ya = 0;
            this.ybuffer = 0;
            if (revertY != -1) {
                targetY = revertY;
                revertY = -1;
            }
            return;
        }
        if (ya < 0 && y+ya < targetY) {
            y = targetY;
            this.ya = 0;
            this.ybuffer = 0;
            if (revertY != -1) {
                targetY = revertY;
                revertY = -1;
            }
            return;
        }
        if (xa > 0 && x+xa > targetX) {
            x = targetX;
            this.xa = 0;
            this.xbuffer = 0;
            if (revertX != -1) {
                targetX = revertX;
                revertX = -1;
            }
            return;
        }
        if (xa < 0 && x+xa < targetX) {
            x = targetX;
            this.xa = 0;
            this.xbuffer = 0;
            if (revertX != -1) {
                targetX = revertX;
                revertX = -1;
            }
            return;
        }

        x += xa;
        y += ya;

        isMoving = xOrg != x || yOrg != y;
    }

    public void render(Graphics g){
        Texture tex = Textures.RED;

        switch (stoneType){
            case RED:
                tex = Textures.RED;
                break;
            case BLUE:
                tex = Textures.BLUE;
                break;
            case GREEN:
                tex = Textures.GREEN;
                break;
            case YELLOW:
                tex = Textures.YELLOW;
                break;
            case ORANGE:
                tex = Textures.ORANGE;
                break;
            case WHITE:
                tex = Textures.WHITE;
                break;
        }

        g.drawTexture(tex, x, y);
    }

    public void setTargetPos(int targetX, int targetY){
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void setRevertPos(int revertX, int revertY){
        this.revertX = revertX;
        this.revertY = revertY;
    }

    public StoneType getStoneType() {
        return stoneType;
    }
}
