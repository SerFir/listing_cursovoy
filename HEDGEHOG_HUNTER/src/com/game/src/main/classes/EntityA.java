
package com.game.src.main.classes;

import java.awt.Graphics;
import java.awt.Rectangle;

/* Интерфейсы определяют некоторый функционал,
не имеющий конкретной реализации,
который затем реализуют классы, применяющие эти интерфейсы.
И один класс может применить множество интерфейсов.*/

public interface EntityA {

    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();

    public double getX();
    public double getY();
}
