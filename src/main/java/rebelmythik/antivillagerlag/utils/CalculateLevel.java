package rebelmythik.antivillagerlag.utils;

import org.bukkit.entity.Villager;

public class CalculateLevel {
    public static long villagerEXP (Villager vil) {
        int vilEXP = vil.getVillagerExperience();
        int vilLVL = vil.getVillagerLevel();

        // Level 2 Calculation
        if (vilLVL == 1 && vilEXP < 10) return 1;
        if (vilLVL == 1 && vilEXP >= 10 && vilEXP < 70) return 2;
        if (vilLVL == 1 && vilEXP >= 70 && vilEXP < 150) return 3;
        if (vilLVL == 1 && vilEXP >= 150 && vilEXP < 250) return 4;
        if (vilLVL == 1 && vilEXP >= 250) return 5;

        // Level 3 Calculation
        if (vilLVL == 2 && vilEXP >= 10 && vilEXP < 70) return 2;
        if (vilLVL == 2 && vilEXP >= 70 && vilEXP < 150) return 3;
        if (vilLVL == 2 && vilEXP >= 150 && vilEXP < 250) return 4;
        if (vilLVL == 2 && vilEXP >= 250) return 5;

        // Level 4 Calculation
        if (vilLVL == 3 && vilEXP >= 70 && vilEXP < 150) return 3;
        if (vilLVL == 3 && vilEXP >= 150 && vilEXP < 250) return 4;
        if (vilLVL == 3 && vilEXP >= 250) return 5;

        //Level 5 Calculation
        if (vilLVL == 4 && vilEXP >= 150 && vilEXP < 250) return 4;
        if (vilLVL == 4 && vilEXP >= 250) return 5;

        if (vilLVL == 5 && vilEXP >= 250) return 5;

        return 0;
    }
}
