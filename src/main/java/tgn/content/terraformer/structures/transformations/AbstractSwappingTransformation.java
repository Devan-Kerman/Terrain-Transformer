package tgn.content.terraformer.structures.transformations;

import org.bukkit.Material;
import tgn.content.terraformer.structures.Region;
import java.util.*;

public abstract class AbstractSwappingTransformation implements Transformation {
	private Map<Material, Material> weights = new HashMap<>();
	private final float chance;

	public AbstractSwappingTransformation(float chance) {
		this.chance = chance;
	}

	@Override
	public void apply(Region region) {
		region.randBlocks(b -> {
			Material material = this.weights.get(b.getType());
			if(material != null)
				b.setType(material, false);
		}, this.chance);
	}

	protected void register(Material from, Material to) {
		this.weights.put(from, to);
	}
}
