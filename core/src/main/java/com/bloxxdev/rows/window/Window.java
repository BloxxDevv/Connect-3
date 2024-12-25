package com.bloxxdev.rows.window;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.TextureRegion;

public class Window {

    public void renderBackgroundTexture(Graphics g){
        TextureRegion region = new TextureRegion(WindowConstants.BACKGROUND_TEXTURE, WindowConstants.WIDTH, WindowConstants.HEIGHT);
        g.scale(1.5F,1.5F);
        g.drawTextureRegion(region, 0, 0);
        g.clearScaling();
    }

}
