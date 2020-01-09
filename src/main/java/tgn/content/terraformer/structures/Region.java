package tgn.content.terraformer.structures;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import tgn.content.terraformer.util.functions.IntCoordinateConsumer;
import java.util.Random;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;

public class Region {
	public static final Random RANDOM = new Random();
	private Chunk[] cache;
	private final int xSize;
	public final int x, y, z, width, height, depth;
	public final World world;

	public Region(World world, int x, int y, int z, int width, int height, int depth) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.world = world;
		this.xSize = (((x + width) >> 4) - (x >> 4) + 1);
		this.cache = new Chunk[this.xSize * (((z + depth) >> 4) - (z >> 4) + 1)];
	}

	/**
	 * uses the chunk cache to get the blocks quicker
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Block getBlock(int x, int y, int z) {
		this.validate(x, y, z);
		return this.getChunk(x, z).getBlock(x & 15, y, z & 15);
	}

	public void forIn(IntCoordinateConsumer consumer) {
		for (int x = 0; x < this.width; x++)
			for (int z = 0; z < this.depth; z++)
				for (int y = 0; y < this.height; y++)
					consumer.accept(x + this.x, y + this.y, z + this.z);
	}

	public void randIn(IntCoordinateConsumer consumer, int attempts) {
		for (int i = 0; i < attempts; i++) {
			int index = RANDOM.nextInt(this.width * this.height * this.depth);
			int z = index / (this.width * this.height);
			index -= (z * this.width * this.height);
			int y = index / this.width;
			int x = index % this.width;
			consumer.accept(x + this.x, y + this.y, z + this.z);
		}
	}

	public void randIn(IntCoordinateConsumer consumer, float chance) {
		this.randIn(consumer, (int) Math.max(1, this.width * this.height * this.depth * chance));
	}

	public void forBlocks(Consumer<Block> blocks) {
		this.forIn((x, y, z) -> blocks.accept(this.getBlock(x, y, z)));
	}

	public void randBlocks(Consumer<Block> blocks, int count) {
		this.randIn((x, y, z) -> blocks.accept(this.getBlock(x, y, z)), count);
	}

	public void randBlocks(Consumer<Block> blocks, float chance) {
		this.randIn((x, y, z) -> blocks.accept(this.getBlock(x, y, z)), chance);
	}

	private void validate(int x, int y, int z) {
		checkArgument(x >= this.x && x < this.width + this.x, x + ":x is out of range");
		checkArgument(y >= 0 && y < 255, y + ":y is out of range");
		checkArgument(z >= this.z && z < this.depth + this.z, z + ":z is out of range");
	}

	private Chunk getChunk(int x, int z) {
		int chunkX = x >> 4;
		int chunkZ = z >> 4;
		int index = ((chunkX - (this.x >> 4)) * this.xSize) + (chunkZ - (z >> 4));
		Chunk chunk = this.cache[index];
		if (chunk == null) chunk = this.cache[index] = this.world.getChunkAt(chunkX, chunkZ);
		return chunk;
	}
}
