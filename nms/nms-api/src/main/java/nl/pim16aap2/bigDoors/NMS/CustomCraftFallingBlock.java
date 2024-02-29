package nl.pim16aap2.bigDoors.NMS;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public interface CustomCraftFallingBlock extends Entity
{
    boolean teleport(Location newPos);

    void remove();

    void setVelocity(Vector vector);

    Location getLocation();

    Vector getVelocity();

    void setHeadPose(EulerAngle pose);

    void setBodyPose(EulerAngle eulerAngle);
}

