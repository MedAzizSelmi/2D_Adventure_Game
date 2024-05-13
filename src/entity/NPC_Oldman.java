package entity;


import java.util.Random;
import main.GamePanel;


public class NPC_Oldman extends Entity {
    
    public NPC_Oldman(GamePanel gp){
        super(gp);
        
        direction = "down";
        speed = 1;
        
        getImage();
        setDialogue();
    }
    public void getImage(){
        
        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }
    public void setDialogue(){
        
        dialogues[0] = "Hello there young fella!";
        dialogues[1] = "You came to this island looking for the treasure?";
        dialogues[2] = "I spend all of my life to find it and I couldn't... \nNow i became old for this shit";
        dialogues[3] = "Anyway... \nMaybe you'll find it with some luck";
        
    }
    public void setAction(){
        
        actionLockCounter++;
        //small IA for npc movement
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100)+1;
        
            if (i < 25){
                direction = "up";
            }
            if (i > 25 && i <= 50){
                direction = "down";
            }
            if (i > 50 && i <= 75){
                direction = "left";
            }
            if (i > 75 && i <= 100){
                direction = "right";
            }
            actionLockCounter = 0;
        }
        
    }
    public void speak(){
        
        super.speak();
    }
}
