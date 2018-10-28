package me.lorinth.craftarrows.WorldGuard;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.Comparator;

public class RegionComparator implements Comparator<ProtectedRegion>{

    @Override
    public int compare(ProtectedRegion a, ProtectedRegion b) {
        return a.getPriority() < b.getPriority() ? -1
                : a.getPriority() > b.getPriority() ? 1
                : 0;
    }

}
