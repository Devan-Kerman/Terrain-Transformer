package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import org.bukkit.block.Block;
import tgn.content.terraformer.structures.Region;

/**
 * drops the region down, and collapses any roofs inside as well
 */
public class GravityTransformation implements Transformation {
	@Override
	public void apply(Region region) {
		region.forIn((x, y, z) -> {
			Block block = region.getBlock(x, y, z);
			// move block to floor
			region.getBlock(x, this.getFloor(region, x, y, z), z).setBlockData(block.getBlockData(), false);
			// delete old block
			block.setType(Material.AIR);
		});
	}

	private int getFloor(Region region, int x, int y, int z) {
		for (int cy = y; cy > 0; cy--) {
			if (!region.getBlock(x, cy, z).isPassable()) // check if block is passable
				return cy+1;
		}
		return 0;
	}
}
