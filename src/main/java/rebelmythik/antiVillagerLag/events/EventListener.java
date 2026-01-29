package rebelmythik.antiVillagerLag.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.UpdateChecker;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class EventListener implements Listener {

    AntiVillagerLag plugin;


    public EventListener(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        //  It's a villager
        if (!event.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        Villager villager = (Villager) event.getRightClicked();

        //  Setup new Villagers
        if (!VillagerUtilities.hasMarker(villager, plugin)) {
            VillagerUtilities.setAiCooldown(villager, plugin, 0L);
            VillagerUtilities.setLevelCooldown(villager, plugin, 0L);
            VillagerUtilities.setLastRestock(villager, plugin);
            VillagerUtilities.setMarker(villager, plugin, true);
        }

        //  Get Times
        long currentTime = System.currentTimeMillis() / 1000;
        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(villager, plugin);
        long vilAiCooldown = VillagerUtilities.getAiCooldown(villager, plugin);
        long totalSeconds = vilAiCooldown - currentTime;
        long sec = totalSeconds % 60;
        long min = totalSeconds / 60;

        //  If the villager is leveling up
        if (vilLevelCooldown > currentTime) {
            String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
            long level_sec = vilLevelCooldown - currentTime;
            message = message.replaceAll("%avlseconds%", Long.toString(level_sec));
            event.getPlayer().sendMessage(VillagerUtilities.colorcodes.cm(message));
            villager.shakeHead();
            event.setCancelled(true);
            return;
        }

        //  Should it be disabled?
        boolean nametag_result = NameTagAI.call(villager, plugin, player);
        boolean block_result = BlockAI.call(villager, plugin, player);
        boolean workblock_result = WorkblockAI.call(villager, plugin, player);
        boolean should_be_disabled = nametag_result || block_result || workblock_result;


        //  If villager AI is being toggled
        if (should_be_disabled == VillagerUtilities.getMarker(villager, plugin)) {
            //  If toggling is on cooldown
            if ((vilAiCooldown > currentTime) && !player.hasPermission("avl.cooldown.bypass")) {
                //Tell player it's on cooldown
                String message = plugin.getConfig().getString("messages.cooldown-ai-message");
                message = message.replaceAll("%avlminutes%", Long.toString(min));
                message = message.replaceAll("%avlseconds%", Long.toString(sec));
                event.getPlayer().sendMessage(VillagerUtilities.colorcodes.cm(message));
                event.setCancelled(true);
            //  If cooldown is over
            } else {
                VillagerUtilities.setMarker(villager, plugin, !should_be_disabled);
                villager.setAware(!should_be_disabled);
                if (plugin.getConfig().getBoolean("toggleableoptions.silence_villagers")) {
                    villager.setSilent(should_be_disabled);
                }
                VillagerUtilities.setAiCooldown(villager, plugin, plugin.getConfig().getLong("ai-toggle-cooldown"));
                //  If nametag shouldn't be consumed, give one back
                if (player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG) && !plugin.getConfig().getBoolean("toggleableoptions.usenametags")) {
                    ItemStack nametag = player.getInventory().getItemInMainHand();
                    if (!nametag.getItemMeta().hasDisplayName()) return;
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                }
            }
        }
        //  If the villager AI is not being toggled
        else {
            //  If nametag shouldn't be consumed, give one back
            if (!VillagerUtilities.hasMarker(villager, plugin)) return;
            if (player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG) && !plugin.getConfig().getBoolean("toggleableoptions.usenametags")) {
                ItemStack nametag = player.getInventory().getItemInMainHand();
                if (!nametag.getItemMeta().hasDisplayName()) return;
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
            }
        }

        //  Restock
        if (!VillagerUtilities.getMarker(villager, plugin)) {
            RestockVillager.call(villager, plugin, player);
        }
    }

    // Code for forcing players to disable villagers

    @EventHandler
    public void inventoryMove(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!plugin.getConfig().getBoolean("toggleableoptions.preventtrading")) return;
        if (!(event.getInventory().getHolder() instanceof Villager)) return;
        Villager vil = (Villager) event.getInventory().getHolder();
        if (!VillagerUtilities.hasMarker(vil, plugin)) return;
        if (!VillagerUtilities.getMarker(vil, plugin)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        //player.closeInventory();
        player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.VillagerMustBeDisabled")));
    }

    @EventHandler
    public void villagerTradeClick(TradeSelectEvent event) {
        if (event.isCancelled()) return;
        if (!plugin.getConfig().getBoolean("toggleableoptions.preventtrading")) return;
        if (!(event.getInventory().getHolder() instanceof Villager)) return;
        Villager vil = (Villager) event.getInventory().getHolder();
        if (!VillagerUtilities.hasMarker(vil, plugin)) return;
        if (!VillagerUtilities.getMarker(vil, plugin)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        player.closeInventory();
        player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.VillagerMustBeDisabled")));
    }

    // Event to handle cancellation of damage to villagers disable by the plugin
    @EventHandler
    public void onCancelVillagerDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Villager && event.getDamager() instanceof Zombie)) return;

        Villager vil = (Villager) event.getEntity();

        if (VillagerUtilities.hasMarker(vil, plugin) && !VillagerUtilities.getMarker(vil, plugin)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("avl.notify.update")) return;
        new UpdateChecker(plugin, 102949).getVersion(version -> {
            if (plugin.getDescription().getVersion().equals(version)) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "AntiVillagerLag is up to date!");
            } else {
                event.getPlayer().sendMessage(ChatColor.GREEN + "There is an update for AntiVillagerLag! https://www.spigotmc.org/resources/antivillagerlag.102949/");
            }
        });
    }

    // Event to handle Villager updating
    @EventHandler
    public void afterTrade(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();
        if(player.hasPermission("avl.disable"))
            return;
        // check if inventory belongs to a Villager Trade Screen
        if (event.getInventory().getHolder() == null) return;
        if (event.getInventory().getHolder() instanceof WanderingTrader) return;
        if(event.getInventory().getType() != InventoryType.MERCHANT) return;

        Villager vil = (Villager) event.getInventory().getHolder();
        // make sure the villager is disabled
        if (!VillagerUtilities.hasMarker(vil, plugin)) return;
        if (VillagerUtilities.getMarker(vil, plugin)) return;

        // handle leveling
        VillagerLevelManager.call(vil, plugin, player);
    }

}
