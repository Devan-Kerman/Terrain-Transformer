package tgn.content.terraformer.structures.transformations;

import tgn.content.terraformer.util.Group;

import static org.bukkit.Material.*;

public class ErodeTransformation extends AbstractSwappingTransformation {
	public ErodeTransformation() {
		super(.3333f);
		// delete vegitation
		Group.VEGETATION.forEach(m -> this.register(m, AIR));
		// wear down stones
		Group.STONE.forEach(m -> this.register(m, COBBLESTONE));
		this.register(COBBLESTONE, GRAVEL);
		this.register(GRAVEL, SAND);
		// barren grounds
		Group.DIRT.forEach(m -> this.register(m, COARSE_DIRT));
		// make all wood grey
		Group.LOGS.forEach(m -> this.register(m, ACACIA_LOG));
		// crack da brick
		Group.STONE_BRICKS.forEach(m -> this.register(m, CRACKED_STONE_BRICKS));
		// override
		this.register(STONE_BRICKS, CRACKED_STONE_BRICKS);
		// override
		this.register(CRACKED_STONE_BRICKS, MOSSY_STONE_BRICKS);
	}
}
