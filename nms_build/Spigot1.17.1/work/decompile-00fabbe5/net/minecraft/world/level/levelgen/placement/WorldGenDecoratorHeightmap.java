package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.levelgen.feature.configurations.HeightmapConfiguration;

public class WorldGenDecoratorHeightmap extends WorldGenDecorator<HeightmapConfiguration> {

    public WorldGenDecoratorHeightmap(Codec<HeightmapConfiguration> codec) {
        super(codec);
    }

    public Stream<BlockPosition> a(WorldGenDecoratorContext worldgendecoratorcontext, Random random, HeightmapConfiguration heightmapconfiguration, BlockPosition blockposition) {
        int i = blockposition.getX();
        int j = blockposition.getZ();
        int k = worldgendecoratorcontext.a(heightmapconfiguration.heightmap, i, j);

        return k > worldgendecoratorcontext.c() ? Stream.of(new BlockPosition(i, k, j)) : Stream.of();
    }
}
