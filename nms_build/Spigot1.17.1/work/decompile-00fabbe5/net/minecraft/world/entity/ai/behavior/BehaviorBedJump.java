package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryTarget;

public class BehaviorBedJump extends Behavior<EntityInsentient> {

    private static final int MAX_TIME_TO_REACH_BED = 100;
    private static final int MIN_JUMPS = 3;
    private static final int MAX_JUMPS = 6;
    private static final int COOLDOWN_BETWEEN_JUMPS = 5;
    private final float speedModifier;
    @Nullable
    private BlockPosition targetBed;
    private int remainingTimeToReachBed;
    private int remainingJumps;
    private int remainingCooldownUntilNextJump;

    public BehaviorBedJump(float f) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = f;
    }

    protected boolean a(WorldServer worldserver, EntityInsentient entityinsentient) {
        return entityinsentient.isBaby() && this.b(worldserver, entityinsentient);
    }

    protected void a(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        super.a(worldserver, entityinsentient, i);
        this.a(entityinsentient).ifPresent((blockposition) -> {
            this.targetBed = blockposition;
            this.remainingTimeToReachBed = 100;
            this.remainingJumps = 3 + worldserver.random.nextInt(4);
            this.remainingCooldownUntilNextJump = 0;
            this.a(entityinsentient, blockposition);
        });
    }

    protected void c(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        super.c(worldserver, entityinsentient, i);
        this.targetBed = null;
        this.remainingTimeToReachBed = 0;
        this.remainingJumps = 0;
        this.remainingCooldownUntilNextJump = 0;
    }

    protected boolean b(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        return entityinsentient.isBaby() && this.targetBed != null && this.a(worldserver, this.targetBed) && !this.e(worldserver, entityinsentient) && !this.f(worldserver, entityinsentient);
    }

    @Override
    protected boolean a(long i) {
        return false;
    }

    protected void d(WorldServer worldserver, EntityInsentient entityinsentient, long i) {
        if (!this.c(worldserver, entityinsentient)) {
            --this.remainingTimeToReachBed;
        } else if (this.remainingCooldownUntilNextJump > 0) {
            --this.remainingCooldownUntilNextJump;
        } else {
            if (this.d(worldserver, entityinsentient)) {
                entityinsentient.getControllerJump().jump();
                --this.remainingJumps;
                this.remainingCooldownUntilNextJump = 5;
            }

        }
    }

    private void a(EntityInsentient entityinsentient, BlockPosition blockposition) {
        entityinsentient.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, (Object) (new MemoryTarget(blockposition, this.speedModifier, 0)));
    }

    private boolean b(WorldServer worldserver, EntityInsentient entityinsentient) {
        return this.c(worldserver, entityinsentient) || this.a(entityinsentient).isPresent();
    }

    private boolean c(WorldServer worldserver, EntityInsentient entityinsentient) {
        BlockPosition blockposition = entityinsentient.getChunkCoordinates();
        BlockPosition blockposition1 = blockposition.down();

        return this.a(worldserver, blockposition) || this.a(worldserver, blockposition1);
    }

    private boolean d(WorldServer worldserver, EntityInsentient entityinsentient) {
        return this.a(worldserver, entityinsentient.getChunkCoordinates());
    }

    private boolean a(WorldServer worldserver, BlockPosition blockposition) {
        return worldserver.getType(blockposition).a((Tag) TagsBlock.BEDS);
    }

    private Optional<BlockPosition> a(EntityInsentient entityinsentient) {
        return entityinsentient.getBehaviorController().getMemory(MemoryModuleType.NEAREST_BED);
    }

    private boolean e(WorldServer worldserver, EntityInsentient entityinsentient) {
        return !this.c(worldserver, entityinsentient) && this.remainingTimeToReachBed <= 0;
    }

    private boolean f(WorldServer worldserver, EntityInsentient entityinsentient) {
        return this.c(worldserver, entityinsentient) && this.remainingJumps <= 0;
    }
}
