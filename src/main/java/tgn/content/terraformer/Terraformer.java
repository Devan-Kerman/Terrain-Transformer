package tgn.content.terraformer;

import org.bukkit.event.Listener;
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
		this.getCommand("terraform").setExecutor(new TerraformCommand());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}