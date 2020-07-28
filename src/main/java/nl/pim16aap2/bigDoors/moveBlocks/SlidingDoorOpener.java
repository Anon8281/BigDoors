package nl.pim16aap2.bigDoors.moveBlocks;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;

import nl.pim16aap2.bigDoors.BigDoors;
import nl.pim16aap2.bigDoors.Door;
import nl.pim16aap2.bigDoors.util.ChunkUtils;
import nl.pim16aap2.bigDoors.util.ChunkUtils.ChunkLoadMode;
import nl.pim16aap2.bigDoors.util.ChunkUtils.ChunkLoadResult;
import nl.pim16aap2.bigDoors.util.DoorOpenResult;
import nl.pim16aap2.bigDoors.util.Pair;
import nl.pim16aap2.bigDoors.util.RotateDirection;
import nl.pim16aap2.bigDoors.util.Util;
import nl.pim16aap2.bigDoors.util.Vector2D;

public class SlidingDoorOpener implements Opener
{
    private BigDoors plugin;

    public SlidingDoorOpener(BigDoors plugin)
    {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Vector2D, Vector2D> getChunkRange(Door door)
    {
        RotateDirection openDirection = door.getOpenDir();
        if (openDirection == null || openDirection.equals(RotateDirection.NONE) || !isRotateDirectionValid(door))
            return null;

        boolean NS = openDirection.equals(RotateDirection.NORTH) || openDirection.equals(RotateDirection.SOUTH);
        int blocksToMove = door.getBlocksToMove() > 0 ? door.getBlocksToMove() : getLengthInDir(door, NS);

        if (door.isOpen())
        {
            switch (openDirection)
            {
            case NORTH:
                openDirection = RotateDirection.SOUTH;
                break;
            case EAST:
                openDirection = RotateDirection.WEST;
                break;
            case SOUTH:
                openDirection = RotateDirection.NORTH;
                break;
            case WEST:
                openDirection = RotateDirection.EAST;
                break;
            default:
                break;
            }
        }

        return getChunkRange(door, new MovementSpecification(blocksToMove, openDirection, NS));
    }

    private Pair<Vector2D, Vector2D> getChunkRange(Door door, MovementSpecification movement)
    {
        int blocksToMove = movement.getBlocks();
        if (movement.getRotateDirection().equals(RotateDirection.NORTH) ||
            movement.getRotateDirection().equals(RotateDirection.WEST))
            blocksToMove *= -1;

        int moveX = 0, moveZ = 0;
        if (movement.NS)
            moveZ = blocksToMove;
        else
            moveX = blocksToMove;

        Location newMin = door.getMinimum().clone().add(moveX, 0, moveZ);
        Location newMax = door.getMaximum().clone().add(moveX, 0, moveZ);

        return ChunkUtils.getChunkRangeBetweenCoords(newMin, newMax, door.getMinimum(), door.getMaximum());
    }

    @Override
    public boolean isRotateDirectionValid(Door door)
    {
        return door.getOpenDir().equals(RotateDirection.NORTH) || door.getOpenDir().equals(RotateDirection.EAST) ||
               door.getOpenDir().equals(RotateDirection.SOUTH) || door.getOpenDir().equals(RotateDirection.WEST);
    }

    @Override
    public RotateDirection getRotateDirection(Door door)
    {
        if (isRotateDirectionValid(door))
            return door.getOpenDir();

        // First try to use the existing methods to figure out where to go. If that
        // doens't work go NORTH if
        RotateDirection openDir = getBlocksToMove(door).getRotateDirection();
        if (openDir.equals(RotateDirection.NONE))
            return door.getMinimum().getBlockX() == door.getMaximum().getBlockX() ? RotateDirection.NORTH :
                RotateDirection.EAST;
        return openDir;
    }

    private int getLengthInDir(Door door, boolean NS)
    {
        // If NS, check the number of blocks along the north/south axis, i.e. the Z
        // axis.
        if (NS)
            return 1 + door.getMaximum().getBlockZ() - door.getMinimum().getBlockZ();
        return 1 + door.getMaximum().getBlockX() - door.getMinimum().getBlockX();
    }

    @Override
    public DoorOpenResult shadowToggle(Door door)
    {
        if (plugin.getCommander().isDoorBusyRegisterIfNot(door.getDoorUID()))
        {
            plugin.getMyLogger().myLogger(Level.INFO,
                                          "Sliding door " + door.getName() + " is not available right now!");
            return abort(DoorOpenResult.BUSY, door.getDoorUID());
        }

        RotateDirection openDirection = door.getOpenDir();
        if (door.getOpenDir().equals(RotateDirection.NONE) || openDirection == null)
        {
            plugin.getMyLogger().myLogger(Level.INFO, "Door " + door.getName() + " has no open direction!");
            return abort(DoorOpenResult.NODIRECTION, door.getDoorUID());
        }

        boolean NS = openDirection.equals(RotateDirection.NORTH) || openDirection.equals(RotateDirection.SOUTH);
        int moved = door.getBlocksToMove() > 0 ? door.getBlocksToMove() : getLengthInDir(door, NS);

        int multiplier = 1;
        // These directions go to a negative value.
        if (openDirection.equals(RotateDirection.NORTH) || openDirection.equals(RotateDirection.WEST))
            multiplier *= -1;
        multiplier *= door.isOpen() ? 1 : -1;

        SlidingMover.updateCoords(door, null, null, moved * multiplier, NS, true);

        return abort(DoorOpenResult.SUCCESS, door.getDoorUID());
    }

    @Override
    public DoorOpenResult openDoor(Door door, double time)
    {
        return openDoor(door, time, false, false);
    }

    private void getNewCoords(final Location min, final Location max, final MovementSpecification blocksToMove)
    {
        int addX = 0, addY = 0, addZ = 0;

        switch (blocksToMove.getRotateDirection())
        {
        case DOWN:
            addY = -1 * blocksToMove.getBlocks();
            break;
        case EAST:
            addX = 1 * blocksToMove.getBlocks();
            break;
        case NORTH:
            addZ = -1 * blocksToMove.getBlocks();
            break;
        case SOUTH:
            addZ = 1 * blocksToMove.getBlocks();
            break;
        case UP:
            addY = 1 * blocksToMove.getBlocks();
            break;
        case WEST:
            addX = -1 * blocksToMove.getBlocks();
            break;
        default:
            break;

        }

        min.add(addX, addY, addZ);
        max.add(addX, addY, addZ);
    }

    // Open a door.
    @Override
    public DoorOpenResult openDoor(Door door, double time, boolean instantOpen, boolean silent, ChunkLoadMode mode)
    {
        if (plugin.getCommander().isDoorBusyRegisterIfNot(door.getDoorUID()))
        {
            if (!silent)
                plugin.getMyLogger().myLogger(Level.INFO, "Door " + door.getName() + " is not available right now!");
            return abort(DoorOpenResult.BUSY, door.getDoorUID());
        }

        final ChunkLoadResult chunkLoadResult = chunksLoaded(door, mode);
        if (chunkLoadResult == ChunkLoadResult.FAIL)
        {
            plugin.getMyLogger().logMessage("Chunks for door " + door.getName() + " are not loaded!", true, false);
            return abort(DoorOpenResult.CHUNKSNOTLOADED, door.getDoorUID());
        }
        if (chunkLoadResult == ChunkLoadResult.REQUIRED_LOAD)
            instantOpen = true;

        // Make sure the doorSize does not exceed the total doorSize.
        // If it does, open the door instantly.
        int maxDoorSize = getSizeLimit(door);
        if (maxDoorSize > 0 && door.getBlockCount() > maxDoorSize)
        {
            plugin.getMyLogger().logMessage("Door \"" + door.getDoorUID() + "\" Exceeds the size limit: " + maxDoorSize,
                                            true, false);
            return abort(DoorOpenResult.ERROR, door.getDoorUID());
        }

        MovementSpecification blocksToMove = getBlocksToMove(door);
        if (Math.abs(blocksToMove.getBlocks()) > BigDoors.get().getConfigLoader().getMaxBlocksToMove())
        {
            plugin.getMyLogger().logMessage("Door \"" + door.getDoorUID() + "\" Exceeds blocksToMove limit: "
                + blocksToMove.getBlocks() + ". Limit = " + BigDoors.get().getConfigLoader().getMaxBlocksToMove(), true,
                                            false);
            return abort(DoorOpenResult.BLOCKSTOMOVEINVALID, door.getDoorUID());
        }

        if (blocksToMove.getBlocks() != 0)
        {
            if (!isRotateDirectionValid(door))
            {
                // If the door is currently open, then the selected rotDirection is actually the
                // closing direction.
                // So, if the door is open, flip the direciton.
                RotateDirection newRotDir = blocksToMove.getRotateDirection();
                if (door.isOpen())
                {
                    newRotDir = newRotDir.equals(RotateDirection.NORTH) ? RotateDirection.SOUTH :
                        newRotDir.equals(RotateDirection.SOUTH) ? RotateDirection.NORTH :
                        newRotDir.equals(RotateDirection.EAST) ? RotateDirection.WEST : RotateDirection.EAST;
                }

                plugin.getMyLogger().logMessage("Updating openDirection of sliding door " + door.getName() + " to "
                    + newRotDir.name() + ". If this is undesired, change it via the GUI.", true, false);
                plugin.getCommander().updateDoorOpenDirection(door.getDoorUID(), newRotDir);
            }

            Location newMin = door.getMinimum();
            Location newMax = door.getMaximum();
            getNewCoords(newMin, newMax, blocksToMove);

            // The door's owner does not have permission to move the door into the new
            // position (e.g. worldguard doens't allow it.
            if (plugin.canBreakBlocksBetweenLocs(door.getPlayerUUID(), door.getPlayerName(), door.getWorld(), newMin,
                                                 newMax) != null ||
                plugin.canBreakBlocksBetweenLocs(door.getPlayerUUID(), door.getPlayerName(), door.getWorld(),
                                                 door.getMinimum(), door.getMaximum()) != null)

                return abort(DoorOpenResult.NOPERMISSION, door.getDoorUID());

            if (fireDoorEventTogglePrepare(door, instantOpen))
                return abort(DoorOpenResult.CANCELLED, door.getDoorUID());

            plugin.getCommander()
                .addBlockMover(new SlidingMover(plugin, door.getWorld(), time, door, instantOpen,
                                                blocksToMove.getBlocks(), blocksToMove.getRotateDirection(),
                                                plugin.getConfigLoader().sdMultiplier()));
            fireDoorEventToggleStart(door, instantOpen);
            return DoorOpenResult.SUCCESS;
        }
        return abort(DoorOpenResult.NODIRECTION, door.getDoorUID());
    }

    private int getBlocksInDir(Door door, RotateDirection slideDir)
    {
        int xMin, xMax, zMin, zMax, yMin, yMax, xLen, zLen, moveBlocks = 0, step;
        xMin = door.getMinimum().getBlockX();
        yMin = door.getMinimum().getBlockY();
        zMin = door.getMinimum().getBlockZ();
        xMax = door.getMaximum().getBlockX();
        yMax = door.getMaximum().getBlockY();
        zMax = door.getMaximum().getBlockZ();
        xLen = Math.abs(xMax - xMin) + 1;
        zLen = Math.abs(zMax - zMin) + 1;

        xLen = door.getOpenDir() == RotateDirection.NONE || door.getBlocksToMove() < 1 ? xLen : door.getBlocksToMove();
        zLen = door.getOpenDir() == RotateDirection.NONE || door.getBlocksToMove() < 1 ? zLen : door.getBlocksToMove();

        int xAxis, yAxis, zAxis;
        step = slideDir == RotateDirection.NORTH || slideDir == RotateDirection.WEST ? -1 : 1;

        int startX, startY, startZ, endX, endY, endZ;
        startY = yMin;
        endY = yMax;
        if (slideDir == RotateDirection.NORTH)
        {
            startZ = zMin - 1;
            endZ = zMin - zLen - 1;
            startX = xMin;
            endX = xMax;
        }
        else if (slideDir == RotateDirection.SOUTH)
        {
            startZ = zMax + 1;
            endZ = zMax + zLen + 1;
            startX = xMin;
            endX = xMax;
        }
        else if (slideDir == RotateDirection.WEST)
        {
            startZ = zMin;
            endZ = zMax;
            startX = xMin - 1;
            endX = xMin - xLen - 1;
        }
        else if (slideDir == RotateDirection.EAST)
        {
            startZ = zMin;
            endZ = zMax;
            startX = xMax + 1;
            endX = xMax + xLen + 1;
        }
        else
            return 0;

        World world = door.getWorld();
        if (slideDir == RotateDirection.NORTH || slideDir == RotateDirection.SOUTH)
            for (zAxis = startZ; zAxis != endZ; zAxis += step)
            {
                for (xAxis = startX; xAxis != endX + 1; ++xAxis)
                    for (yAxis = startY; yAxis != endY + 1; ++yAxis)
                        if (!Util.isAirOrWater(world.getBlockAt(xAxis, yAxis, zAxis).getType()))
                            return moveBlocks;
                moveBlocks += step;
            }
        else
            for (xAxis = startX; xAxis != endX; xAxis += step)
            {
                for (zAxis = startZ; zAxis != endZ + 1; ++zAxis)
                    for (yAxis = startY; yAxis != endY + 1; ++yAxis)
                        if (!Util.isAirOrWater(world.getBlockAt(xAxis, yAxis, zAxis).getType()))
                            return moveBlocks;
                moveBlocks += step;
            }
        return moveBlocks;
    }

    private MovementSpecification getBlocksToMove(Door door)
    {
        int blocksNorth = 0, blocksEast = 0, blocksSouth = 0, blocksWest = 0;

        if (door.getOpenDir().equals(RotateDirection.NONE))
        {
            blocksNorth = getBlocksInDir(door, RotateDirection.NORTH);
            blocksSouth = getBlocksInDir(door, RotateDirection.SOUTH);
            blocksEast = getBlocksInDir(door, RotateDirection.EAST);
            blocksWest = getBlocksInDir(door, RotateDirection.WEST);
        }
        else if (door.getOpenDir().equals(RotateDirection.NORTH) && !door.isOpen() ||
                 door.getOpenDir().equals(RotateDirection.SOUTH) && door.isOpen())
            blocksNorth = getBlocksInDir(door, RotateDirection.NORTH);
        else if (door.getOpenDir().equals(RotateDirection.NORTH) && door.isOpen() ||
                 door.getOpenDir().equals(RotateDirection.SOUTH) && !door.isOpen())
            blocksSouth = getBlocksInDir(door, RotateDirection.SOUTH);
        else if (door.getOpenDir().equals(RotateDirection.EAST) && !door.isOpen() ||
                 door.getOpenDir().equals(RotateDirection.WEST) && door.isOpen())
            blocksEast = getBlocksInDir(door, RotateDirection.EAST);
        else if (door.getOpenDir().equals(RotateDirection.EAST) && door.isOpen() ||
                 door.getOpenDir().equals(RotateDirection.WEST) && !door.isOpen())
            blocksWest = getBlocksInDir(door, RotateDirection.WEST);
        else
            return new MovementSpecification(0, RotateDirection.NONE);

        int maxVal = Math.max(Math.abs(blocksNorth), Math.max(blocksEast, Math.max(blocksSouth, Math.abs(blocksWest))));

        if (Math.abs(blocksNorth) == maxVal)
            return new MovementSpecification(blocksNorth, RotateDirection.NORTH);
        if (blocksEast == maxVal)
            return new MovementSpecification(blocksEast, RotateDirection.EAST);
        if (blocksSouth == maxVal)
            return new MovementSpecification(blocksSouth, RotateDirection.SOUTH);
        if (Math.abs(blocksWest) == maxVal)
            return new MovementSpecification(blocksWest, RotateDirection.WEST);
        return new MovementSpecification(0, RotateDirection.NONE);
    }

    private static final class MovementSpecification
    {
        private final int blocks;
        private final RotateDirection rotateDirection;
        private final boolean NS;

        MovementSpecification(int blocks, RotateDirection rotateDirection, boolean NS)
        {
            this.blocks = Math.abs(blocks);
            this.rotateDirection = rotateDirection;
            this.NS = NS;
        }

        MovementSpecification(int blocks, RotateDirection rotateDirection)
        {
            this(blocks, rotateDirection,
                 rotateDirection == RotateDirection.NORTH || rotateDirection == RotateDirection.SOUTH);
        }

        public int getBlocks()
        {
            return blocks;
        }

        public RotateDirection getRotateDirection()
        {
            return rotateDirection;
        }
    }
}
