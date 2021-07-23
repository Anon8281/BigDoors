package nl.pim16aap2.bigDoors.NMS;

import net.minecraft.CrashReportSystemDetails;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * v1_17_R1 implementation of {@link CustomEntityFallingBlock}.
 *
 * @author Pim
 * @see CustomEntityFallingBlock
 */
public class CustomEntityFallingBlock_V1_17_R1 extends EntityFallingBlock implements CustomEntityFallingBlock
{
    protected static final DataWatcherObject<BlockPosition> e = DataWatcher.a(EntityFallingBlock.class,
                                                                              DataWatcherRegistry.l);
    /**
     * ticksLived
     */
    public int b;

    /**
     * dropItem
     */
    public boolean c;

    /**
     * hurtEntities
     */
    public boolean ap;

    /**
     * tileEntityData
     */
    public NBTTagCompound d;

    /**
     * block
     */
    private IBlockData f;

    /**
     * fallHurtMax
     */
    private int aq;

    /**
     * FallHurtAmount
     */
    private float ar;

    private final org.bukkit.World bukkitWorld;

    public CustomEntityFallingBlock_V1_17_R1(final org.bukkit.World world, final double d0, final double d1,
        final double d2, final IBlockData iblockdata)
    {
        super(EntityTypes.C, ((CraftWorld) world).getHandle());
        bukkitWorld = world;
        f = iblockdata;
        r = true;
        setPosition(d0, d1 + (1.0F - getHeight()) / 2.0F, d2);
        c = false;
        setNoGravity(true);
        aq = 0;
        ar = 0.0F;
        setMot(0, 0, 0);

        /*
         * lastX, lastY, lastZ
         */
        u = d0;
        v = d1;
        w = d2;

        // try setting noclip twice, because it doesn't seem to stick.
        P = true;
        a(new BlockPosition(locX(), locY(), locZ()));
        spawn();
        P = true;
    }

    public void spawn()
    {
        ((org.bukkit.craftbukkit.v1_17_R1.CraftWorld) bukkitWorld).getHandle().addEntity(this, SpawnReason.CUSTOM);
    }

    @Override
    public void tick()
    {
        if (f.isAir())
            die();
        else
        {
            move(EnumMoveType.a, getMot());
            double locY = locY();
            if (++b > 100 && (locY < 1 || locY > 256) || b > 12000)
                die();

            setMot(getMot().d(0.9800000190734863D, 1.0D, 0.9800000190734863D));
        }
    }

    @Override
    public boolean a(float f, float f1, DamageSource damagesource)
    {
        return false;
    }

    @Override
    protected void saveData(final NBTTagCompound nbttagcompound)
    {
        nbttagcompound.set("BlockState", GameProfileSerializer.a(f));
        nbttagcompound.setInt("Time", b);
        nbttagcompound.setBoolean("DropItem", c);
        nbttagcompound.setBoolean("HurtEntities", ap);
        nbttagcompound.setFloat("FallHurtAmount", ar);
        nbttagcompound.setInt("FallHurtMax", aq);
        if (d != null)
            nbttagcompound.set("TileEntityData", d);
    }

    @Override
    protected void loadData(final NBTTagCompound nbttagcompound)
    {
        f = GameProfileSerializer.c(nbttagcompound.getCompound("BlockState"));
        b = nbttagcompound.getInt("Time");
        if (nbttagcompound.hasKeyOfType("HurtEntities", 99))
        {
            ap = nbttagcompound.getBoolean("HurtEntities");
            ar = nbttagcompound.getFloat("FallHurtAmount");
            aq = nbttagcompound.getInt("FallHurtMax");
        }
        else if (f.a(TagsBlock.G))
            ap = true;

        if (nbttagcompound.hasKeyOfType("DropItem", 99))
            c = nbttagcompound.getBoolean("DropItem");

        if (nbttagcompound.hasKeyOfType("TileEntityData", 10))
            d = nbttagcompound.getCompound("TileEntityData");

        if (f.isAir())
            f = Blocks.C.getBlockData();
    }

    @Override
    public void appendEntityCrashDetails(final CrashReportSystemDetails crashreportsystemdetails)
    {
        super.appendEntityCrashDetails(crashreportsystemdetails);
        crashreportsystemdetails.a("Imitating BlockState", f.toString());
    }

    @Override
    public IBlockData getBlock()
    {
        return f;
    }
}