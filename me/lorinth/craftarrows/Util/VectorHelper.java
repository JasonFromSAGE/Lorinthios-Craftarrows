package me.lorinth.craftarrows.Util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VectorHelper {

    public static Vector getRawVector(Location from, Location to){
        return to.toVector().subtract(from.toVector());
    }

    public static Vector getVectorBetween(Location from, Location to, double min, double max){
        Vector vector = getNormalizedVector(from, to);
        double distance = from.distanceSquared(to) / 9;

        return vector.multiply(Math.min(Math.max(distance, min*min), max >= 1 ? max*max : max));
    }

    public static Vector getNormalizedVector(Location from, Location to){
        return getRawVector(from, to).normalize();
    }

    public static Vector getDirectionVector(Location from, Location to, double force){
        return getNormalizedVector(from, to).multiply(force);
    }
}
