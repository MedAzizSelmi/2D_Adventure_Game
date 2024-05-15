package main;

import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;



public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum1 = 0;
    public int commandNum2 = 0;
    //public int titleScreenState = 0;
    int slotCol = 0;
    int slotRow = 0;
    int subState = 0;
    
    
    public UI(GamePanel gp){
        this.gp = gp;
        
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
       
    }
    
    public void addMessage(String text){
        
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2){
        
        this.g2 = g2;
        
        g2.setFont(maruMonica);
        g2.setColor(Color.white);
        
        //title state
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        //play state
        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }
        //pause state
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawInventoryScreen();
        }
        //dialogue state
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
        if(gp.gameState == gp.optionsState){
            drawOptionsScreen();
        }
        //game over state
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        
    }
    public void drawPlayerLife(){
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;
        //draw maxlife
        while(i < gp.player.maxLife/2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }
        //reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        //draw current life
        i = 0;
        while(i < gp.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
        //draw max mana
        x = (gp.tileSize/2)-5;
        y = (int) (gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.maxMana){
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }
        // reset
        x = (gp.tileSize/2)-5;
        y = (int) (gp.tileSize*1.5);
        //draw mana
        i = 0;
        while(i < gp.player.mana){
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
        
    }
    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;
                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            } 
        }
    }
    public void drawTitleScreen(){
        
        
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHight);
        
            //title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Blue Boy Adventure";
            int x = getXforCenterText(text);
            int y = gp.tileSize*3;
            g2.setColor(Color.GRAY);
            g2.drawString(text, x+3, y+3);
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);
        
            x += gp.screenWidth/2 - 1.9*(gp.tileSize*2);
            y += gp.tileSize*2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
        
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        
            text = "NEW GAME";
            x = getXforCenterText(text);
            y += gp.tileSize*3.5;
            g2.drawString(text, x, y);
            if(commandNum1 == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }
        
            text = "LOAD GAME";
            x = getXforCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum1 == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }
        
            text = "QUIT GAME";
            x = getXforCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum1 == 2){
                g2.drawString(">", x-gp.tileSize, y);
            }
        
    }
    public void drawPauseScreen(){
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = gp.screenWidth - gp.tileSize*12;        
        int y = gp.screenHight - gp.tileSize*10 - gp.tileSize/2;
        g2.drawString(text, x, y);
        
        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize*2;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));
        
        int textX = frameX + 20 ;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize;
        String value;
        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.defence);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize ;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);
        
        
    }
    public void drawDialogueScreen(){
        
        //window
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;
        
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawInventoryScreen(){
        int frameX = gp.tileSize*12;
        int frameY = gp.tileSize*2;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;
        for(int i = 0; i < gp.player.inventory.size(); i++){
            if(gp.player.inventory.get(i) == gp.player.currentWeapon || 
                    gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if(i == 4 || i == 9 || i == 14){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        int itemIndex = getItemIndexForSlot();
        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String Line: gp.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(Line, textX, textY);
                textY += 32;
            }
            
        }
        
    }
    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHight);
        
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
        text = "GAME OVER";
        
        //shadow
        g2.setColor(Color.black);
        x = getXforCenterText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);
        
        //main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);
        
        //retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenterText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum2 == 0){
            g2.drawString(">", x-40, y);
        }
        
        //back title screen
        text = "Quit";
        x = getXforCenterText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum2 == 1){
            g2.drawString(">", x-40, y);
        }
        
    }
    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        switch(subState){
            case 0: options_top(frameX, frameY); break;
            case 1: options_fullScreenNotification(frameX, frameY); break;
            case 2: options_control(frameX, frameY); break;
            case 3: options_endGameConfirmation(frameX, frameY); break;
        }
        gp.keyH.enterPressed = false;
    }
    public void options_top(int frameX, int frameY){
        int textX;
        int textY;
        
        //title
        String text = "Options";
        textX = getXforCenterText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        //full screen on/off
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full Screen", textX, textY);
        if(commandNum2 == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                if(gp.fullScreenOn == false){
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn == true){
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }
        
        //music
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum2 == 1){
            g2.drawString(">", textX-25, textY);
        }
        
        
        //sound effects
        textY += gp.tileSize;
        g2.drawString("Sound Effects", textX, textY);
        if(commandNum2 == 2){
            g2.drawString(">", textX-25, textY);
        }
        
        
        //controle
        textY += gp.tileSize;
        g2.drawString("Controle", textX, textY);
        if(commandNum2 == 3){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 2;
                commandNum2 = 0;
            }
        }
        
        //save and quit
        textY += gp.tileSize;
        g2.drawString("Main Menu", textX, textY);
        if(commandNum2 == 4){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 3;
                commandNum2 = 0;
            }
        }
        
        //back
        textY += gp.tileSize*2;
        g2.drawString("Back To The Game", textX, textY);
        if(commandNum2 == 5){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.playState;
                gp.playMusic(0);
                commandNum2 = 0;
            }
        }
        
        //full screen checkbox
        textX = (int) (frameX + gp.tileSize*4.5);
        textY = frameY + gp.tileSize*2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if(gp.fullScreenOn == true){
            g2.fillRect(textX, textY, 24, 24);
        }
        
        //music volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        //SE volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        gp.config.saveConfig();
    }
    public void options_fullScreenNotification(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        
        currentDialogue = "The change will take \neffect after restarting \nthe game.";
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        
        //back
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        g2.drawString(">", textX-25, textY);
        if(gp.keyH.enterPressed == true){
            subState = 0;
        }
        
    }
    public void options_control(int frameX, int frameY){
        int textX;
        int textY;
        
        //title
        String text = "Control";
        textX = getXforCenterText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY); textY += gp.tileSize;
        g2.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
        g2.drawString("Shoot", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause Screen", textX, textY); textY += gp.tileSize;
        g2.drawString("Options", textX, textY); 
        textX = frameX + gp.tileSize*5;
        textY = frameY + gp.tileSize*2;
        g2.drawString("W/A/S/D", textX, textY);textY += gp.tileSize;
        g2.drawString("Enter", textX, textY);textY += gp.tileSize;
        g2.drawString("F", textX, textY);textY += gp.tileSize;
        g2.drawString("P", textX, textY);textY += gp.tileSize;
        g2.drawString("Esc", textX, textY);
        
        //back
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        g2.drawString(">", textX-25, textY);
        if(gp.keyH.enterPressed == true){
            subState = 0;
            commandNum2 = 3;
        }
    }
    public void options_endGameConfirmation(int frameX, int frameY){
        int textX;
        int textY;
        String text = "Back To Main Menu?";
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*3;
        g2.drawString(text, textX, textY);
        //yes
        text = "Yes";
        textX = getXforCenterText(text);
        textY += gp.tileSize*3;
        g2.drawString(text, textX, textY);
        if(commandNum2 == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }
        
        //no
        text = "No";
        textX = getXforCenterText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum2 == 1){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum2 = 4;
            }
        }
    }
    public int getItemIndexForSlot(){
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }
    public void drawSubWindow(int x, int y, int width, int height){
        
        Color c = new Color(0,0,0,220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    public int getXforCenterText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    public int getXforAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}
