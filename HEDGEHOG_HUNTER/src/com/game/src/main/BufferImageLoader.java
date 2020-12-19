package com.game.src.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class BufferImageLoader {

    private BufferedImage image;
    private double dx;
    private double dy;

    public BufferedImage loadImage(String path) throws IOException{
        image = ImageIO.read(getClass().getResource(path));
        return image;
    }

//    public void update() {
//    }

//    public void draw(Graphics2D g) {
//    }

//    public void setVector(double dx, double dy) {
//        this.dx = dx;
//        this.dy = dy;
//    }
}
