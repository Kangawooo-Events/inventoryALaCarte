package me.titruc.inventoryALaCarte.menu.menuUI.layout;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.view.builder.MerchantInventoryViewBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class MenuMerchant extends MenuHolder {

    private final Merchant merchant;

    public MenuMerchant() {
        super(0, 0, InventoryType.MERCHANT);
        this.merchant = InventoryALaCarte.singleton.getServer().createMerchant();
    }

    public void setTrades(List<MerchantRecipe> recipes) {
        merchant.setRecipes(recipes);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void showInventory(Player player) {
        MerchantInventoryViewBuilder<?> builder = MenuType.MERCHANT.builder();
        builder.merchant(merchant);
        builder.title(getTitle());
        player.openInventory(builder.build(player));
    }

    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException("MenuMerchant does not have a standard Inventory.");
    }
}