package nl.pim16aap2.bigDoors.NMS.AS_v1_13_R2;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.Vector3f;
import nl.pim16aap2.bigDoors.NMS.CustomCraftFallingBlock_Vall;

public class CraftArmorStand_V1_13_R2 extends CraftLivingEntity implements ArmorStand, CustomCraftFallingBlock_Vall
{
	public CraftArmorStand_V1_13_R2(org.bukkit.craftbukkit.v1_13_R2.CraftServer server, EntityLiving entity)
	{
		super(server, entity);
	}

    public void recalcPosition()
    {
        entity.recalcPosition();
    }

	@Override
	public void setGravity(boolean gravity)
	{
//        super.setGravity(gravity);
//        // Armor stands are special
//        getHandle().noclip = !gravity;
		super.setGravity(gravity);
		getHandle().noclip = gravity;
	}

	@Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public EntityArmorStand getHandle() {
        return (EntityArmorStand) super.getHandle();
    }

	@Override
    public ItemStack getItemInHand() {
        return getEquipment().getItemInHand();
    }

	@Override
    public void setItemInHand(ItemStack item) {
        getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(getHandle().bodyPose);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        getHandle().setBodyPose(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(getHandle().leftArmPose);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        getHandle().setLeftArmPose(toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(getHandle().rightArmPose);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        getHandle().setRightArmPose(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(getHandle().leftLegPose);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        getHandle().setLeftLegPose(toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(getHandle().rightLegPose);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        getHandle().setRightLegPose(toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(getHandle().headPose);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        getHandle().setHeadPose(toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().hasBasePlate();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().setBasePlate(!basePlate);
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().hasArms();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().setArms(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Vector3f old) {
        return new EulerAngle(
            Math.toRadians(old.getX()),
            Math.toRadians(old.getY()),
            Math.toRadians(old.getZ())
        );
    }

    private static Vector3f toNMS(EulerAngle old) {
        return new Vector3f(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        return getHandle().isMarker();
    }

    @Override
    public void setMarker(boolean marker) {
        getHandle().setMarker(marker);
    }
}
