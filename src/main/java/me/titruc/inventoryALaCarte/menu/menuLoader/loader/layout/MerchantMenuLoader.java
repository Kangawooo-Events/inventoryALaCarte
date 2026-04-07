package me.titruc.inventoryALaCarte.menu.menuLoader.loader.layout;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.menuLoader.loader.MenuLoader;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import me.titruc.inventoryALaCarte.menu.menuUI.layout.MenuMerchant;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MerchantMenuLoader extends MenuLoader {

    private static final Logger log = InventoryALaCarte.singleton.getLogger();

    @Override
    protected MenuHolder createMenu(FileConfiguration config) {
        MenuMerchant menu = new MenuMerchant();

        if (config.contains("items")) {
            log.warning("[InventoryALaCarte] [MerchantMenu] 'items' field will be ignored with merchant type.");
        }

        if (!config.contains("trades")) {
            log.severe("[InventoryALaCarte] [MerchantMenu] 'trades' field is required for merchant type.");
            return null;
        }

        List<Map<?, ?>> rawTrades = config.getMapList("trades");

        if (rawTrades.isEmpty()) {
            log.severe("[InventoryALaCarte] [MerchantMenu] 'trades' field is required for merchant type (list is empty).");
            return null;
        }

        List<MerchantRecipe> recipes = new ArrayList<>();

        for (int i = 0; i < rawTrades.size(); i++) {
            Map<?, ?> rawTrade = rawTrades.get(i);
            String tradeId = "trade[" + i + "]";

            if (!rawTrade.containsKey("result") && !rawTrade.containsKey("result_key")) {
                log.severe("[InventoryALaCarte] [MerchantMenu] " + tradeId + " is missing required field 'result' or 'result_key'. Trade skipped.");
                continue;
            }
            if (!rawTrade.containsKey("ingredient1")) {
                log.severe("[InventoryALaCarte] [MerchantMenu] " + tradeId + " is missing required field 'ingredient1'. Trade skipped.");
                continue;
            }

            int resultAmount    = rawTrade.containsKey("result_amount")    ? (int)     rawTrade.get("result_amount")                      : 1;
            int maxUses         = rawTrade.containsKey("max_uses")         ? (int)     rawTrade.get("max_uses")                           : 2147483647;
            boolean expReward   = rawTrade.containsKey("exp_reward")       ? (boolean) rawTrade.get("exp_reward")                         : false;
            int villagerExp     = rawTrade.containsKey("villager_exp")     ? (int)     rawTrade.get("villager_exp")                       : 0;
            float priceMultiply = rawTrade.containsKey("price_multiplier") ? ((Number) rawTrade.get("price_multiplier")).floatValue()      : 0.0f;
            int demand          = rawTrade.containsKey("demand")           ? (int)     rawTrade.get("demand")                             : 0;
            int specialPrice    = rawTrade.containsKey("special_price")    ? (int)     rawTrade.get("special_price")                      : 0;
            boolean ignoreDisc  = rawTrade.containsKey("ignore_discounts") ? (boolean) rawTrade.get("ignore_discounts")                   : true;


            ItemStack resultItem;
            if (rawTrade.containsKey("result_key")) {
                String key = (String) rawTrade.get("result_key");
                if (!InventoryALaCarte.itemRegistry.has(key)) {
                    log.severe("[InventoryALaCarte] [MerchantMenu] " + tradeId + " has unknown result_key '" + key + "'. Trade skipped.");
                    continue;
                }
                resultItem = InventoryALaCarte.itemRegistry.get(key);
                resultItem.setAmount(resultAmount);
            } else {
                Material resultMaterial;
                try {
                    resultMaterial = Material.valueOf((String) rawTrade.get("result"));
                } catch (IllegalArgumentException e) {
                    log.severe("[InventoryALaCarte] [MerchantMenu] " + tradeId + " has invalid material for 'result': '" + rawTrade.get("result") + "'. Trade skipped.");
                    continue;
                }
                resultItem = new ItemStack(resultMaterial, resultAmount);
            }

            MerchantRecipe recipe = new MerchantRecipe(
                    resultItem,
                    0,
                    maxUses,
                    expReward,
                    villagerExp,
                    priceMultiply,
                    demand,
                    specialPrice,
                    ignoreDisc
            );


            Material ing1Material;
            try {
                ing1Material = Material.valueOf((String) rawTrade.get("ingredient1"));
            } catch (IllegalArgumentException e) {
                log.severe("[InventoryALaCarte] [MerchantMenu] " + tradeId + " has invalid material for 'ingredient1': '" + rawTrade.get("ingredient1") + "'. Trade skipped.");
                continue;
            }
            int ing1Amount = rawTrade.containsKey("ingredient1_amount") ? (int) rawTrade.get("ingredient1_amount") : 1;
            recipe.addIngredient(new ItemStack(ing1Material, ing1Amount));


            if (rawTrade.containsKey("ingredient2")) {
                Material ing2Material;
                try {
                    ing2Material = Material.valueOf((String) rawTrade.get("ingredient2"));
                } catch (IllegalArgumentException e) {
                    log.warning("[InventoryALaCarte] [MerchantMenu] " + tradeId + " has invalid material for 'ingredient2': '" + rawTrade.get("ingredient2") + "'. Ingredient ignored.");
                    recipes.add(recipe);
                    continue;
                }
                int ing2Amount = rawTrade.containsKey("ingredient2_amount") ? (int) rawTrade.get("ingredient2_amount") : 1;
                recipe.addIngredient(new ItemStack(ing2Material, ing2Amount));
            }

            recipes.add(recipe);
        }

        if (recipes.isEmpty()) {
            log.severe("[InventoryALaCarte] [MerchantMenu] No valid trades could be loaded. Menu will be empty.");
        }

        menu.setTrades(recipes);
        return menu;
    }

    @Override
    protected void loadItems(MenuHolder menu, FileConfiguration config) {
        //empty do not touch
    }
}