package rebelmythik.antivillagerlag.utils;

import org.bukkit.entity.Villager;

public class CalculateLevel {
    public static long villagerEXP (Villager vil) {
        
        int vilEXP = vil.getVillagerExperience();
            
        // Villager Level depending on their XP
        // source: https://minecraft.fandom.com/wiki/Trading#Mechanics
        
        if (vilEXP >= 250) return 5;
        if (vilEXP >= 150) return 4;
        if (vilEXP >= 70) return 3;
        if (vilEXP >= 10) return 2;
        
        // default level is 1
        return 1;
    }
}
