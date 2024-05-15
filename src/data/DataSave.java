package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSave implements Serializable {
    
    //player status
    int level;
    int maxLife;
    int life;
    int maxMana;
    int mana;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int coin;
    
    //player inventory
    ArrayList<String> itemNames = new ArrayList<>();
    int currentWeaponSlot;
    int currentShieldSlot;
}
