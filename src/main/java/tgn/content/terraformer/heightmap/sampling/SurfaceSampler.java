package tgn.content.terraformer.heightmap.sampling;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * attempts to detect the height of the natural terrain
 */
public class SurfaceSampler implements Sampler {
	private final World world;
	private final int confidence;

	/**
	 * initializes a surface sampler with the default confidence level
	 * @param world the world to sample the heights from
	 * @param confidence the amount of "surface blocks" in a row that must be detected for the sampler to be confident it has reached a valid surface
	 */
	public SurfaceSampler(World world, int confidence) {
		this.world = world;
		this.confidence = confidence;
	}

	@Override
	public int getHeight(int x, int y) {
		Chunk chunk = this.world.getChunkAt(x >> 4, y >> 4);
		int surfaces = 0;
		for (int h = 255; h >= 53; h--) {
			Block block = chunk.getBlock(x & 15, h, y & 15);
			// if the block is a "surface block"
			if (this.isSurface(block.getType()))
				if (surfaces == this.confidence) // and we have found enough surface blocks in a row to know for certain we have found a surface
					return h + this.confidence; // return the first valid surface
				else // otherwise increment the number of confirmed surface blocks we have found
					surfaces++;
			else // if this block isn't a surface block, it means we need to keep looking, so we reset the surface counter
				surfaces = 0;
		}
		return -1;
	}

	private boolean isSurface(Material material) {
		switch (material) {
			case GRASS:
			case SNOW:
			case SNOW_BLOCK:
			case SAND:
			case STONE:
			case DIRT:
			case COARSE_DIRT:
			case FARMLAND:
			case WATER:
			case GRASS_BLOCK:
			case GRASS_PATH:
				return true;
			default:
				return false;
		}
	}
}
