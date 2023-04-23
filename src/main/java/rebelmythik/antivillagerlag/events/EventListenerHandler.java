package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;



public class EventListenerHandler implements Listener {

    public AntiVillagerLag plugin;
    public BlockAI blockAi;
    public NameTagAI nameTagAI;

    public RadiusWorkBlock radiusWorkBlock;
    public RestockVillager restockVillager;
    public VillagerLevelManager villagerLevelManager;

    public ConvertNewVillager convertNewVillager;

    ColorCode colorCodes = new ColorCode();

    public EventListenerHandler(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.blockAi = new BlockAI(plugin);
        this.nameTagAI = new NameTagAI(plugin);
        this.restockVillager = new RestockVillager(plugin);
        this.villagerLevelManager = new VillagerLevelManager(plugin);
        this.convertNewVillager = new ConvertNewVillager(plugin);
        this.radiusWorkBlock = new RadiusWorkBlock(plugin);
    }

    public void sanityChecks(Villager vil, long currentTime){

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);
        long vilCooldown = VillagerUtilities.getRestockCooldown(vil, plugin);
        long vilTime = VillagerUtilities.getTime(vil, plugin);

        if(vilLevelCooldown > currentTime + villagerLevelManager.cooldown * 2)
            VillagerUtilities.setLevelCooldown(vil, plugin, villagerLevelManager.cooldown);

        if(vilCooldown > currentTime + blockAi.cooldown * 2)
            VillagerUtilities.setNewRestockCooldown(vil, plugin, blockAi.cooldown);

        if(vilTime > vil.getWorld().getFullTime())
            VillagerUtilities.setNewTime(vil, plugin);
    }

    @EventHandler
    public void inventoryMove(InventoryClickEvent event) {
        if (!plugin.getConfig().getBoolean("toggleableoptions.preventtrading")) return;
        if (!(event.getInventory().getHolder() instanceof Villager)) return;
        Villager vil = (Villager) event.getInventory().getHolder();
        if (VillagerUtilities.isDisabled(vil, plugin)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        player.closeInventory();
        player.sendMessage(colorCodes.cm(plugin.getConfig().getString("messages.VillagerMustBeDisabled")));
    }
    @EventHandler
    public void villagerTradeClick(TradeSelectEvent event) {
        if (!plugin.getConfig().getBoolean("toggleableoptions.preventtrading")) return;
        if (!(event.getInventory().getHolder() instanceof Villager)) return;
        Villager vil = (Villager) event.getInventory().getHolder();
        if (VillagerUtilities.isDisabled(vil, plugin)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        player.closeInventory();
        player.sendMessage(colorCodes.cm(plugin.getConfig().getString("messages.VillagerMustBeDisabled")));
    }
    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e){

        Player player = e.getPlayer();
        if(player.hasPermission("avl.disable"))
            return;
        // do checks and setup
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;

        Villager vil = (Villager) e.getRightClicked();

        // check whether this villager has tags
        if (!VillagerUtilities.hasRestockCooldown(vil, plugin)) VillagerUtilities.setNewRestockCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasTime(vil, plugin)) VillagerUtilities.setNewTime(vil, plugin);
        if (!VillagerUtilities.hasDisabledByNametag(vil, plugin)) VillagerUtilities.setDisabledByNametag(vil, plugin, false);
        if (!VillagerUtilities.hasDisabledByBlock(vil, plugin)) VillagerUtilities.setDisabledByBlock(vil, plugin, false);
        if (!VillagerUtilities.hasDisabledByWorkstation(vil, plugin)) VillagerUtilities.setDisabledByWorkstation(vil, plugin, false);
        if (!VillagerUtilities.hasDisabledByCommand(vil, plugin)) VillagerUtilities.setDisabledByCommand(vil, plugin, false);

        long currentTime = System.currentTimeMillis() / 1000;

        // If time or AI is broken fix it
        sanityChecks(vil, currentTime);
        convertNewVillager.call(vil, player);

        long timeThatVilLevelCooldownEnds = VillagerUtilities.getLevelCooldown(vil, plugin);

        long timeTillVilLevelCooldownEnds = timeThatVilLevelCooldownEnds - currentTime;

        // Check if the villager is disabled for leveling and send a message
        if (VillagerUtilities.isDisabled(vil, plugin)) {
            if (timeThatVilLevelCooldownEnds > currentTime) {
                String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(timeTillVilLevelCooldownEnds));
                player.sendMessage(colorCodes.cm(message));
                vil.shakeHead();
                e.setCancelled(true);
                return;
            }
        }

        // handle Nametag Ai, check if it is already disabled by block
        if (plugin.getConfig().getBoolean("toggleableoptions.userenaming")) {
            //change to update DISABLED_BY_NAMETAG_KEY
            nameTagAI.call(vil, player, e);
        }
        // handle Block Ai, check if nametag cancelled event (avoid duplicate error?)
        if (plugin.getConfig().getBoolean("toggleableoptions.useblocks")) {
            //change to update DISABLED_BY_BLOCK_KEY
            blockAi.call(vil, player);
        }
        // handle RadiusWorkBlock, check if disabled by workstation
        if (plugin.getConfig().getBoolean("toggleableoptions.useworkstations")) {
            //change to update DISABLED_BY_WORKSTATION_KEY
            radiusWorkBlock.call(vil, player);
        }
        //RadiusOptimizeCommand, check if disabled by workstation (for later)

        // disable if it should be disabled
        if (VillagerUtilities.isDisabled(vil, plugin)) {
            VillagerUtilities.disableTheVillager(vil, plugin);
        } else {
            VillagerUtilities.undisableTheVillager(vil, plugin);
        }


        // handle Restock, check if Villager is disabled before
        if (VillagerUtilities.isDisabled(vil, plugin)) {
            restockVillager.call(vil, player);
        }

    }

    @EventHandler
    public void afterTrade(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();
        if(player.hasPermission("avl.disable"))
            return;
        // check if inventory belongs to a Villager Trade Screen
        if (event.getInventory().getHolder() instanceof WanderingTrader) return;

        if(event.getInventory().getType() != InventoryType.MERCHANT) return;

        if (event.getInventory().getHolder() == null) return;

        Villager vil = (Villager) event.getInventory().getHolder();

        if (!VillagerUtilities.isDisabled(vil, plugin)) return;
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);

        villagerLevelManager.call(vil, player);
    }

    @EventHandler
    public void onCancelVillagerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Villager && event.getDamager() instanceof Zombie)) return;

        Villager vil = (Villager) event.getEntity();

        if (VillagerUtilities.hasMarker(vil, plugin)) {
            event.setCancelled(true);
        }
    }
}


