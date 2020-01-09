package tgn.content.terraformer.util;

import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.bukkit.Material.*;

public class Group {
	private static final Material[] MATERIALS = Material.values();
	// only stained glass
	public static final Group STAINED_GLASS = new Group();
	// only stained glass panes
	public static final Group STAINED_GLASS_PANES = new Group();
	// normal glass and all stained variants
	public static final Group GLASS = new Group();
	// normal glass panes and stained variants
	public static final Group GLASS_PANES = new Group();
	// glass panes and glass, and their stained variants
	public static final Group ALL_GLASS = new Group(GLASS, GLASS_PANES);
	// all the "stones", stone, diorite, andesite, granite
	public static final Group STONE = new Group();
	// "dirts"
	public static final Group DIRT = new Group();
	// living shit that can be sheared
	public static final Group VEGETATION = new Group();
	// s n o w including layers
	public static final Group SNOW = new Group();
	// air, cave air, void air
	public static final Group AIR = new Group(Material::isAir);
	// all the logs and log, OAK_LOG and OAK_LOGS, etc.
	public static final Group LOGS = new Group();
	// stone bricks and stuff
	public static final Group STONE_BRICKS = new Group();
	// leaves
	public static final Group LEAVES = new Group();
	public static final Group GROWABLE_SURFACE = new Group();

	private final Predicate<Material> predicate;
	private final Set<Material> set;
	public Group() {
		Set<Material> materials = new HashSet<>();
		this.predicate = materials::contains;
		this.set = materials;
	}

	public Group(Predicate<Material> predicate) {
		this.predicate = predicate;
		this.set = null;
	}

	public Group(Group...groups) {
		Predicate<Material> predicate = groups[0].predicate;
		for (int i = 1; i < groups.length; i++)
			predicate = predicate.or(groups[i].predicate);
		this.predicate = predicate;
		this.set = null;
	}

	public boolean is(Material material) {
		return this.predicate.test(material);
	}

	public void forEach(Consumer<Material> consumer) {
		assert this.set != null : "set is null";
		this.set.forEach(consumer);
	}

	static {
		register(Material.GLASS, GLASS, STAINED_GLASS);
		register("STAINED_GLASS", String::endsWith, GLASS, STAINED_GLASS);
		register(GLASS_PANE, GLASS_PANES);
		register("STAINED_GLASS_PANES", String::endsWith, STAINED_GLASS_PANES, GLASS_PANES);
		register(STONE, Material.STONE, DIORITE, GRANITE, ANDESITE);
		register(DIRT, Material.DIRT, COARSE_DIRT, GRASS_PATH, GRASS_BLOCK, FARMLAND);
		register(VEGETATION, GRASS, TALL_GRASS, CORNFLOWER, SUNFLOWER, POPPY, DANDELION, ALLIUM, ROSE_BUSH, AZURE_BLUET, FERN, LARGE_FERN, VINE, CACTUS, BLUE_ORCHID, RED_TULIP, ORANGE_TULIP, PINK_TULIP, WHITE_TULIP, OXEYE_DAISY, LILY_OF_THE_VALLEY, LILAC, PEONY);
		register(SNOW, Material.SNOW, SNOW_BLOCK);
		register("LEAVES", String::endsWith, VEGETATION, LEAVES);
		register("^(?!POTTED).*_SAPLING", String::matches, VEGETATION);
		register("LOG", String::endsWith, LOGS);
		register("WOOD", String::endsWith, LOGS);
		register(STONE_BRICKS, CHISELED_STONE_BRICKS, CRACKED_STONE_BRICKS, MOSSY_STONE_BRICKS, Material.STONE_BRICKS, INFESTED_CHISELED_STONE_BRICKS, INFESTED_STONE_BRICKS, INFESTED_CRACKED_STONE_BRICKS, INFESTED_MOSSY_STONE_BRICKS);
		register(GROWABLE_SURFACE, DIRT);
		register(GROWABLE_SURFACE, SAND);
	}

	private static void register(Material material, Group...groups) {
		for (Group group : groups) {
			assert group.set != null : "group cannot be added to";
			group.set.add(material);
		}
	}

	public static void register(Group group, Material...materials) {
		for (Material material : materials)
			register(material, group);
	}

	public static void register(Group group, Group...groups) {
		for (Group group1 : groups) {
			group1.forEach(m -> register(group, m));
		}
	}

	private static void register(String string, BiPredicate<String, String> check, Group...groups) {
		for (Material material : MATERIALS)
			if(check.test(material.name(), string))
				register(material, groups);
	}
}
