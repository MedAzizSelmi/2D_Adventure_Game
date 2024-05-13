package main;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    //Screen Settings
    final int originalTileSize=16;
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;//48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;//960px
    public final int screenHight = tileSize * maxScreenRow;//576px
    //World map settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    //full screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHight;
    BufferedImage tempScreen;
    Graphics2D g2;
    //FPS
    int FPS = 60;
    
    //system
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionDetecter CDetecter = new CollisionDetecter(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;
    
    //entity and object
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    //game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int optionsState = 4;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    public void setupGame(){
        
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        gameState = titleState;
        tempScreen = new BufferedImage(screenWidth, screenHight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
        setFullScreen();
    }
    public void setFullScreen(){
        //get local screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        //get full screen width and height
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run(){
        
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while(gameThread != null) {
            
            //Update info (exp: character pos)
            update();
            
            //Draw the screen with the updated info
            drawToTempScreen();//draw everything to the buffered image
            drawToScreen();//draw the buffared image to the screen
            
            try {
                double remainingTime = nextDrawTime -  System.nanoTime();
                remainingTime = remainingTime/1000000;
                
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                
                Thread.sleep((long) remainingTime);
                
                nextDrawTime += drawInterval;
                
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
    }
    public void update(){
        
        if(gameState == playState){
            player.update();
            
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if(monster[i].alive == true && monster[i].dying == false){
                        monster[i].update();
                    }
                    if(monster[i].alive == false){
                        monster[i] = null;
                    }
                }
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive == true){
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }
        }
        if(gameState == pauseState){
            //nothing
        }
        
    }
    public void drawToTempScreen(){
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
           drawStart = System.nanoTime(); 
        }
        
        if(gameState == titleState){
            ui.draw(g2);
        }
        else{
            
            tileM.draw(g2);
            
            entityList.add(player);
            
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }
            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                } 
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    entityList.add(monster[i]);
                } 
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                } 
            }
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }
            entityList.clear();
            
        
            ui.draw(g2);
        }
        
        if(keyH.checkDrawTime == true){
         long drawEnd = System.nanoTime();
        long passed = drawEnd - drawStart;
        g2.setColor(Color.white);
        g2.drawString("Draw time: " + passed, 10, 400);
        System.out.println("Draw time: " + passed);   
        }
    }
    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    
    public void playMusic(int i){
        
        music.setFile(i);
        music.play();
        music.loop();
        
        
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
        
        se.setFile(i);
        se.play();
    }
}
