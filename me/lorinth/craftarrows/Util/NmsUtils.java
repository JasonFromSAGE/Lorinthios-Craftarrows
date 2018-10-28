package me.lorinth.craftarrows.Util;

import org.bukkit.inventory.ItemStack;

public class NmsUtils {

    public static ItemStack RemoveDamageAttribute(ItemStack i){
        Class<?> itemStackClass = NmsHelper.getNMSClass("ItemStack");
        Class<?> craftItemStack = NmsHelper.getOBClass("inventory.CraftItemStack");
        Class<?> nbtTagCompound = NmsHelper.getNMSClass("NBTTagCompound");
        Class<?> nbtTagList = NmsHelper.getNMSClass("NBTTagList");
        Class<?> nbtTagString = NmsHelper.getNMSClass("NBTTagString");
        Class<?> nbtTagInt = NmsHelper.getNMSClass("NBTTagInt");
        Class<?> nbtBase = NmsHelper.getNMSClass("NBTBase");
        try{
            Object nmsStack = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, i);
            Object compound = (boolean) itemStackClass.getMethod("hasTag").invoke(nmsStack) ? itemStackClass.getMethod("getTag").invoke(nmsStack) : nbtTagCompound.newInstance();
            Object modifiers = nbtTagList.newInstance();

            Object damage = nbtTagCompound.newInstance();
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "AttributeName", nbtTagString.getConstructor(String.class).newInstance("generic.attackDamage"));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "Name", nbtTagString.getConstructor(String.class).newInstance("generic.attackDamage"));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "Amount", nbtTagInt.getConstructor(int.class).newInstance(0));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "Operation", nbtTagInt.getConstructor(int.class).newInstance(0));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "UUIDLeast", nbtTagInt.getConstructor(int.class).newInstance(894654));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "UUIDMost", nbtTagInt.getConstructor(int.class).newInstance(2872));
            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(damage, "Slot", nbtTagString.getConstructor(String.class).newInstance("mainhand"));
            nbtTagList.getMethod("add", nbtBase).invoke(modifiers, damage);

            nbtTagCompound.getMethod("set", String.class, nbtBase).invoke(compound, "AttributeModifiers", modifiers);
            itemStackClass.getMethod("setTag", nbtTagCompound).invoke(nmsStack, compound);
            i = (ItemStack) craftItemStack.getDeclaredMethod("asBukkitCopy", itemStackClass).invoke(null, nmsStack);
        }
        catch(Exception e){
            System.out.println(e);
        }

        return i;
    }

}
