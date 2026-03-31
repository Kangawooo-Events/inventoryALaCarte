package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class StatementStep implements ClickStep {

    private final ConditionalBranch ifBranch;
    private final List<ConditionalBranch> elseIfBranches;
    private final List<ClickStep> elseBranch; // else has no conditions

    public StatementStep(
            ConditionalBranch ifBranch,
            List<ConditionalBranch> elseIfBranches,
            List<ClickStep> elseBranch
    ) {
        this.ifBranch = ifBranch;
        this.elseIfBranches = elseIfBranches;
        this.elseBranch = elseBranch;
    }

    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item) {
        // Try if
        if (ifBranch.test(player, menu, item)) {
            ifBranch.execute(player, menu, item);
            return;
        }

        // Try each else_if in order
        for (ConditionalBranch branch : elseIfBranches) {
            if (branch.test(player, menu, item)) {
                branch.execute(player, menu, item);
                return;
            }
        }

        // Fallback to else
        for (ClickStep action : elseBranch) {
            action.execute(player, menu, item);
        }
    }
}