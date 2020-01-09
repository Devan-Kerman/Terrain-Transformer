package tgn.content.terraformer.heightmap;

import com.google.common.base.Preconditions;
import org.bukkit.World;
import tgn.content.terraformer.heightmap.sampling.Region2D;
import tgn.content.terraformer.heightmap.sampling.Sampler;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.OptionalDouble;
import java.util.function.BiConsumer;

import static java.lang.Math.floorDiv;

public class HeightMap {
	// flattened 2d array
	private float[] heights;

	/**
	 * how big each average should be
	 */
	private int resolution;

	/**
	 * the unscaled area encompassed by this heightmap
	 */
	private Rectangle area;

	private final int widthScaled;

	public HeightMap(Rectangle area, Sampler sampler, AverageStrategy strategy, int resolution) {
		Preconditions.checkArgument(resolution > 0, "resolution must be greater than 0");
		this.resolution = resolution;
		this.area = area;
		this.widthScaled = area.width / resolution;
		final int heightScaled = area.height / resolution;
		this.heights = new float[this.widthScaled * heightScaled];

		// initialize the height map
		for (int x = 0; x < area.width; x += resolution) // unscaled
			for (int y = 0; y < area.height; y += resolution) { // unscaled
				// sample all areas in the map
				Region2D region = new Region2D(x + area.x, y + area.y, resolution);
				// find the average
				float value = strategy.average(region, sampler);
				// rescale the coordinates and record it, even if invalid
				this.heights[floorDiv(x, resolution) * this.widthScaled + floorDiv(y, resolution)] = value;
			}
	}

	/**
	 * returns -1 if invalid height
	 * @param x
	 * @param y
	 * @return
	 */
	public float getHeight(int x, int y) {
		// ensure valid location
		if((x >= this.area.x && x < this.area.x + this.area.width) && (y >= this.area.y && y < this.area.y + this.area.height)) {
			int scaledX = floorDiv(x, this.resolution);
			int scaledY = floorDiv(y, this.resolution);
			return this.getHeightAt(scaledX, scaledY);
		}
		return -1;
	}

	public OptionalDouble getValidHeight(int x, int y) {
		float height = this.getHeight(x, y);
		if (height < 0) return OptionalDouble.empty();
		else return OptionalDouble.of(height);
	}

	public Point getScaled(int x, int y) {
		return new Point(floorDiv(x, this.resolution), floorDiv(y, this.resolution));
	}

	public void forEachScaled(BiConsumer<Point, Float> heightConsumer) {
		for (int i = 0; i < this.heights.length; i++)
			heightConsumer.accept(new Point((i / this.widthScaled) * this.resolution, (i % this.widthScaled) * this.resolution), this.heights[i]);
	}

	// round to base
	public Region2D getRegion(World world, int x, int y) {
		return new Region2D(floorDiv(x, this.resolution) * this.resolution, floorDiv(y, this.resolution) * this.resolution, this.resolution);
	}

	private float getHeightAt(int x, int y) {
		return this.heights[x * this.widthScaled + y];
	}
}
