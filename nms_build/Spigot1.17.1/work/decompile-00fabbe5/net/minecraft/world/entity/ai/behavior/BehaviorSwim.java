package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.entity.EntityInsentient;

public class BehaviorSwim extends Behavior<EntityInsentient> {

    private final float chance;

    public BehaviorSwim(float f) {
        super(ImmutableMap.of());
        this.chance = f;
    }

    protected boolean a(WorldServer worldserver, EntityInsentient entityinsentient) {
        return entityinsentient.isInWater() && entityinsentient.b((Tag) TagsFluid.WATER) > entityinsentient.cN() || entityinsentient.aX();
    }

    protected boolean b(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        return this.a(worldserver, entityinsentient);
    }

    protected void d(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        if (entityinsentient.getRandom().nextFloat() < this.chance) {
            entityinsentient.getControllerJump().jump();
        }

    }
}
