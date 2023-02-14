package rebelmythik.antivillagerlag.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;



public class EventListenerHandler implements Listener {

    public AntiVillagerLag plugin;
    public BlockAI blockAi;
    public NameTagAI nameTagAI;
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
    }

    public void sanityChecks(Villager vil, long currentTime){

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);
        long vilTime = VillagerUtilities.getTime(vil, plugin);

        if(vilLevelCooldown > currentTime + villagerLevelManager.cooldown * 2)
            VillagerUtilities.setLevelCooldown(vil, plugin, villagerLevelManager.cooldown);

        if(vilCooldown > currentTime + blockAi.cooldown * 2)
            VillagerUtilities.setNewCooldown(vil, plugin, blockAi.cooldown);

        if(vilTime > vil.getWorld().getFullTime())
            VillagerUtilities.setNewTime(vil, plugin);
    }



    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e){
        Player player = e.getPlayer();
        if(player.hasPermission("avl.disable"))
            return;
        // do checks and setup
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;

        Villager vil = (Villager) e.getRightClicked();

        // check whether this villager has cooldown tags
        if (!VillagerUtilities.hasCooldown(vil, plugin)) VillagerUtilities.setNewCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasTime(vil, plugin)) VillagerUtilities.setNewTime(vil, plugin);

        long currentTime = System.currentTimeMillis() / 1000;

        // if time is broken fix it!
        sanityChecks(vil, currentTime);

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);

        long totalSeconds = vilLevelCooldown - currentTime;
        long sec = totalSeconds % 60;


        // Check if the villager is disabled for leveling and send a message
        if (VillagerUtilities.isDisabled(vil, plugin)) {
            if (vilLevelCooldown > currentTime) {
                String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorCodes.cm(message));
                // why not ;)
                vil.shakeHead();
                e.setCancelled(true);
                return;
            }
        }

        if(!VillagerUtilities.hasDisabledByBlock(vil, plugin)){
            VillagerUtilities.setDisabledByBlock(vil, plugin, false);
        }

        // handle Nametag Ai, check if it is already disabled by block
        if (plugin.getConfig().getBoolean("toggleableoptions.userenaming") && !VillagerUtilities.getDisabledByBlock(vil, plugin))
            nameTagAI.call(vil, player, e);

        // handle Block Ai, check if nametag cancelled event (avoid duplicate error?)
        if (plugin.getConfig().getBoolean("toggleableoptions.useblocks") && !e.isCancelled())
            blockAi.call(vil, player);

        // handle Restock, check if Villager is disabled before
        if (VillagerUtilities.isDisabled(vil, plugin))
            restockVillager.call(vil, player);

        if (VillagerUtilities.isDisabled(vil, plugin))
            convertNewVillager.call(vil, player);
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
}


