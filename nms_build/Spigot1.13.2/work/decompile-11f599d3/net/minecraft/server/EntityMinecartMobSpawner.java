package net.minecraft.server;

public class EntityMinecartMobSpawner extends EntityMinecartAbstract {

    private final MobSpawnerAbstract a = new MobSpawnerAbstract() {
        public void a(int i) {
            EntityMinecartMobSpawner.this.world.broadcastEntityEffect(EntityMinecartMobSpawner.this, (byte) i);
        }

        public World a() {
            return EntityMinecartMobSpawner.this.world;
        }

        public BlockPosition b() {
            return new BlockPosition(EntityMinecartMobSpawner.this);
        }
    };

    public EntityMinecartMobSpawner(World world) {
        super(EntityTypes.SPAWNER_MINECART, world);
    }

    public EntityMinecartMobSpawner(World world, double d0, double d1, double d2) {
        super(EntityTypes.SPAWNER_MINECART, world, d0, d1, d2);
    }

    public EntityMinecartAbstract.EnumMinecartType v() {
        return EntityMinecartAbstract.EnumMinecartType.SPAWNER;
    }

    public IBlockData z() {
        return Blocks.SPAWNER.getBlockData();
    }

    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a.a(nbttagcompound);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.a.b(nbttagcompound);
    }

    public void tick() {
        super.tick();
        this.a.c();
    }

    public boolean bM() {
        return true;
    }
}
