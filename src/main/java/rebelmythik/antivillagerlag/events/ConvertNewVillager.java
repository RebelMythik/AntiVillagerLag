package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class ConvertNewVillager {
    private final AntiVillagerLag plugin;

    public ConvertNewVillager(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    public void call(Villager vil, Player player) {
        if (!vil.hasAI()) {
            vil.setAI(true);
            vil.setAware(false);
        }
    }
}
