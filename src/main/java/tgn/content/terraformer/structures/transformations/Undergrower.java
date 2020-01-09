package tgn.content.terraformer.structures.transformations;

import tgn.content.terraformer.util.Group;

import static org.bukkit.Material.*;

public class Undergrower extends AbstractSwappingTransformation {
	public Undergrower() {
		super(.333f);
		// delete vegitation
		Group.VEGETATION.forEach(m -> this.register(m, AIR));
		// make all wood grey
		Group.LOGS.forEach(m -> this.register(m, ACACIA_LOG));
		// barren grounds
		Group.DIRT.forEach(m -> this.register(m, COARSE_DIRT));
	}
}
