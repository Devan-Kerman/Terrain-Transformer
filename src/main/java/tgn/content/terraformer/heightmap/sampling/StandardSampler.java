package tgn.content.terraformer.heightmap.sampling;

import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * just gets the highest non-air block at the location
 */
public class StandardSampler implements Sampler {
	private final World world;

	public StandardSampler(World world) {
		this.world = world;
	}

	@Override
	public int getHeight(int x, int y) {
		Chunk chunk = this.world.getChunkAt(x >> 4, y >> 4); // performance :P
		for (int h = 255; h >= 0; h--) {
			if(!chunk.getBlock(x & 15, h, y & 15).getType().isAir()) // if non air block
				return h;
		}
		return -1;
	}
}
