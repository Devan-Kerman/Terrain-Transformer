package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import tgn.content.terraformer.structures.Region;
import tgn.content.terraformer.util.Group;

import static org.bukkit.Material.MOSSY_STONE_BRICKS;

public class Overgrower implements Transformation {
	private static final BlockFace[] FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST, BlockFace.UP, BlockFace.DOWN};
	@Override
	public void apply(Region region) {
		// TODO: make vegetation and shit
		region.randBlocks(b -> {
			Material material = b.getType();
			if(Group.STONE_BRICKS.is(material))
				b.setType(MOSSY_STONE_BRICKS);
			else if(Group.LEAVES.is(material)) {
				for (BlockFace face : FACES) { // grow leaves
					Block block = b.getRelative(face);
					if(block.isPassable())
						block.setType(b.getType());
				}
			}
		}, .5f);
	}

}
