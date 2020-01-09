package tgn.content.terraformer.structures;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import tgn.content.terraformer.structures.transformations.*;
import tgn.content.terraformer.util.functions.IntCoordinateConsumer;
import java.util.Random;
import java.util.function.Consumer;

public class Region {
	private static final Transformation ERODE_TRANSFORMER = new ErodeTransformation();
	private static final Transformation GRAVITY_TRANSFORMER = new GravityTransformation();
	private static final Transformation WINDOW_TRANSFORMER = new ShatterWindowTransformation();
	private static final Transformation VINE_TRANSFORMER = new Vineificator();
	private static final Transformation AGE_TRANSFORMER = new AgeTransformer();
	private static final Transformation DESERT_TRANSFORMER = new Desertifier();
	private static final Transformation OVERGROWTH_TRANSFORMER = new Overgrower();
	private static final Transformation UNDERGROWTH_TRANSFORMER = new Undergrower();

	public static final Random RANDOM = new Random();
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
	}

	public void age() {
		AGE_TRANSFORMER.apply(this);
	}
	public void desert() {
		DESERT_TRANSFORMER.apply(this);
	}
	public void overgrow() {
		OVERGROWTH_TRANSFORMER.apply(this);
	}
	public void undergrow() {
		UNDERGROWTH_TRANSFORMER.apply(this);
	}
	public void erode() {
		ERODE_TRANSFORMER.apply(this);
	}
	public void collapse() {
		GRAVITY_TRANSFORMER.apply(this);
	}
	public void shatter() {
		WINDOW_TRANSFORMER.apply(this);
	}
	public void vine() {
		VINE_TRANSFORMER.apply(this);
	}

	/**
	 * uses the chunk cache to get the blocks quicker
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Block getBlock(int x, int y, int z) {
		if(this.validate(x, y, z)) {
			return this.getChunk(x, z).getBlock(x & 15, y, z & 15);
		} else
			return this.world.getBlockAt(x, y, z);
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

	private boolean validate(int x, int y, int z) {
		return (x >= this.x && x < this.width + this.x) && (y >= 0 && y < 255) && (z >= this.z && z < this.depth + this.z);
	}

	private Chunk getChunk(int x, int z) {
		// TODO chunk cache
		return this.world.getChunkAt(x >> 4, z >> 4);
	}

	@Override
	public String toString() {
		return "Region{" + "x=" + x + ", y=" + y + ", z=" + z + ", width=" + width + ", height=" + height + ", depth=" + depth + ", world=" + world + '}';
	}
}
