package tgn.content.terraformer.structures.transformations;

import tgn.content.terraformer.util.Group;

import static org.bukkit.Material.*;

public class ErodeTransformation extends AbstractSwappingTransformation {
	public ErodeTransformation() {
		super(.3333f);
		// wear down stones
		Group.STONE.forEach(m -> this.register(m, COBBLESTONE));
		this.register(COBBLESTONE, GRAVEL);
		this.register(GRAVEL, SAND);
		// crack da brick
		Group.STONE_BRICKS.forEach(m -> this.register(m, CRACKED_STONE_BRICKS));
		// override
		this.register(STONE_BRICKS, CRACKED_STONE_BRICKS);
		// TODO more things
	}
}
