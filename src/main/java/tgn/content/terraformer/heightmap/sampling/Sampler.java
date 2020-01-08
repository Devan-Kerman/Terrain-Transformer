package tgn.content.terraformer.heightmap.sampling;

/**
 * finds a height from an x and y coordinate
 */
public interface Sampler {
	/**
	 * get the height at the point
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the height, or -1 if none was found
	 */
	int getHeight(int x, int y);
}
