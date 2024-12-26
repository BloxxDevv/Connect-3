package com.bloxxdev.rows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bloxxdev.rows.constants.FieldConstants;
import com.bloxxdev.rows.field.Stone;
import com.bloxxdev.rows.field.StoneType;
import com.bloxxdev.rows.field.Tile;
import com.bloxxdev.rows.particles.ParticleEngine;
import com.bloxxdev.rows.window.Window;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.Random;

public class Main extends BasicGame {
	public static final String GAME_IDENTIFIER = "com.bloxxdev.rows";

    private Window window;
    private Tile[][] tiles = new Tile[18][13];
    private Stone[][] stones = new Stone[18][13];

    private int tempX = 0;

    private int tileAmount = 0;

    private boolean init = true;

    boolean mouseProcessed = false;

    private int selX = -1;
    private int selY = -1;

    private int[] scheduledRemoval = new int[]{-1, -1, -1, -1};

    private boolean remove = false;

    private int prevMX = -1;
    private int prevMY = -1;

    private boolean slide = false;

    private boolean test = false;

    private ParticleEngine particleEngine;

	@Override
    public void initialise() {
        this.particleEngine = new ParticleEngine();

        this.window = new Window();
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (x > 3 && x < 14 && y > 3 && y < 9) {
                    Tile t = new Tile(x, y);
                    tiles[x][y] = t;
                }
            }
        }
    }

    int c = 0;
    int x = 0;
    int y = 0;

    private void initBoard(){
        if (c == 1){
            c = 0;

            if (y < 13){
                if (x < 18){

                    if (x > 3 && x < 14 && y > 3 && y < 9) {
                        spawnNewStone(x);
                    }

                    x++;
                }
                if (x == 18) {
                    y++;
                    x = 0;
                }
            }
            if (y == 13)
                init = false;
        }
    }

    private void scheduleRemoval(){
        if (!isMoving()){
            if (remove){
                removeOnSchedule();
                remove = false;
            }
        }
    }

    private void fetchLeftClick(){
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !isMoving() && !init){
            if (!mouseProcessed){

                int mx = Gdx.input.getX();
                int my = Gdx.input.getY();

                int x = (mx - FieldConstants.OFFSET) / FieldConstants.TILE_SIZE;
                int y = (my - FieldConstants.OFFSET) / FieldConstants.TILE_SIZE;

                if (tiles[x][y] != null) {
                    if (selX != -1 && selY != -1) {
                        if ((x == selX && Math.abs(y - selY) == 1 )|| y == selY && Math.abs(x - selX) == 1) {
                            tiles[selX][selY].selected = false;

                            Stone[][] copy = stones.clone();

                            Stone stoneBuffer = copy[selX][selY];
                            stones[selX][selY] = stones[x][y];
                            stones[x][y] = stoneBuffer;
                            stones[selX][selY].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                            stones[x][y].setTargetPos(x * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, y * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                            if (isMatch(x, y) == null && isMatch(selX, selY) == null){
                                Stone[][] newCopy = stones.clone();

                                Stone revStoneBuffer = newCopy[selX][selY];
                                stones[selX][selY] = stones[x][y];
                                stones[x][y] = revStoneBuffer;

                                int xa = selX;
                                int ya = selY;

                                if (x == selX)
                                    xa = -1;
                                if (y == selY)
                                    ya = -1;

                                stones[selX][selY].setRevertPos((xa == -1) ? -1 : xa * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, (ya == -1) ? -1 : ya * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                                xa = x;
                                ya = y;

                                if (x == selX)
                                    xa = -1;
                                if (y == selY)
                                    ya = -1;

                                stones[x][y].setRevertPos((xa == -1) ? -1 : xa * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, (ya == -1) ? -1 : ya * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                            }else{
                                scheduledRemoval[0] = x;
                                scheduledRemoval[1] = y;
                                scheduledRemoval[2] = selX;
                                scheduledRemoval[3] = selY;

                                remove = true;
                            }

                            selX = -1;
                            selY = -1;
                            prevMX = -1;
                            prevMY = -1;
                        }else{
                            tiles[selX][selY].selected = false;
                            tiles[x][y].selected = true;
                            selX = x;
                            selY = y;
                            prevMX = mx;
                            prevMY = my;
                            slide = true;
                        }
                    }else{
                        tiles[x][y].selected = true;
                        selX = x;
                        selY = y;
                        prevMX = mx;
                        prevMY = my;
                        slide = true;
                    }
                }

                mouseProcessed = true;
            }
        }
    }

    private void fetchLeftRelease(){
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

            if (slide) {
                if (prevMX != -1 && prevMY != -1) {
                    int mx = Gdx.input.getX();
                    int my = Gdx.input.getY();

                    if (Math.abs(prevMX - mx) > 15 || Math.abs(prevMY - my) > 15) {

                        int xa = mx - prevMX;
                        int ya = my - prevMY;

                        if (Math.abs(xa) >= Math.abs(ya)){
                            if (xa < 0){
                                if (tiles[selX-1][selY] != null){
                                    tiles[selX][selY].selected = false;

                                    Stone[][] copy = stones.clone();

                                    Stone stoneBuffer = copy[selX][selY];
                                    stones[selX][selY] = stones[selX-1][selY];
                                    stones[selX-1][selY] = stoneBuffer;
                                    stones[selX][selY].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    stones[selX-1][selY].setTargetPos((selX-1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                                    if (isMatch(selX-1, selY) == null && isMatch(selX, selY) == null){
                                        Stone[][] newCopy = stones.clone();

                                        Stone revStoneBuffer = newCopy[selX][selY];
                                        stones[selX][selY] = stones[selX-1][selY];
                                        stones[selX-1][selY] = revStoneBuffer;
                                        stones[selX][selY].setRevertPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, -1);
                                        stones[selX-1][selY].setRevertPos((selX-1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, -1);
                                    }else{
                                        scheduledRemoval[0] = selX-1;
                                        scheduledRemoval[1] = selY;
                                        scheduledRemoval[2] = selX;
                                        scheduledRemoval[3] = selY;

                                        remove = true;
                                    }

                                    selX = -1;
                                    selY = -1;
                                    prevMX = -1;
                                    prevMY = -1;
                                }
                            }else{
                                if (tiles[selX+1][selY] != null){
                                    tiles[selX][selY].selected = false;

                                    Stone[][] copy = stones.clone();

                                    Stone stoneBuffer = copy[selX][selY];
                                    stones[selX][selY] = stones[selX+1][selY];
                                    stones[selX+1][selY] = stoneBuffer;
                                    stones[selX][selY].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    stones[selX+1][selY].setTargetPos((selX+1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                                    if (isMatch(selX+1, selY) == null && isMatch(selX, selY) == null){
                                        Stone[][] newCopy = stones.clone();

                                        Stone revStoneBuffer = newCopy[selX][selY];
                                        stones[selX][selY] = stones[selX+1][selY];
                                        stones[selX+1][selY] = revStoneBuffer;
                                        stones[selX][selY].setRevertPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, -1);
                                        stones[selX+1][selY].setRevertPos((selX+1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, -1);
                                    }else{
                                        scheduledRemoval[0] = selX+1;
                                        scheduledRemoval[1] = selY;
                                        scheduledRemoval[2] = selX;
                                        scheduledRemoval[3] = selY;

                                        remove = true;
                                    }

                                    selX = -1;
                                    selY = -1;
                                    prevMX = -1;
                                    prevMY = -1;
                                }
                            }
                        }else if (Math.abs(xa) < Math.abs(ya)){
                            if (ya < 0){
                                if (tiles[selX][selY-1] != null){
                                    tiles[selX][selY].selected = false;

                                    Stone[][] copy = stones.clone();

                                    Stone stoneBuffer = copy[selX][selY];
                                    stones[selX][selY] = stones[selX][selY-1];
                                    stones[selX][selY-1] = stoneBuffer;
                                    stones[selX][selY].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    stones[selX][selY-1].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, (selY-1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                                    if (isMatch(selX, selY-1) == null && isMatch(selX, selY) == null){
                                        Stone[][] newCopy = stones.clone();

                                        Stone revStoneBuffer = newCopy[selX][selY];
                                        stones[selX][selY] = stones[selX][selY-1];
                                        stones[selX][selY-1] = revStoneBuffer;
                                        stones[selX][selY].setRevertPos(-1, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                        stones[selX][selY-1].setRevertPos(-1, (selY-1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    }else{
                                        scheduledRemoval[0] = selX;
                                        scheduledRemoval[1] = selY-1;
                                        scheduledRemoval[2] = selX;
                                        scheduledRemoval[3] = selY;

                                        remove = true;
                                    }

                                    selX = -1;
                                    selY = -1;
                                    prevMX = -1;
                                    prevMY = -1;
                                }
                            }else{
                                if (tiles[selX][selY+1] != null){
                                    tiles[selX][selY].selected = false;

                                    Stone[][] copy = stones.clone();

                                    Stone stoneBuffer = copy[selX][selY];
                                    stones[selX][selY] = stones[selX][selY+1];
                                    stones[selX][selY+1] = stoneBuffer;
                                    stones[selX][selY].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    stones[selX][selY+1].setTargetPos(selX * FieldConstants.TILE_SIZE + FieldConstants.OFFSET, (selY+1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

                                    if (isMatch(selX, selY+1) == null && isMatch(selX, selY) == null){
                                        Stone[][] newCopy = stones.clone();

                                        Stone revStoneBuffer = newCopy[selX][selY];
                                        stones[selX][selY] = stones[selX][selY+1];
                                        stones[selX][selY+1] = revStoneBuffer;
                                        stones[selX][selY].setRevertPos(-1, selY * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                        stones[selX][selY+1].setRevertPos(-1, (selY+1) * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);
                                    }else{
                                        scheduledRemoval[0] = selX;
                                        scheduledRemoval[1] = selY+1;
                                        scheduledRemoval[2] = selX;
                                        scheduledRemoval[3] = selY;

                                        remove = true;
                                    }

                                    selX = -1;
                                    selY = -1;
                                    prevMX = -1;
                                    prevMY = -1;
                                }
                            }
                        }
                    }
                }
                slide = false;
            }

            mouseProcessed = false;
        }
    }

    private void tick(){
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (stones[x][y] != null) {
                    stones[x][y].tick();
                }
            }
        }

        particleEngine.tick();
    }

    private void applyGravity(){
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (tiles[x][y] != null && y > 0){
                    if (tiles[x][y].isEmpty()){
                        if (tiles[x][y-1] != null) {
                            if (!tiles[x][y - 1].isEmpty()) {
                                if (stones[x][y-1] != null) {
                                    stones[x][y - 1].setTargetPos(toScreenCoords(x), toScreenCoords(y));
                                    stones[x][y] = stones[x][y - 1];
                                    stones[x][y - 1] = null;
                                    tiles[x][y].toggleEmpty();
                                    tiles[x][y - 1].toggleEmpty();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnStones(){
        for (int i = 0; i < 18; i++) {
            int startY = 0;
            for (int y = 0; y < 13; y++) {
                if (tiles[i][y] != null){
                    startY = y;
                    break;
                }
            }

            if (tiles[i][startY] != null && tiles[i][startY].isEmpty())
                spawnNewStone(i);
        }
    }

    private int toScreenCoords(int i){
        return i * FieldConstants.TILE_SIZE + FieldConstants.OFFSET;
    }

    private void checkAllStones(){
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (tiles[x][y] != null && isMatch(x, y) != null){
                    scheduledRemoval[0] = x;
                    scheduledRemoval[1] = y;
                    removeOnSchedule();
                    return;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        initBoard();

        if (!init) {
            scheduleRemoval();
            if (!isMoving()) {
                spawnStones();
            }
            applyGravity();
            if (isMoving())
                test = true;
            if (!isMoving() && test){
                checkAllStones();
                test = false;
            }
        }

        fetchLeftClick();
        fetchLeftRelease();

        tick();

        c++;
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
        this.window.renderBackgroundTexture(g);

        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (tiles[x][y] != null){
                    tiles[x][y].render(g);
                }
            }
        }

        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (stones[x][y] != null) {
                    stones[x][y].render(g);
                }
            }
        }

        particleEngine.render(g);
    }

    private boolean isMoving(){
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 13; y++) {
                if (stones[x][y] != null && stones[x][y].isMoving)
                    return true;
            }
        }
        return false;
    }

    private void killBlaster(int x, int y){
        if (stones[x][y] != null) {
            if (stones[x][y].vBlast) {
                for (int yy = 0; yy < 13; yy++) {
                    if (tiles[x][yy] != null && stones[x][yy] != null) {
                        StoneType t = stones[x][yy].getStoneType();
                        if (stones[x][yy] != null && stones[x][yy].vBlast) {
                            if (yy != y)
                                killBlaster(x, yy);
                        }
                        if (stones[x][yy] != null && stones[x][yy].hBlast) {
                            if (yy != y)
                                killBlaster(x, yy);
                        }
                        stones[x][yy] = null;
                        tiles[x][yy].setEmpty();
                        particleEngine.destroyStone(x, yy, t);
                    }
                }
            } else if (stones[x][y].hBlast) {
                for (int xx = 0; xx < 18; xx++) {
                    if (tiles[xx][y] != null && stones[xx][y] != null) {
                        StoneType t = stones[xx][y].getStoneType();
                        if (stones[xx][y] != null && stones[xx][y].vBlast) {
                            if (xx != x)
                                killBlaster(xx, y);
                        }
                        if (stones[xx][y] != null && stones[xx][y].hBlast) {
                            if (xx != x)
                                killBlaster(xx, y);
                        }
                        stones[xx][y] = null;
                        tiles[xx][y].setEmpty();
                        particleEngine.destroyStone(xx, y, t);
                    }
                }
            }
        }
    }

    private void removeOnSchedule(){
        for (int i = 0; i < scheduledRemoval.length; i+=2) {
            int[][] kill = isMatch(scheduledRemoval[i], scheduledRemoval[i+1]);

            for (int j = 0; kill != null && j < kill.length; j++) {
                if (stones[kill[j][0]][kill[j][1]] != null) {
                    StoneType type = stones[kill[j][0]][kill[j][1]].getStoneType();
                    if (stones[kill[j][0]][kill[j][1]].vBlast || stones[kill[j][0]][kill[j][1]].hBlast){
                        killBlaster(kill[j][0], kill[j][1]);
                    } else {
                        stones[kill[j][0]][kill[j][1]] = null;
                        tiles[kill[j][0]][kill[j][1]].toggleEmpty();
                        particleEngine.destroyStone(kill[j][0], kill[j][1], type);
                    }
                }
            }
        }
        scheduledRemoval = new int[]{-1, -1, -1, -1};
    }

    private int[][] isMatch(int x, int y){
        if (x < 0 || y < 0 || x > 17 || y > 12)
            return null;

        if (stones[x][y] == null)
            return null;

        StoneType type = stones[x][y].getStoneType();

        if (x-3 >= 0 && stones[x-3][y] != null && stones[x-2][y] != null && stones[x-1][y] != null){
            if (stones[x-3][y].getStoneType() == stones[x-2][y].getStoneType() && stones[x-2][y].getStoneType() == stones[x-1][y].getStoneType() && stones[x-1][y].getStoneType() == type){
                return null;
            }
        }
        if (x+3 < 18 && stones[x+3][y] != null && stones[x+2][y] != null && stones[x+1][y] != null){
            if (stones[x+3][y].getStoneType() == stones[x+2][y].getStoneType() && stones[x+2][y].getStoneType() == stones[x+1][y].getStoneType() && stones[x+1][y].getStoneType() == type){
                return null;
            }
        }
        if (y-3 >= 0 && stones[x][y-3] != null && stones[x][y-2] != null && stones[x][y-1] != null){
            if (stones[x][y-3].getStoneType() == stones[x][y-2].getStoneType() && stones[x][y-2].getStoneType() == stones[x][y-1].getStoneType() && stones[x][y-1].getStoneType() == type){
                return null;
            }
        }
        if (y+3 < 13 && stones[x][y+3] != null && stones[x][y+2] != null && stones[x][y+1] != null){
            if (stones[x][y+3].getStoneType() == stones[x][y+2].getStoneType() && stones[x][y+2].getStoneType() == stones[x][y+1].getStoneType() && stones[x][y+1].getStoneType() == type){
                return null;
            }
        }

        //5 Row
        //HOR
        if (x-2 >= 0 && x+2 < 18 && stones[x-2][y] != null && stones[x-1][y] != null && stones[x+2][y] != null && stones[x+1][y] != null && stones[x-1][y].getStoneType() == type && stones[x+1][y].getStoneType() == type&& stones[x-2][y].getStoneType() == type && stones[x+2][y].getStoneType() == type) {
            return new int[][]{{x-2, y}, {x-1, y}, {x, y}, {x+1, y}, {x+2, y}};
        }
        //VERT
        if (y-2 >= 0 && y+2 < 13 && stones[x][y-2] != null && stones[x][y-1] != null && stones[x][y+2] != null && stones[x][y+1] != null && stones[x][y-1].getStoneType() == type && stones[x][y+1].getStoneType() == type&& stones[x][y-2].getStoneType() == type && stones[x][y+2].getStoneType() == type) {
            return new int[][]{{x, y-2}, {x, y-1}, {x, y}, {x, y+1}, {x, y+2}};
        }

        //4 Row
        //HOR R
        if (x-2 >= 0 && x+1 < 18 && stones[x-2][y] != null && stones[x-1][y] != null && stones[x+1][y] != null && stones[x-1][y].getStoneType() == type && stones[x+1][y].getStoneType() == type&& stones[x-2][y].getStoneType() == type) {
            stones[x][y].vBlast = true;
            return new int[][]{{x-2, y}, {x-1, y}, {x+1, y}};
        }
        //HOR L
        if (x-1 >= 0 && x+2 < 18 && stones[x-1][y] != null && stones[x+2][y] != null && stones[x+1][y] != null && stones[x-1][y].getStoneType() == type && stones[x+1][y].getStoneType() == type && stones[x+2][y].getStoneType() == type) {
            stones[x][y].vBlast = true;
            return new int[][]{{x-1, y}, {x+1, y}, {x+2, y}};
        }
        //VERT U
        if (y-1 >= 0 && y+2 < 13 && stones[x][y-1] != null && stones[x][y+2] != null && stones[x][y+1] != null && stones[x][y-1].getStoneType() == type && stones[x][y+1].getStoneType() == type && stones[x][y+2].getStoneType() == type) {
            stones[x][y].hBlast = true;
            return new int[][]{{x, y-1}, {x, y+1}, {x, y+2}};
        }
        //VERT D
        if (y-2 >= 0 && y+1 < 13 && stones[x][y-2] != null && stones[x][y-1] != null && stones[x][y+1] != null && stones[x][y-1].getStoneType() == type && stones[x][y+1].getStoneType() == type && stones[x][y-2].getStoneType() == type) {
            stones[x][y].hBlast = true;
            return new int[][]{{x, y-2}, {x, y-1}, {x, y+1}};
        }

        if (x-1 >= 0 && x+1 < 18 && stones[x-1][y] != null && stones[x+1][y] != null && stones[x-1][y].getStoneType() == type && stones[x+1][y].getStoneType() == type){
            return new int[][]{{x-1, y}, {x, y}, {x+1, y}};
        }
        if (y-1 >= 0 && y+1 < 13 && stones[x][y-1] != null && stones[x][y+1] != null && stones[x][y-1].getStoneType() == type && stones[x][y+1].getStoneType() == type){
            return new int[][]{{x, y-1}, {x, y}, {x, y+1}};
        }
        if (x-2 >= 0 && stones[x-2][y] != null && stones[x-1][y] != null && stones[x-2][y].getStoneType() == type && stones[x-1][y].getStoneType() == type){
            return new int[][]{{x-2, y}, {x-1, y}, {x, y}};
        }
        if (x+2 < 18 && stones[x+2][y] != null && stones[x+1][y] != null && stones[x+2][y].getStoneType() == type && stones[x+1][y].getStoneType() == type){
            return new int[][]{{x+2, y}, {x+1, y}, {x, y}};
        }
        if (y-2 >= 0 && stones[x][y-2] != null && stones[x][y-1] != null && stones[x][y-2].getStoneType() == type && stones[x][y-1].getStoneType() == type){
            return new int[][]{{x, y-2}, {x, y-1}, {x, y}};
        }
        if (y+2 < 13 && stones[x][y+2] != null && stones[x][y+1] != null && stones[x][y+2].getStoneType() == type && stones[x][y+1].getStoneType() == type){
            return new int[][]{{x, y+2}, {x, y+1}, {x, y}};
        }

        return null;
    }

    public void spawnNewStone(int x){
        int a = 0;
        Random random = new Random();

        while (a == 0) {
            int ran = random.nextInt(6);
            StoneType type = StoneType.RED;

            switch (ran) {
                case 0:
                    type = StoneType.RED;
                    break;
                case 1:
                    type = StoneType.BLUE;
                    break;
                case 2:
                    type = StoneType.GREEN;
                    break;
                case 3:
                    type = StoneType.YELLOW;
                    break;
                case 4:
                    type = StoneType.ORANGE;
                    break;
                case 5:
                    type = StoneType.WHITE;
                    break;
            }

            int lastEmptyTile = -1;

            for (int i = 0; i < 13; i++) {
                if (tiles[x][i] != null && tiles[x][i].isEmpty()) {
                    lastEmptyTile = i;
                }
            }

            if (lastEmptyTile == -1)
                return;

            if (shouldGenNew(x, lastEmptyTile, type))
                continue;

            tiles[x][lastEmptyTile].toggleEmpty();

            int y = 0;
            for (int i = 0; i < 13; i++) {
                if (tiles[x][i] != null){
                    y = i;
                    break;
                }
            }

            Stone stone = new Stone(toScreenCoords(x), toScreenCoords(y-1), type, lastEmptyTile * FieldConstants.TILE_SIZE + FieldConstants.OFFSET);

            stones[x][lastEmptyTile] = stone;
            a++;
        }
    }

    private boolean shouldGenNew(int x, int lastEmptyTile, StoneType type){
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;

        for (int xx = 0; xx <= 2; xx++) {
            for (int yy = 0; yy <= 2; yy++) {
                if ((xx == 0) ^ (yy == 0)){
                    if (xx == 1){
                        if (x + 1 <= 17){
                            if (stones[x + 1][lastEmptyTile] != null) {
                                if (stones[x+1][lastEmptyTile].getStoneType() == type){
                                    right = true;
                                }
                            }
                        }
                        if (x - 1 >= 0){
                            if (stones[x - 1][lastEmptyTile] != null) {
                                if (stones[x-1][lastEmptyTile].getStoneType() == type) {
                                    left = true;
                                }
                            }
                        }
                    }
                    if (yy == 1){
                        if (lastEmptyTile + 1 <= 12){
                            if (stones[x][lastEmptyTile+1] != null) {
                                if (stones[x][lastEmptyTile+1].getStoneType() == type) {
                                    bottom = true;
                                }
                            }
                        }
                        if (lastEmptyTile - 1 >= 0){
                            if (stones[x][lastEmptyTile-1] != null) {
                                if (stones[x][lastEmptyTile-1].getStoneType() == type) {
                                    top = true;
                                }
                            }
                        }
                    }
                    if (xx == 2){
                        if (x + 2 <= 17){
                            if (stones[x+2][lastEmptyTile] != null) {
                                if (stones[x+2][lastEmptyTile].getStoneType() == type && right) {
                                    return true;
                                }
                            }
                        }
                        if (x - 2 >= 0){
                            if (stones[x-2][lastEmptyTile] != null) {
                                if (stones[x-2][lastEmptyTile].getStoneType() == type && left) {
                                    return true;
                                }
                            }
                        }
                    }
                    if (yy == 2){
                        if (lastEmptyTile + 2 <= 12){
                            if (stones[x][lastEmptyTile+2] != null) {
                                if (stones[x][lastEmptyTile+2].getStoneType() == type && bottom) {
                                    return true;
                                }
                            }
                        }
                        if (lastEmptyTile - 2 >= 0){
                            if (stones[x][lastEmptyTile-2] != null) {
                                if (stones[x][lastEmptyTile-2].getStoneType() == type && top) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;

    }
}
