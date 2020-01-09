package tgn.content.terraformer.structures.transformations;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import tgn.content.terraformer.structures.Region;

public class Vineificator implements Transformation {
	private static final BlockFace[] FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
	@Override
	public void apply(Region region) {
		region.randIn((x, y, z) -> {
			Block block = region.getBlock(x, y, z);
			if(block.getType().isAir()) {
				BlockFace face = FACES[Region.RANDOM.nextInt(FACES.length)];
				if(block.getRelative(face).getType().isSolid()) {
					BlockData data = Bukkit.createBlockData(Material.VINE);
					((MultipleFacing)data).setFace(face, true);
					block.setBlockData(data);
				}
			}
		}, .5f);
	}

}
