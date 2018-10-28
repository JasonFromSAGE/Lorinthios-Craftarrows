package me.lorinth.craftarrows.GUI;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ArrowRecipeMenu{

    private static ArrayList<IconMenu> recipeMenus;
    private static HashMap<ArrowVariant, Integer> variantPage;
    private static HashMap<Player, Integer> playerIndexes;

    public static void load(){
        recipeMenus = new ArrayList<>();
        variantPage = new HashMap<>();
        playerIndexes = new HashMap<>();

        int index = 0;
        for(ArrowVariant variant : LorinthsCraftArrows.ArrowVariantList){
            IconMenu menu = new IconMenu(variant.getRecipe().getItemName(), 5, ArrowRecipeMenu::click);
            ItemStack[][] materials = variant.getRecipe().getRecipeMaterials();

            for(int y=0; y<3; y++){
                for(int x=0; x<3; x++){
                    menu.addButton(menu.getRow(1 + y), 2 + x, materials[y][x], null);
                }
            }

            menu.addButton(menu.getRow(2), 6, variant.getRecipe().getItem(), null);

            // Add Buttons
            menu.addButton(menu.getRow(2), 0, new ItemStack(Material.PAPER), ChatColor.YELLOW + "Left");
            menu.addButton(menu.getRow(2), 8, new ItemStack(Material.PAPER), ChatColor.YELLOW + "Right");

            recipeMenus.add(menu);
            variantPage.put(variant, index);
            index++;
        }
    }

    private static boolean click(Player clicker, IconMenu menu, IconMenu.Row row, int slot, ItemStack item) {
        if (item != null) {
            if(row.row == 2){
                int newIndex = playerIndexes.get(clicker);
                if(slot == 0){
                    newIndex -= 1;
                    if(newIndex < 0)
                        newIndex = recipeMenus.size()-1;
                    open(clicker, newIndex);
                }
                else if(slot == 8){
                    newIndex += 1;
                    if(newIndex >= recipeMenus.size())
                        newIndex = 0;
                }
                final int index = newIndex;
                Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> open(clicker, index), 1);
            }
        }
        return true;
    }

    public static void open(Player player){
        open(player, playerIndexes.getOrDefault(player, 0));
    }

    public static void open(Player player, String name){
        ArrowVariant variant = LorinthsCraftArrows.getArrowVariantBySimpleName(name);
        int page = 0;
        if(variant != null){
            page = variantPage.getOrDefault(variant, 0);
        }
        open(player, page);
    }

    private static void open(Player player, Integer index){
        playerIndexes.put(player, index);

        recipeMenus.get(index).open(player);
    }

}
