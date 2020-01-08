package tgn.content.terraformer.heightmap;

import tgn.content.terraformer.heightmap.sampling.Region;
import tgn.content.terraformer.heightmap.sampling.Sampler;
import java.util.function.BiFunction;

public enum AverageStrategy {
	/**
	 * take the average of all the points inside the sampling region
	 */
	AVERAGE((region, sampler) -> {
		int sum = 0;
		float count = 0;
		final int res = region.getResolution();
		for (int x = 0; x < res; x++)
			for (int y = 0; y < res; y++) {
				int height = sampler.getHeight(x + region.getX(), y + region.getY());
				if (height >= 0) { // ensure valid sample
					sum += height;
					count++;
				}
			}
		if (count == 0) return -1f;
		return sum / count;
	}),
	/**
	 * take the first valid sample
	 */
	FIRST((region, sampler) -> {
		final int res = region.getResolution();
		for (int x = 0; x < res; x++)
			for (int y = 0; y < res; y++) {
				float height = sampler.getHeight(x + region.getX(), y + region.getY());
				if (height >= 0) // if valid sample
					return height;
			}
		return -1f;
	}),
	/**
	 * take the largest sample
	 */
	MAX((region, sampler) -> {
		final int res = region.getResolution();
		float max = -1;
		for (int x = 0; x < res; x++)
			for (int y = 0; y < res; y++) {
				float height = sampler.getHeight(x + region.getX(), y + region.getY());
				if(height > max)
					max = height;
			}
		return max;
	});

	private BiFunction<Region, Sampler, Float> averager;
	AverageStrategy(BiFunction<Region, Sampler, Float> averager) {
		this.averager = averager;
	}

	/**
	 * get the rough average height in the given region
	 * @param region the region to average
	 * @param sampler the sampler to find the heights
	 * @return an average of the heights, or -1 if none was found
	 */
	public float average(Region region, Sampler sampler) {
		return this.averager.apply(region, sampler);
	}
}
