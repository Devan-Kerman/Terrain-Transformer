package tgn.content.terraformer;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tgn.content.terraformer.commands.TerraformCommand;

/**
 * transformation goals:
 *
 * terrain smoothing
 *      - erosion
 * structure detection
 *      - homes
 *      - skyscrapers
 *      - farms
 * structure rotation and transformations
 *      - skyscraper toppling
 *      - roof collapse
 * artificial structure creation
 * natural height map calculation
 * biome rewriting
 *      - biome setting
 *      - block remapping
 *      - terrain changes
 * recoloring
 * vegetation
 * gradual change
 */
public final class Terraformer extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		// Plugin startup logic
		BukkitCommandManager manager = new BukkitCommandManager(this);
		manager.registerCommand(new TerraformCommand());
		manager.enableUnstableAPI("help");
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void falling(ItemSpawnEvent event) {
		Item entity = event.getEntity();
		for (Entity nearbyEntity : entity.getNearbyEntities(2, 2, 2)) {
			if(nearbyEntity instanceof FallingBlock) {
				if(nearbyEntity.getScoreboardTags().contains("replace")) {
					nearbyEntity.remove();
					event.setCancelled(true);
					BlockData data = ((FallingBlock) nearbyEntity).getBlockData();
					nearbyEntity.getWorld().getBlockAt(nearbyEntity.getLocation()).setBlockData(data);
				}
			}
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
