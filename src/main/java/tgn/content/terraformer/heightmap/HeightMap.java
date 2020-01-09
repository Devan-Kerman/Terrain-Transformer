package tgn.content.terraformer.heightmap;

import com.google.common.base.Preconditions;
import tgn.content.terraformer.heightmap.sampling.Region2D;
import tgn.content.terraformer.heightmap.sampling.Sampler;
import java.awt.Rectangle;
import java.util.OptionalDouble;

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

	public HeightMap(Rectangle area, Sampler sampler, AverageStrategy strategy, int resolution) {
		Preconditions.checkArgument(resolution > 0, "resolution must be greater than 0");
		this.resolution = resolution;
		this.area = area;
		this.heights = new float[(area.width / resolution) * (area.height / resolution)];

		// initialize the height map
		for (int x = 0; x < area.width; x += resolution) // unscaled
			for (int y = 0; y < area.height; y += resolution) { // unscaled
				// sample all areas in the map
				Region2D region = new Region2D(x + area.x, y + area.y, resolution);
				// find the average
				float value = strategy.average(region, sampler);
				// rescale the coordinates and record it, even if invalid
				this.heights[floorDiv(x, resolution) * (area.width / resolution) + floorDiv(y, resolution)] = value;
			}
	}

	public float getHeight(int x, int y) {
		// ensure valid location
		Preconditions.checkArgument((x >= this.area.x && x < this.area.x + this.area.width) && (y >= this.area.y && y < this.area.y + this.area.height));
		int scaledX = floorDiv(x, this.resolution);
		int scaledY = floorDiv(y, this.resolution);
		return this.getHeightAt(scaledX, scaledY);
	}

	public OptionalDouble getValidHeight(int x, int y) {
		float height = this.getHeight(x, y);
		if(height < 0)
			return OptionalDouble.empty();
		else
			return OptionalDouble.of(height);
	}

	private float getHeightAt(int x, int y) {
		return this.heights[x * (this.area.width / this.resolution) + y];
	}
}
