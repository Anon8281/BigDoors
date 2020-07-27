/**
 *
 */
package nl.pim16aap2.bigDoors.util;

import java.util.function.Function;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Pim
 */
public final class ChunkUtils
{
    private ChunkUtils()
    {
        // STAY OUT!
        throw new IllegalAccessError();
    }

    public static Pair<Vector2D, Vector2D> getChunkRangeBetweenCoords(final Location locA, final Location locB)
    {
        return getChunkRangeBetweenSortedCoords(locA, locB, locA, locB);
    }

    public static Pair<Vector2D, Vector2D> getChunkRangeBetweenSortedCoords(final Location minA, final Location minB,
                                                                            final Location maxA, final Location maxB)
    {
        int minX = Math.min(minA.getBlockX(), minB.getBlockX());
        int minZ = Math.min(minA.getBlockZ(), minB.getBlockZ());

        int maxX = Math.max(maxA.getBlockX(), maxB.getBlockX());
        int maxZ = Math.max(maxA.getBlockZ(), maxB.getBlockZ());

        // Convert coords to chunk-space
        minX = minX >> 4;
        minZ = minZ >> 4;
        maxX = maxX >> 4;
        maxZ = maxZ >> 4;

        return new Pair<>(new Vector2D(minX, minZ), new Vector2D(maxX, maxZ));
    }

    public static Pair<Vector2D, Vector2D> getChunkRangeBetweenCoords(final Location locA, final Location locB,
                                                                      final Location locC, final Location locD)
    {
        int minX = Math.min(Math.min(locA.getBlockX(), locB.getBlockX()), Math.min(locC.getBlockX(), locD.getBlockX()));
        int minZ = Math.min(Math.min(locA.getBlockZ(), locB.getBlockZ()), Math.min(locC.getBlockZ(), locD.getBlockZ()));

        int maxX = Math.max(Math.max(locA.getBlockX(), locB.getBlockX()), Math.max(locC.getBlockX(), locD.getBlockX()));
        int maxZ = Math.max(Math.max(locA.getBlockZ(), locB.getBlockZ()), Math.max(locC.getBlockZ(), locD.getBlockZ()));

        // Convert coords to chunk-space
        minX = minX >> 4;
        minZ = minZ >> 4;
        maxX = maxX >> 4;
        maxZ = maxZ >> 4;

        return new Pair<>(new Vector2D(minX, minZ), new Vector2D(maxX, maxZ));
    }

    public static Result checkChunks(final World world, final Pair<Vector2D, Vector2D> chunkRange, final Mode mode)
    {
        Function<Chunk, Result> modeFun;
        switch (mode)
        {
        case VERIFY_LOADED:
            modeFun = ChunkUtils::verifyLoaded;
            break;
        case ATTEMPT_LOAD:
            modeFun = ChunkUtils::attemptLoad;
            break;
        default:
            throw new UnsupportedOperationException();
        }

        boolean requiredLoad = false;
        for (int x = chunkRange.getFirst().getX(); x <= chunkRange.getSecond().getX(); ++x)
            for (int z = chunkRange.getFirst().getY(); z <= chunkRange.getSecond().getY(); ++z)
            {
                final Result result = modeFun.apply(world.getChunkAt(x, z));
                if (result == Result.REQUIRED_LOAD)
                    requiredLoad = true;
                else if (result == Result.FAIL)
                    return Result.FAIL;
            }
        return requiredLoad ? Result.REQUIRED_LOAD : Result.PASS;
    }

    private static Result attemptLoad(final Chunk chunk)
    {
        if (chunk.isLoaded())
            return Result.PASS;
        return chunk.load(false) ? Result.REQUIRED_LOAD : Result.FAIL;
    }

    private static Result verifyLoaded(final Chunk chunk)
    {
        return chunk.isLoaded() ? Result.PASS : Result.FAIL;
    }

    public enum Mode
    {
        /**
         * Verifies that chunks are loaded. If not, it will not attempt to load it and
         * abort the process instead.
         */
        VERIFY_LOADED,

        /**
         * Attempts to load any potentially unloaded chunks.
         */
        ATTEMPT_LOAD,
    }

    public enum Result
    {
        /**
         * All chunks are loaded.
         */
        PASS,

        /**
         * The process failed. For example, it could not load any chunks even though it
         * had to.
         */
        FAIL,

        /**
         * The process successfully loaded 1 or more chunks.
         */
        REQUIRED_LOAD
    }
}
