package com.game.src.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Menu {

    public Rectangle playButton = new Rectangle(Game.WIDHT / 2 + 120, 150, 100, 50);

    public Rectangle quitButton = new Rectangle(Game.WIDHT / 2 + 120, 250, 100, 50);
    public static final int WIDHT = 320;// ширина окна final - потому что он не может изменится
    public static final int HEIGHT = WIDHT / 12 * 9;

    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;



        Font fnt0 = new Font("impact", Font.PLAIN, 50);
        g.setFont(fnt0);
        g.setColor(Color.yellow);
        g.drawString("N R T", Game.WIDTH / 2 + 280 , 100);

        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.drawString("PLAY", playButton.x + 12, playButton.y + 37);
        g2d.draw(playButton);

        g.drawString("QUIT", quitButton.x + 12, quitButton.y + 37);
        g2d.draw(quitButton);
    }
}
