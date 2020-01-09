package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import tgn.content.terraformer.structures.Region;

/**
 * makes everything sandy and shit
 */
public class Desertifier implements Transformation {
	@Override
	public void apply(Region region) {
		region.forBlocks(b -> {
			if(region.world.getHighestBlockYAt(b.getX(), b.getZ()) == b.getY()) { // if exposed to sky
				Material type = b.getType();
				if(type.isSolid())
					b.setType(Material.SAND, false); // make the desert
				else if(type == Material.AIR && Region.RANDOM.nextDouble() < .05 && b.getRelative(BlockFace.DOWN).getType().isSolid())
					b.setType(Material.DEAD_BUSH, false); // tumble weed time
			}
		});

		// TODO add more things
	}
}