package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    //debug
    boolean checkDrawTime = false;
    
    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        else if(gp.gameState == gp.playState){
            playState(code);
        }
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        else if(gp.gameState == gp.optionsState){
            optionsState(code);
        }
        else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }
    }
    public void titleState(int code){
 
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            gp.ui.commandNum1--;
            if(gp.ui.commandNum1 < 0){
                gp.ui.commandNum1 = 2;
            }
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            gp.ui.commandNum1++;
            if(gp.ui.commandNum1 > 2){
                gp.ui.commandNum1 = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum1 == 0){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum1 == 1){
                gp.saveLoad.load();
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum1 == 2){
                System.exit(0);
            }
        }
            
        
    }
    public void playState(int code){
        if(code == KeyEvent.VK_W){
                upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.pauseState;
            gp.stopMusic();    
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_F){
            shotKeyPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.optionsState;
            gp.stopMusic();
        }
        
        //debug
        if(code == KeyEvent.VK_T){
            if(checkDrawTime == false){
                checkDrawTime = true;
            }
            else if(checkDrawTime == true){
                checkDrawTime = false;
            }
        }
    }
    public void pauseState(int code){
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
            gp.playMusic(0);
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(8);
            }    
        }
        if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(8);
            }    
        }
        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(8);
            }    
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(8);
            }    
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void optionsState(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
            gp.playMusic(0);
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        int maxCommandNum2 = 0;
        switch(gp.ui.subState){
            case 0: maxCommandNum2 = 5; break;
            case 3: maxCommandNum2 = 1; break;
        }
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            gp.ui.commandNum2--;
            gp.playSE(8);
            if(gp.ui.commandNum2 < 0){
                gp.ui.commandNum2 = maxCommandNum2;
            }
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            gp.ui.commandNum2++;
            gp.playSE(8);
            if(gp.ui.commandNum2 > maxCommandNum2){
                gp.ui.commandNum2 = 0;
            }
        }
        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum2 == 1 && gp.music.volumeScale < 5){
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(8);
                }
                if(gp.ui.commandNum2 == 2 && gp.se.volumeScale < 5){
                    gp.se.volumeScale++;
                    gp.playSE(8);
                }
            }     
        }
        if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum2 == 1 && gp.music.volumeScale > 0){
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(8);
                }
                if(gp.ui.commandNum2 == 2 && gp.se.volumeScale > 0){
                    gp.se.volumeScale--;
                    gp.playSE(8);
                }
            }    
        }
        
    }
    public void gameOverState(int code){
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            gp.ui.commandNum2--;
            if(gp.ui.commandNum2 < 0){
                gp.ui.commandNum2 = 1;
            }
            gp.playSE(8);
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            gp.ui.commandNum2++;
            if(gp.ui.commandNum2 > 1){
                gp.ui.commandNum2 = 0;
            }
            gp.playSE(8);
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum2 == 0){
                gp.gameState = gp.playState;
                gp.Retry();
                gp.playMusic(0);
            }
            if(gp.ui.commandNum2 == 1){
                gp.gameState = gp.titleState;
                gp.Restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_F){
            shotKeyPressed = false;
        }
    }
    
}
