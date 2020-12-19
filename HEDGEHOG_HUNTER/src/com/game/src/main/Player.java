package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

public class Player extends GameObject implements EntityA{

    //переменные для улчшения управления(отклик сробатывает быстрее)
    private double velX = 0;
    private double velY = 0;
    private Textures tex;

    Game game;
    Controller controller;

    public Player(double x, double y, Textures tex, Game game, Controller controller){
        super(x,y);
        this.tex = tex;
        this.game = game;
        this.controller = controller;
    }

    public void tick(){
        x+=velX;
        y+=velY;
        //чтобы не вылетал за пределы экрана
        if(x <= 0)
            x = 0;
        if(x >= 625 - 16)
            x = 625 - 16;
        if(y <= 0)
            y = 0;
        if(y >= 470 - 32)
            y = 470 - 32;

        for(int i = 0; i < game.eb.size(); i++){
            EntityB tempEnt = game.eb.get(i);

            if(Physics.Collision(this, tempEnt)){
                controller.removeEntity(tempEnt);
                Game.HEALTH -= 50;
                if (Game.HEALTH==0){
                    Game.eventFinish();
                }
                game.setEnemy_killed(game.getEnemy_killed() + 1);////////////////////////
            }
        }

    }

    public Rectangle getBounds(){

        return new Rectangle((int)x, (int)y, 32, 32);
    }

    public void render(Graphics g){

        g.drawImage(tex.player, (int)x, (int)y, null);
    }

    public double getX(){

        return x;
    }

    public double getY(){

        return y;
    }

    public void setX(double x){

        this.x = x;
    }

    public void setY(double y){

        this.y = y;
    }

    public void setVelX(double velX){

        this.velX = velX;
    }

    public void setVelY(double velY){

        this.velY = velY;
    }

}
