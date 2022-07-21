package rebelmythik.antivillagerlag.utils;


import org.bukkit.entity.Villager;

public class CalculateLevel {

<<<<<<< HEAD
    public static long villagerEXP (Villager vil) {
=======
    public static int villagerEXP (Villager vil) {
>>>>>>> 82c7ab4 (test)
        int vilEXP = vil.getVillagerExperience();
        int vilLVL = vil.getVillagerLevel();

        // Level 2 Calculation
<<<<<<< HEAD
=======
        if (vilLVL == 1 && vilEXP < 10) return 1;
>>>>>>> 82c7ab4 (test)
        if (vilLVL == 1 && vilEXP >= 10 && vilEXP < 70) return 2;
        if (vilLVL == 1 && vilEXP >= 70 && vilEXP < 150) return 3;
        if (vilLVL == 1 && vilEXP >= 150 && vilEXP < 250) return 4;
        if (vilLVL == 1 && vilEXP >= 250) return 5;

        // Level 3 Calculation
<<<<<<< HEAD
=======
        if (vilLVL == 2 && vilEXP < 70) return 2;
>>>>>>> 82c7ab4 (test)
        if (vilLVL == 2 && vilEXP >= 70) return 3;
        if (vilLVL == 2 && vilEXP >= 150) return 4;
        if (vilLVL == 2 && vilEXP >= 250) return 5;

        // Level 4 Calculation
<<<<<<< HEAD
=======
        if (vilLVL == 2 && vilEXP < 150) return 3;
>>>>>>> 82c7ab4 (test)
        if (vilLVL == 3 && vilEXP >= 150) return 4;
        if (vilLVL == 3 && vilEXP >= 250) return 5;

        // Level 5 Calculation
<<<<<<< HEAD
        if (vilLVL == 4 && vilEXP >= 250) return 5;

        return 0;
=======
        if (vilLVL == 2 && vilEXP < 150) return 4;
        if (vilLVL == 4 && vilEXP >= 250) return 5;

        return 1;
    }

    public static void updateLevel(Villager vil) {
        vil.setVillagerLevel(villagerEXP(vil));
>>>>>>> 82c7ab4 (test)
    }
}
