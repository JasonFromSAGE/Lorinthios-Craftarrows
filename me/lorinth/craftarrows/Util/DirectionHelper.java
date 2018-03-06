package me.lorinth.craftarrows.Util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Created by bnra2 on 3/3/2018.
 */
public class DirectionHelper {

    public enum Direction{
        NORTH, EAST, SOUTH, WEST
    }

    /**
     * Get the cardinal compass direction of an entity.
     *
     * @param entity
     * @return
     */
    public static Direction getCardinalDirection(LivingEntity entity) {
        double rot = (entity.getLocation().getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    /**
     * Converts a rotation to a cardinal direction name.
     *
     * @param rot
     * @return
     */
    private static Direction getDirection(double rot) {
        if (0 <= rot && rot < 45) {
            return Direction.WEST;
        } else if (45 <= rot && rot < 135) {
            return Direction.NORTH;
        } else if (135 <= rot && rot < 225) {
            return Direction.EAST;
        } else if (225 <= rot && rot < 315) {
            return Direction.SOUTH;
        } else if (315 <= rot && rot < 360.0) {
            return Direction.WEST;
        } else {
            return null;
        }
    }

}
