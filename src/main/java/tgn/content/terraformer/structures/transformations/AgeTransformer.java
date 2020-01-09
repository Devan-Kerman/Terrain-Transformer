package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import tgn.content.terraformer.structures.Region;

/**
 * poke holes in everything
 */
public class AgeTransformer implements Transformation {
	@Override
	public void apply(Region region) {
		region.randBlocks(b -> {
			if (!b.isLiquid()) b.setType(b.getType().isSolid() ? Material.AIR : Material.COBWEB);
		}, .05f);
	}
}
