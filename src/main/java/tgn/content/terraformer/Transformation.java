package tgn.content.terraformer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import tgn.content.terraformer.heightmap.AverageStrategy;
import tgn.content.terraformer.heightmap.HeightMap;
import tgn.content.terraformer.heightmap.sampling.StandardSampler;
import tgn.content.terraformer.heightmap.sampling.SurfaceSampler;
import tgn.content.terraformer.structures.Region;
import tgn.content.terraformer.util.P3D;
import java.awt.Rectangle;
import java.util.*;

import static java.lang.StrictMath.abs;

public class Transformation {
	private static final float BUILDING_THRESHOLD = 1.0f;
	private static final float ROOF_THRESHOLD = 3f;
	private static final int RESOLUTION = 3;
	private final HeightMap terrain;
	private final HeightMap buildings;
	private final Region region;

	public Transformation(Region region) {
		this.region = region;
		Rectangle area = new Rectangle(region.x, region.z, region.width, region.depth);
		this.terrain = new HeightMap(area, new SurfaceSampler(region.world, 0), AverageStrategy.AVERAGE, RESOLUTION);
		this.buildings = new HeightMap(area, new StandardSampler(region.world), AverageStrategy.MAX, RESOLUTION);
	}

	public void start(CommandSender sender) {
		sender.sendMessage("Starting!");
		List<Region> structures = this.getStructures();
		sender.sendMessage(structures.size() + " structures detected!");
		structures.forEach(r -> {
			sender.sendMessage(ChatColor.GREEN+""+r);
			r.erode();
			r.shatter();
			r.desert();
			r.undergrow();
			r.age();
			r.vine();
			// collapse may move the structure out region so it has to be last
			r.collapse();
		});
		sender.sendMessage("structure transformation complete");
	}

	private List<Region> getStructures() {
		List<Region> regions = new ArrayList<>();
		this.buildings.forEachScaled((p, f) -> {
			Set<P3D> points = new HashSet<>();
			this.search(new P3D(p.x, f.intValue(), p.y), points, OptionalDouble.empty(), 0);
			if(points.size() > 1) {
				IntSummaryStatistics x = points.stream().mapToInt(px -> px.x).summaryStatistics();
				IntSummaryStatistics y = points.stream().mapToInt(px -> px.y).summaryStatistics();
				IntSummaryStatistics z = points.stream().mapToInt(px -> px.z).summaryStatistics();
				Region region = new Region(this.region.world, x.getMin(), y.getMin(), z.getMin(), x.getMax() - x.getMin() + 1, y.getMax() - y.getMin() + 1, z.getMax() - z.getMin() + 1);
				regions.add(region);
			}
		});
		return regions;
	}

	@SuppressWarnings ("OptionalUsedAsFieldOrParameterType")
	private void search(P3D start, Set<P3D> interests, OptionalDouble oldBuilding, int after) {
		if(after > 3)
			return;
		OptionalDouble building = this.buildings.getValidHeight(start.x, start.z);
		double d = building.orElse(Double.NEGATIVE_INFINITY);
		// if building is present and the "roof" isn't too slanted
		if (building.isPresent() && (!oldBuilding.isPresent() || abs(d - oldBuilding.getAsDouble()) < ROOF_THRESHOLD)) {
			OptionalDouble terrain = this.terrain.getValidHeight(start.x, start.z);
			// if terrain is good, which means it's an actual building
			if (terrain.isPresent() && d - terrain.getAsDouble() > BUILDING_THRESHOLD) {
				// building detected
				interests.add(start);
				// search nearby
				this.search(new P3D(start.x, (int) d, start.z + RESOLUTION), interests, building, ++after);
				this.search(new P3D(start.x, (int) d, start.z - RESOLUTION), interests, building, after);
				this.search(new P3D(start.x + RESOLUTION, (int) d, start.z), interests, building, after);
				this.search(new P3D(start.x + RESOLUTION, (int) d, start.z), interests, building, after);
			}
		}
	}
}
