package rebelmythik.antivillagerlag.events;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;


public class BlockAI {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;

    public BlockAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }

    public void call(Villager vil, Player player) {

        Material belowvil = vil.getLocation().add(0, -1,0).getBlock().getType();

        // else check if Villager is disabled with Block
        List<String> blocksThatDisable = plugin.getConfig().getStringList("BlocksThatDisable");
        boolean willBeDisabled = blocksThatDisable.contains(belowvil.name());
        VillagerUtilities.setDisabledByBlock(vil, plugin, willBeDisabled);

    }
}
