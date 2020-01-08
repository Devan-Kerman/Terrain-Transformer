package tgn.content.terraformer.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tgn.content.terraformer.heightmap.AverageStrategy;
import tgn.content.terraformer.heightmap.HeightMap;
import tgn.content.terraformer.heightmap.sampling.Sampler;
import tgn.content.terraformer.heightmap.sampling.StandardSampler;
import tgn.content.terraformer.heightmap.sampling.SurfaceSampler;
import tgn.content.terraformer.util.ArrayUtil;
import java.awt.Rectangle;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class TerraformCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 7 || args.length == 8) {
				Optional<Integer[]> ints = ArrayUtil.map(args, Integer::parseInt);
				if (ints.isPresent()) {
					Integer[] loc = ints.get();
					this.test(((Player) sender).getWorld(), loc[0], loc[1], loc[2], loc[3], loc[4], loc[5], loc[6], loc.length == 7 ? 0 : loc[7]);
					return true;
				}
			}
			sender.sendMessage(ChatColor.RED + "Invalid args!");
		}
		return false;
	}

	private static final AverageStrategy[] STRATEGIES = AverageStrategy.values();

	private void test(World world, int sx, int sy, int ex, int ey, int sample, int res, int strat, int con) {
		Sampler sampler;
		if (sample == 0) sampler = new StandardSampler(world);
		else sampler = new SurfaceSampler(world, con);

		HeightMap map = new HeightMap(new Rectangle(min(sx, ex), min(sy, ey), abs(sx - ex), abs(sy - ey)), sampler, STRATEGIES[Math.min(STRATEGIES.length, strat)], res);

		for (int x = sx; x < ex; x++)
			for (int y = sx; y < ey; y++) {
				int finalX = x;
				int finalY = y;
				map.getValidHeight(x, y).ifPresent(d -> world.getBlockAt(finalX, (int) d, finalY).setType(Material.LIME_STAINED_GLASS));
			}
	}
}
