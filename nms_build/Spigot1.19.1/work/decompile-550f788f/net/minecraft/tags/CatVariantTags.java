package net.minecraft.tags;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.animal.CatVariant;

public class CatVariantTags {

    public static final TagKey<CatVariant> DEFAULT_SPAWNS = create("default_spawns");
    public static final TagKey<CatVariant> FULL_MOON_SPAWNS = create("full_moon_spawns");

    private CatVariantTags() {}

    private static TagKey<CatVariant> create(String s) {
        return TagKey.create(IRegistry.CAT_VARIANT_REGISTRY, new MinecraftKey(s));
    }
}
