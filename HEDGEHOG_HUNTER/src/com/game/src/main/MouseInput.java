package com.game.src.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{


    public void mouseClicked(MouseEvent e) {

    }


    public void mousePressed(MouseEvent e) {

        int mx = e.getX();
        int my = e.getY();



        //play button
        if(mx > Game.WIDTH / 2 + 280 && mx <= Game.WIDTH / 2 + 380){
            if(my >= 150 && my <= 200){
                //нажатие кнопки play
                Game.State = Game.STATE.GAME;
            }
        }

        //quit button
        if(mx > Game.WIDTH / 2 + 280 && mx <= Game.WIDTH / 2 + 380){
            if(my >= 250 && my <= 300){
                //нажатие кнопки quit
                System.exit(1);
            }
        }

    }


    public void mouseReleased(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }


    public void mouseExited(MouseEvent e) {

    }



}
