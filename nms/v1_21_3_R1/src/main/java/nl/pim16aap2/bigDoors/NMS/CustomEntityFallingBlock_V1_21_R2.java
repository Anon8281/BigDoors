package nl.pim16aap2.bigDoors.NMS;

import net.minecraft.CrashReportSystemDetails;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityRemoveEvent;

public class CustomEntityFallingBlock_V1_21_R2 extends EntityFallingBlock implements CustomEntityFallingBlock
{
    private IBlockData block;
    private final CraftWorld world;

    public CustomEntityFallingBlock_V1_21_R2(
        final org.bukkit.World world, final double d0, final double d1, final double d2, final IBlockData iblockdata)
    {
        super(EntityTypes.Y, ((CraftWorld) world).getHandle());
        this.world = (CraftWorld) world;
        block = iblockdata;

        this.a_(d0, d1, d2);
        super.a = 0;
        super.i = false;
        super.ad = true;
        this.f(true);
        this.i(new Vec3D(0.0, 0.0, 0.0));
        this.a(BlockPosition.a(this.dB(), this.dD(), this.dH()));
        spawn();
    }

    public void spawn()
    {
        this.world.addEntityToWorld(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    private void die()
    {
        this.discard(EntityRemoveEvent.Cause.PLUGIN);
    }

    @Override
    public void h()
    {
        if (block.l())
            die();
        else
        {
            this.a(EnumMoveType.a, this.dz());
            if (++a > 12000)
                die();

            i(dz().d(0.9800000190734863D, 1.0D, 0.9800000190734863D));
        }
    }

    @Override
    public boolean a(float f, float f1, DamageSource damagesource)
    {
        return false;
    }

    @Override
    protected void b(final NBTTagCompound nbttagcompound)
    {
        nbttagcompound.a("BlockState", GameProfileSerializer.a(block));
        nbttagcompound.a("Time", a);
        nbttagcompound.a("DropItem", false);
        nbttagcompound.a("HurtEntities", i);
        nbttagcompound.a("FallHurtAmount", 0.0f);
        nbttagcompound.a("FallHurtMax", 0);
        if (c != null)
            nbttagcompound.a("TileEntityData", c);
    }

    @Override
    protected void a(final NBTTagCompound nbttagcompound)
    {
        block = GameProfileSerializer.a(super.cU().a(Registries.f), nbttagcompound.p("BlockState"));
        a = nbttagcompound.h("Time");

        if (nbttagcompound.b("TileEntityData", 10))
            super.c = nbttagcompound.p("TileEntityData");
    }

    @Override
    public void a(final CrashReportSystemDetails crashreportsystemdetails)
    {
        super.a(crashreportsystemdetails);
        crashreportsystemdetails.a("Animated BigDoors block with state: ", block.toString());
    }

    @Override
    public IBlockData p()
    {
        return block;
    }
}
