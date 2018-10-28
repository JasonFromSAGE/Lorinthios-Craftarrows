package me.lorinth.craftarrows.Objects;

import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.OutputHandler;
import me.lorinth.craftarrows.Util.TryParse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArrowRecipe {

    private HashMap<Character, Material> recipeMaterials = new HashMap<>();
    private HashMap<Character, Byte> recipeData = new HashMap<>();
    private boolean isDisabled = false;
    private String itemName = "";
    private String description = "";
    private ItemStack item = new ItemStack(Material.ARROW);
    private Integer craftCount;
    private String recipe1 = "";
    private String recipe2 = "";
    private String recipe3 = "";
    private String nameGarbage = "" + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.ITALIC;

    public ArrowRecipe(FileConfiguration config, String prefix, String name){
        load(config, prefix, name);
    }

    public String getItemName(){
        return itemName;
    }
    public ItemStack getItem(){ return item; }
    public boolean isDisabled(){ return isDisabled; }

    private void load(FileConfiguration config, String prefix, String name){
        prefix = prefix + "." + name;
        loadConfigValues(config, prefix, name);
        loadRecipeData(config, prefix, name);
        makeItem();
        if(!isDisabled)
            makeRecipe(name);
    }

    private void loadConfigValues(FileConfiguration config, String prefix, String name){
        isDisabled = !config.getBoolean(prefix + ".Enabled");
        recipe1 = config.getString(prefix + ".Shape-1");
        recipe2 = config.getString(prefix + ".Shape-2");
        recipe3 = config.getString(prefix + ".Shape-3");

        itemName = config.getString(prefix + ".Name").replace("&", "§") + nameGarbage;
        description = config.getString(prefix + ".Desc").replace("&", "§");
        craftCount = config.getInt(prefix + ".Amount");
    }

    private void loadRecipeData(FileConfiguration config, String prefix, String name){
        List<String> shapeChars = config.getStringList(prefix + ".ShapeChars");
        List<String> charMats = config.getStringList(prefix + ".CharMats");

        if(shapeChars.size() == charMats.size()){
            for(int i=0; i<shapeChars.size(); i++){
                String line = charMats.get(i);
                if(line.contains(",")){
                    String[] split = charMats.get(0).split(",");
                    if(TryParse.parseByte(split[1]))
                        recipeData.put(shapeChars.get(i).charAt(0), Byte.parseByte(split[1]));
                    if(TryParse.parseMaterial(split[0])) {
                        recipeMaterials.put(shapeChars.get(i).charAt(0), Material.valueOf(split[0]));
                    }
                    else{
                        OutputHandler.PrintError("Invalid material, " + charMats.get(i) + ", in recipe, " + name);
                        OutputHandler.PrintError("Recipe disabled");
                        return;
                    }
                }
                else{
                    if(TryParse.parseMaterial(charMats.get(i))) {
                        recipeMaterials.put(shapeChars.get(i).charAt(0), Material.valueOf(charMats.get(i)));
                    }
                    else{
                        OutputHandler.PrintError("Invalid material, " + charMats.get(i) + ", in recipe, " + name);
                        OutputHandler.PrintError("Recipe disabled");
                        return;
                    }
                }
            }
        }
        else{
            OutputHandler.PrintError("Recipe for, " + name + ", does not work");
            OutputHandler.PrintError("Mistmatched items between, ShapeChars and CharMats");
            OutputHandler.PrintError("Recipe disabled");
            return;
        }
    }

    private void makeItem(){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setLore(new ArrayList<String>(){{ add(description); }});
        item.setItemMeta(meta);
        item.setAmount(craftCount);
    }

    private void makeRecipe(String name){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(LorinthsCraftArrows.instance, name), item);
        for(Character cha : recipeMaterials.keySet()){
            if(recipeMaterials.get(cha) == Material.AIR){
                recipe1.replace(cha, ' ');
                recipe2.replace(cha, ' ');
                recipe3.replace(cha, ' ');
            }
        }

        recipe.shape(new String[]{ recipe1, recipe2, recipe3 });
        for(Character cha : recipeMaterials.keySet()){
            if(recipeData.containsKey(cha))
                recipe.setIngredient(cha, new MaterialData(recipeMaterials.get(cha), recipeData.get(cha)));
            else
                recipe.setIngredient(cha, recipeMaterials.get(cha));
        }

        LorinthsCraftArrows.AddRecipe(recipe);
    }

    public ItemStack[][] getRecipeMaterials(){
        ItemStack[][] recipeMaterialArray = new ItemStack[3][3];
        recipeMaterialArray[0] = convertLineToItemStacks(recipe1);
        recipeMaterialArray[1] = convertLineToItemStacks(recipe2);
        recipeMaterialArray[2] = convertLineToItemStacks(recipe3);
        return recipeMaterialArray;
    }

    private ItemStack[] convertLineToItemStacks(String line){
        ItemStack[] newLine = new ItemStack[3];
        for(int i=0; i<3; i++){
            char c = line.charAt(i);
            if(recipeData.containsKey(c))
                newLine[i] = new ItemStack(recipeMaterials.get(c), 1, (short) 0, recipeData.get(c));
            else
                newLine[i] = new ItemStack(recipeMaterials.get(c));
        }
        return newLine;
    }

}
