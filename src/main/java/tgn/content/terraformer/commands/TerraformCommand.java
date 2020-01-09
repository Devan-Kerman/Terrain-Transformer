package tgn.content.terraformer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tgn.content.terraformer.Transformation;
import tgn.content.terraformer.structures.Region;

@CommandAlias("terraform")
public class TerraformCommand extends BaseCommand {
	@Default
	public void terraform(CommandSender sender, int x, int y, int z, @Default("64") int width, @Default("64") int height, @Default("64") int depth) {
		Region region = new Region(sender instanceof Player ? ((Player) sender).getWorld() : Bukkit.getWorlds().get(0), x, y, z, width, height, depth);
		Transformation transformation = new Transformation(region);
		transformation.start(sender);
	}
}
