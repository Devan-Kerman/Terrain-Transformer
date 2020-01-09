package tgn.content.terraformer.util;

public class P3D {
	public final int x, y, z;

	public P3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "P3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof P3D)) return false;

		P3D d = (P3D) object;

		if (this.x != d.x) return false;
		if (this.y != d.y) return false;
		return this.z == d.z;
	}

	@Override
	public int hashCode() {
		int result = this.x;
		result = 31 * result + this.y;
		result = 31 * result + this.z;
		return result;
	}
}
