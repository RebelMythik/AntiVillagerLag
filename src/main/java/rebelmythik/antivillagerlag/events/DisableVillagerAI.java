package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class DisableVillagerAI implements Listener {

    public AntiVillagerLag plugin;
    public String name4Idiots;

    public DisableVillagerAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void RightClick(PlayerInteractEntityEvent e) {

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        PlayerInventory inv = player.getInventory();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        ItemStack item;
        if (e.getHand().equals(EquipmentSlot.HAND)) {
            if (!inv.getItemInMainHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInMainHand();
            inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount() + 1);
        } else {
            if (!inv.getItemInOffHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInOffHand();
            inv.getItemInOffHand().setAmount(inv.getItemInOffHand().getAmount() + 1);
        }

        if (!item.getItemMeta().getDisplayName().equals(plugin.getConfig().getString("NameThatDisables"))) return;
        vil.setAI(false);
    }
}
