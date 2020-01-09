package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import tgn.content.terraformer.structures.Region;

/**
 * drops the region down, and collapses any roofs inside as well
 */
public class GravityTransformation implements Transformation {
	@Override
	public void apply(Region region) {
		region.forIn((x, y, z) -> {
			Block block = region.getBlock(x, y, z);
			if(block.getType().isSolid()) {
				// move block to floor
				if(block.getRelative(BlockFace.DOWN).getType().isAir())
					region.world.spawnFallingBlock(block.getLocation().add(.5, 0, .5), block.getBlockData()).addScoreboardTag("replace");

				block.setType(Material.AIR);
			}
		});
	}
}
