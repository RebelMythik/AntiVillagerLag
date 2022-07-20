package rebelmythik.antivillagerlag.utils;


import org.bukkit.entity.Villager;

public class CalculateLevel {

    public static long villagerEXP (Villager vil) {
        int vilEXP = vil.getVillagerExperience();
        int vilLVL = vil.getVillagerLevel();

        if (vilLVL == 1 && vilEXP < 10) return 1;

        return 5;
    }
}
