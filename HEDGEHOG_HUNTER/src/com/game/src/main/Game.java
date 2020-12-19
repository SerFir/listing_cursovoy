package com.game.src.main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;
//Контейнер предоставляет пространство, в котором может быть расположен компонент.
//JFrame — это окно верхнего уровня с заголовком и рамкой.
import javax.swing.JFrame;

//Интерфейс Runnable содержит только один метод run()
//Метод run() выполняется при запуске потока.
public class Game extends Canvas implements Runnable  {

    public static final long serialVersionUID = 1L;
    public static final int WIDHT = 320;// ширина окна final - потому что он не может изменится
    public static final int HEIGHT = WIDHT / 12 * 9;
    public static final int SCALE = 2;//коэфициент масштабирования
    public final String TITLE = "N R T";

    private boolean running = false;
    private Thread thread; //инициализируем новый поток

    private boolean eventFinish;
    private BufferedImage image = new BufferedImage(WIDHT, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage spriteSheet = null;
    private BufferedImage background = null;

    private boolean is_shooting = false;//если убрать это и еще в кейпресд то можно стрелять с зажатым пробелом

    private static int enemy_count = 3;//счетчик врагов
    private int enemy_killed = 0;

 private static Player p;
    private static Controller c;
    private static Textures tex;
    private Menu menu;
/////////////////////////
    private static long time;
///////////////////////////
    public LinkedList<EntityA> ea;
    public LinkedList<EntityB> eb;

    public static int HEALTH = 150;

    public static enum STATE{
        MENU,
        GAME,
        GAMEOVER
    };

///////////////////////////////////////////
    //CHASI
    public String getTimeToString() {

        int minutes = (int) (time / 3600);
        int seconds = (int) ((time % 3600) / 60);
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    public long getTime() {

        return time;
    }

    public void setTime(long t) {

        time = t;
    }

//////////////////////////////////////////


    public static STATE State = STATE.MENU;

    public void init(){

        //////////////////////////
        setTime(getTime());

        ////////////////////
        requestFocus();//до того как это было написано чтобы начать упавлять нужно было щелкуныуть по окну мышкой
        BufferImageLoader loader = new BufferImageLoader();
        try{
            spriteSheet = loader.loadImage("/sprite_sheet.png");
            background = loader.loadImage("/background.png"); // 700x500 background
        }catch(IOException e){
            e.printStackTrace();
        }

        tex = new Textures(this);
        c = new Controller(tex, this);
        p = new Player(200, 200, tex, this, c);

        menu = new Menu();


        ea = c.getEntityA();
        eb = c.getEntityB();

        this.addKeyListener(new KeyInput(this));
        this.addMouseListener(new MouseInput());

        c.createEnemy(enemy_count);
    }





    private synchronized void start(){
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running)
            return;
        try {
            thread.join();
        } catch (InterruptedException e/*ex*/) {
            // Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            e.printStackTrace();
        }
        System.exit(1);
    }

    public void run(){// этод метод вызавыется когда срабатывает Runnable это "сеpдце игры"
           init();
        long lastTime = System.nanoTime();// наносекунды
        final double amountOfTicks = 60.0;// FPS = 60
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while(running){
            //это игровой цикл
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                ////////////////
                time++;// счет таймера
                //////////////////////
                tick();
                updates++;
                delta--;
            }

            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + " Ticks, fps" + frames);
                updates = 0;//эти две строчки для того чтобы FPS сбрасывался до 60 иначе н увеличивается на 60
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        if(State == STATE.GAME){
            p.tick();
            c.tick();
        }   if(enemy_killed >= enemy_count){
            enemy_count += 1;
            enemy_killed = 0;
            c.createEnemy(enemy_count);//создает врагов когда предидущие убиты
        }

    }

    private void render(){

        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){

            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        /////////////////////////////////

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(background, 0, 0, null);

        if(State == STATE.GAME){
            p.render(g);
            c.render(g);
/////////////////////////////////
            g.setColor(java.awt.Color.WHITE);
            g.drawString(getTimeToString(), 600, 20);

////////////////////////////////////

            g.setColor(Color.gray);
            g.fillRect(5, 5, 150, 25);//длина полосы здоровья

            g.setColor(Color.yellow);
            g.fillRect(5, 5, HEALTH, 25);

            g.setColor(Color.white);
            g.drawRect(5, 5, 150, 25);


        }else if(State == STATE.MENU){
                //menu = new Menu();
                menu.render(g);
        }

        ////////////////////////////////
        g.dispose();
        bs.show();
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if(State == STATE.GAME){
            if(key == KeyEvent.VK_RIGHT){
                p.setVelX(5);
            }else if(key == KeyEvent.VK_LEFT){
                p.setVelX(-5);
            }else if(key == KeyEvent.VK_DOWN){
                p.setVelY(5);
            }else if(key == KeyEvent.VK_UP){
                p.setVelY(-5);
            }else if(key == KeyEvent.VK_SPACE && !is_shooting){//убрать && !is_shooting
                is_shooting = true;//убрать и ниже тоже
                c.addEntity(new Bullet(p.getX(), p.getY(), tex, this));
            }
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_RIGHT){
            p.setVelX(0);
        }else if(key == KeyEvent.VK_LEFT){
            p.setVelX(0);
        }else if(key == KeyEvent.VK_DOWN){
            p.setVelY(0);
        }else if(key == KeyEvent.VK_UP){
            p.setVelY(0);
        }else if(key == KeyEvent.VK_SPACE){
            is_shooting = false;//убрать
        }
    }

    public static void main(String args[]){
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDHT * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDHT * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDHT * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    public void update() {
        if(eventFinish) eventFinish();
        }


   public static void eventFinish() {


        if(HEALTH == 0) {

        HEALTH=150;

        enemy_count = 3;
        time=0;
        State=STATE.MENU;

        }

    }


    public BufferedImage getSpriteSheet(){
        return spriteSheet;
    }

    public int getEnemy_count(){
        return enemy_count;
    }

    public int getEnemy_killed(){
        return enemy_killed;
    }

    public void setEnemy_count(int enemy_count){
        this.enemy_count = enemy_count;
    }

    public void setEnemy_killed(int enemy_killed){
        this.enemy_killed = enemy_killed;
    }
}
