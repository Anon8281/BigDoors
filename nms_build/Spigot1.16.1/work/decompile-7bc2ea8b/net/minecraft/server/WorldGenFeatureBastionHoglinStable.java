package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

public class WorldGenFeatureBastionHoglinStable {

    public static void a() {}

    static {
        ImmutableList<DefinedStructureProcessor> immutablelist = ImmutableList.of(new DefinedStructureProcessorRule(ImmutableList.of(new DefinedStructureProcessorPredicates(new DefinedStructureTestRandomBlock(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.1F), DefinedStructureTestTrue.b, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getBlockData()), new DefinedStructureProcessorPredicates(new DefinedStructureTestRandomBlock(Blocks.BLACKSTONE, 1.0E-4F), DefinedStructureTestTrue.b, Blocks.AIR.getBlockData()), WorldGenFeatureBastionExtra.a)));
        ImmutableList<DefinedStructureProcessor> immutablelist1 = ImmutableList.of(new DefinedStructureProcessorRule(ImmutableList.of(new DefinedStructureProcessorPredicates(new DefinedStructureTestRandomBlock(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), DefinedStructureTestTrue.b, Blocks.AIR.getBlockData()), new DefinedStructureProcessorPredicates(new DefinedStructureTestRandomBlock(Blocks.GOLD_BLOCK, 0.1F), DefinedStructureTestTrue.b, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getBlockData()), WorldGenFeatureBastionExtra.a)));

        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/origin"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/air_base", ImmutableList.of()), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/starting_pieces"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/starting_stairs_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/starting_stairs_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/starting_stairs_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/starting_stairs_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/starting_stairs_4", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/mirrored_starting_pieces"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/stairs_0_mirrored", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/stairs_1_mirrored", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/stairs_2_mirrored", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/stairs_3_mirrored", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/starting_pieces/stairs_4_mirrored", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/wall_bases"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/walls/wall_base", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/walls"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/walls/side_wall_0", immutablelist1), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/walls/side_wall_1", immutablelist1), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/stairs"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_1_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_1_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_1_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_1_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_1_4", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_2_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_2_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_2_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_2_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_2_4", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_3_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_3_1", immutablelist), 1), new Pair[]{Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_3_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_3_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/stairs/stairs_3_4", immutablelist), 1)}), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/small_stables/inner"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/inner_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/inner_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/inner_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/inner_3", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/small_stables/outer"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/outer_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/outer_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/outer_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/small_stables/outer_3", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/large_stables/inner"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/inner_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/inner_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/inner_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/inner_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/inner_4", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/large_stables/outer"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/outer_0", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/outer_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/outer_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/outer_3", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/large_stables/outer_4", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/posts"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/posts/stair_post", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/posts/end_post", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/ramparts"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/ramparts/ramparts_1", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/ramparts/ramparts_2", immutablelist), 1), Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/ramparts/ramparts_3", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/rampart_plates"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/rampart_plates/rampart_plate_1", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
        WorldGenFeatureDefinedStructureJigsawPlacement.a.a(new WorldGenFeatureDefinedStructurePoolTemplate(new MinecraftKey("bastion/hoglin_stable/connectors"), new MinecraftKey("empty"), ImmutableList.of(Pair.of(new WorldGenFeatureDefinedStructurePoolSingle("bastion/hoglin_stable/connectors/end_post_connector", immutablelist), 1)), WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID));
    }
}
