package rebelmythik.antivillagerlag.utils;


import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCode {
    public String cm(String s) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        for(Matcher matcher = pattern.matcher(s); matcher.find(); matcher = pattern.matcher(s)) {
            String color = s.substring(matcher.start(), matcher.end());
            s = s.replace(color, ChatColor.of(color) + "");
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }
}

