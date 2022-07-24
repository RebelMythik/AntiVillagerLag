<p align="center">
    <a href="https://discord.gg/WWWeAqwcU6" alt="Discord">
        <img src="https://img.shields.io/discord/990400053673873428?label=Discord"/>
    </a>
</p>

# AntiVillagerLag 

SpigotMC: [Link](https://www.spigotmc.org/resources/antivillagerlag.102949/)

This is a simple plugin that allows your players to optimize their own villagers and does not effect anything vanilla wise. I am making this plugin due to massive trading halls tanking mspt on my servers and others. This will allow trading halls to keep their twice a day villager restocks but won't affect any farms villagers will be used in, such as Iron Golem Farms, Villager Breeders, Raid Farms, Etc.


## How the plugin works
You name a villager with a nametag that matches the name set in config.yml under "NameThatDisables:" and disables the villagers ai. If you wish to re-enable the villager ai all you have to do is rename it to anything else. 
In addition or instead of you can use a block a villager stands on to enable/disable villager ai (after interaction with the villager), you can set "BlockThatDisables:" to the block you want and change "useblocks:" to true. 
Disabled villagers get restocked at two configurable times of day, when the player right clicks the villager. During this right click it checks the custom time stored on the villager of when it was last restocked compared to the current server time. 
The plugin checks if villagers are ready to Levelup and re-enables their ai for 5 seconds to let them levelup their trades.

## Please Note

While the villagers are optimized they will not sleep in beds, or breed while ai is disabled. Players will have to re-enable ai to do so. 
NPC plugins using villagers usually already disable their ai. Make sure not to disable and then re-enable the NPC with AVL, as that will cause the NPC ai to reactivate.

## Features of the Plugin

Nametags will not be used when renaming villagers. This is just to allow easy renaming of all villagers and not have to spend a bunch of resources getting nametags which will discourage players from using this plugin. You can turn it off in the config. 

There is a cooldown between renaming villagers to prevent exploiting vanilla restocking and the
plugin restocking from both happening in a single day. 

Automatically restocks villagers at two set times which are configurable. 

Automatically levels up disabled villagers.

## Bugs and Support

If you encounter any bugs please open a github issue. I will try to solve it as soon as possible!

For support, you can join the [Discord Server](https://discord.gg/WWWeAqwcU6)
