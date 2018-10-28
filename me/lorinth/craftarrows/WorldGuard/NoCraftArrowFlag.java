package me.lorinth.craftarrows.WorldGuard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.lorinth.craftarrows.Util.NmsHelper;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Location;

public class NoCraftArrowFlag {

    // StateFlag with the name "my-custom-flag", which defaults to "allow"
    public static final StateFlag No_Craft_Arrows = new StateFlag("no-craft-arrows", false);
    public static boolean Enabled = false;

    public static void onLoad() {
        // ... do your own plugin things, get the WorldGuard object, etc
        OutputHandler.PrintInfo("Registering Custom Flag 'no-craft-arrows'");
        int version = NmsHelper.getSimpleVersion();
        OutputHandler.PrintInfo("NMS Version : " + version);
        if(version <= 12){
            try {
                // register our flag with the registry
                FlagRegistry registry = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst().getFlagRegistry();
                registry.register(No_Craft_Arrows);
                Enabled = true;
                OutputHandler.PrintInfo("no-craft-arrows flag added to World Guard!");

            } catch (FlagConflictException e) {
                OutputHandler.PrintException("Error hooking into World Guard", e);
                Enabled = false;
            }
        }
        else{
            try{
                FlagRegistry registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
                registry.register(No_Craft_Arrows);
                Enabled = true;
                OutputHandler.PrintInfo("no-craft-arrows flag added to World Guard!");
            } catch (FlagConflictException e) {
                OutputHandler.PrintException("Error hooking into World Guard", e);
                Enabled = false;
            }
        }
    }

    public static boolean IsProtectedRegion(Location location){
        try{
            if(Enabled){
                int version = NmsHelper.getSimpleVersion();
                if(version <= 12) {

                    RegionManager regionManager = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst().getRegionManager(location.getWorld());
                    if (regionManager == null)
                        return false;

                    ApplicableRegionSet regionSet = regionManager.getApplicableRegions(location);
                    if (regionSet == null)
                        return false;

                    return regionSet.testState(null, No_Craft_Arrows);
                }
                else{
                    /*RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                    return query.testState(new com.sk89q.worldedit.util.Location, (RegionAssociable) null, (StateFlag) No_Craft_Arrows);*/
                }
            }
        }
        catch(Exception exception){
            Enabled = false;
        }
        return false;
    }

}
