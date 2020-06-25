package nl.pim16aap2.bigDoors.compatiblity;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

import nl.pim16aap2.bigDoors.BigDoors;

/**
 * Compatibility hook for the new version of PlotSquared.
 *
 * @see IProtectionCompat
 * @author Pim
 */
public class TownyNewProtectionCompat implements IProtectionCompat
{
    @SuppressWarnings("unused")
    private final BigDoors plugin;
    private boolean success = false;
    private static final ProtectionCompat compat = ProtectionCompat.TOWNY;
    private final TownyAPI townyAPI;

    public TownyNewProtectionCompat(final BigDoors plugin)
    {
        this.plugin = plugin;
        townyAPI = TownyAPI.getInstance();
        success = townyAPI != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBreakBlock(final Player player, final Location loc)
    {
        return PlayerCacheUtil.getCachePermission(player, loc,
                                                  loc.getBlock().getType(),
                                                  ActionType.DESTROY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBreakBlocksBetweenLocs(final Player player, final Location loc1, final Location loc2)
    {
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int xPos = x1; xPos <= x2; ++xPos)
            for (int yPos = y1; yPos <= y2; ++yPos)
                for (int zPos = z1; zPos <= z2; ++zPos)
                    if (!canBreakBlock(player, new Location(loc1.getWorld(), xPos, yPos, zPos)))
                        return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean success()
    {
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return ProtectionCompat.getName(compat);
    }
}


