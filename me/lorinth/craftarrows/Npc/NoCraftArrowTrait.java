package me.lorinth.craftarrows.Npc;

import me.lorinth.craftarrows.Listener.CraftArrowListener;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;

@TraitName("nocraftarrows")
public class NoCraftArrowTrait extends Trait {

    public NoCraftArrowTrait() {
        super("nocraftarrows");
    }

    //Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {
        CraftArrowListener.ignoredEntities.add(this.getNPC().getEntity());
    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
        CraftArrowListener.ignoredEntities.remove(this.getNPC().getEntity());
    }

}
