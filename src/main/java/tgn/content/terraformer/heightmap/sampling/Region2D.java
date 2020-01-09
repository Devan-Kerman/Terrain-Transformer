package tgn.content.terraformer.heightmap.sampling;

public class Region2D {
	private int x, y, resolution;

	public Region2D(int x, int y, int resolution) {
		this.x = x;
		this.y = y;
		this.resolution = resolution;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getResolution() {
		return this.resolution;
	}
}
