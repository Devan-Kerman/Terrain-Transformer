package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import tgn.content.terraformer.structures.Region;
import tgn.content.terraformer.util.Group;

/**
 * makes everything sandy and shit
 */
public class Desertifier implements Transformation {
	@Override
	public void apply(Region region) {
		region.forBlocks(b -> {
			Material type = b.getType();
			if(region.world.getHighestBlockYAt(b.getX(), b.getZ()) == b.getY()) { // if exposed to sky
				if(type.isSolid())
					b.setType(Material.SAND, false); // make the desert, make them float
				else if(Region.RANDOM.nextDouble() < .05 && Group.GROWABLE_SURFACE.is(type) && b.getRelative(BlockFace.UP).getType().isAir())
					b.setType(Material.DEAD_BUSH, false); // tumble weed time and keep them on the block
			}
			if(type == Material.WATER)
				b.setType(Material.AIR, false); // keep da water back
			else if(type)
		});

		// TODO add more things
	}
}
