package me.lorinth.craftarrows.WorldGuard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.lorinth.craftarrows.Util.NmsHelper;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Location;

public class NoCraftArrowFlag {

    // StateFlag with the name "my-custom-flag", which defaults to "allow"
    private static WorldGuard worldGuard;
    public static final StateFlag No_Craft_Arrows = new StateFlag("no-craft-arrows", false);
    public static boolean Enabled = false;

    public static void onLoad() {
        // ... do your own plugin things, get the WorldGuard object, etc
        OutputHandler.PrintInfo("Registering Custom Flag 'no-craft-arrows'");
        int version = NmsHelper.getSimpleVersion();
        OutputHandler.PrintInfo("NMS Version : " + version);
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

    public static boolean IsProtectedRegion(Location location){
        try{
            if(Enabled){
                WorldGuard WG = ensureWorldGuard();

                RegionContainer container = WG.getPlatform().getRegionContainer();
                if(container != null) {
                    RegionQuery query = container.createQuery();
                    return query.testState(BukkitAdapter.adapt(location), Associables.constant(Association.NON_MEMBER), (StateFlag) No_Craft_Arrows);
                }
            }
        }
        catch(Exception exception){
            Enabled = false;
        }
        return false;
    }

    private static WorldGuard ensureWorldGuard(){
        if(worldGuard == null){
            worldGuard = WorldGuard.getInstance();
        }

        return worldGuard;
    }

}
