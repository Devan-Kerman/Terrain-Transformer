package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import tgn.content.terraformer.structures.Region;
import tgn.content.terraformer.util.Group;

/**
 * shatters all the glass inside an area
 */
public class ShatterWindowTransformation implements Transformation {
	@Override
	public void apply(Region region) {
		region.randBlocks(b -> {
			if(Group.ALL_GLASS.is(b.getType()))
				b.setType(Material.AIR, false);
		},.333f);
	}
}
