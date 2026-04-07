package me.titruc.inventoryALaCarte.menu.menuLoader.loader;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.ConfigHandler;
import me.titruc.inventoryALaCarte.menu.clickableEvent.*;
import me.titruc.inventoryALaCarte.menu.clickableEvent.actionStep.SimpleActionStep;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class MenuLoader {

    public MenuHolder load(FileConfiguration config) {
        MenuHolder menu = createMenu(config);
        if (menu == null) return null;

        String titleStr = config.getString("title.title", ConfigHandler.defaultMenuName);
        menu.setTitle(Component.text(titleStr));

        loadItems(menu, config);
        return menu;
    }

    protected abstract MenuHolder createMenu(FileConfiguration config);

    protected void loadItems(MenuHolder menu, FileConfiguration config) {
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) return;

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection == null) continue;
            parseItem(menu, itemSection, key);
        }
    }

    private void parseItem(MenuHolder menu, ConfigurationSection section, String itemKey) {

        if (!section.contains("slot")) {
            warn("Item \"" + itemKey + "\" : required field : \"slot\" not there, item is ignored.");
            return;
        }
        int slot = section.getInt("slot");

        ItemStack item;

        if (section.contains("item_key")) {
            // --- item depuis le registre ---
            String registryKey = section.getString("item_key");

            if (!InventoryALaCarte.itemRegistry.has(registryKey)) {
                warn("Item \"" + itemKey + "\" : unknown item_key \"" + registryKey + "\", item is ignored.");
                return;
            }

            item = InventoryALaCarte.itemRegistry.get(registryKey);

        } else {
            // --- item depuis un material ---
            String materialName = section.getString("material");
            if (materialName == null) {
                warn("Item \"" + itemKey + "\" : required field : \"material\" not there, item is ignored.");
                return;
            }
            Material material = Material.matchMaterial(materialName);
            if (material == null) {
                warn("Item \"" + itemKey + "\" : Unknown Material \"" + materialName + "\", item is ignored.");
                return;
            }

            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (section.contains("name")) meta.displayName(Component.text(section.getString("name")));
            if (section.contains("lore")) meta.lore(section.getStringList("lore").stream().map(Component::text).toList());
            if (section.contains("model")) {
                String[] split = section.getString("model").split(":");
                meta.setItemModel(new NamespacedKey(split[0], split[1]));
            }
            if (section.contains("customModelData")) meta.setCustomModelData(section.getInt("customModelData"));

            item.setItemMeta(meta);
        }

        if (section.contains("amount")) item.setAmount(section.getInt("amount"));

        menu.getInventory().setItem(slot, item);

        if (!section.contains("click")) return;

        List<ClickStep> steps = parseClickList(section.getMapList("click"), "items." + itemKey);
        if (!steps.isEmpty()) {
            menu.addMenuAction(slot, steps);
        }
    }

    private List<ClickStep> parseClickList(List<Map<?, ?>> rawList, String context) {
        List<ClickStep> steps = new ArrayList<>();

        for (int i = 0; i < rawList.size(); i++) {
            Map<String, Object> entry = toStringMap(rawList.get(i));
            String entryContext = context + "[" + i + "]";

            if (entry.containsKey("statement")) {
                StatementStep statement = parseStatement(entry.get("statement"), entryContext + ".statement");
                if (statement != null) steps.add(statement);
            } else if (entry.containsKey("action")) {
                SimpleActionStep action = parseSimpleAction(entry, entryContext);
                if (action != null) steps.add(action);
            } else {
                warn(entryContext + " : ignore entry, neither \"action\" or \"statement\" found.");
            }
        }

        return steps;
    }

    private SimpleActionStep parseSimpleAction(Map<String, Object> entry, String context) {
        String actionName = (String) entry.get("action");
        if (actionName == null) return null;

        if (!InventoryALaCarte.clickableEventRegister.has(actionName)) {
            warn(context + " : unknown Action \"" + actionName + "\", mayber a typo ? or is it loaded yet ?.");
        }

        Map<String, Object> params = toStringMap(entry.get("parameter"));
        return new SimpleActionStep(actionName, params);
    }

    @SuppressWarnings("unchecked")
    private StatementStep parseStatement(Object raw, String context) {
        if (!(raw instanceof Map<?, ?> rawMap)) {
            warn(context + " : must be a (if/else_if/else), statement ignored.");
            return null;
        }
        Map<String, Object> statementMap = toStringMap(rawMap);


        if (!statementMap.containsKey("if")) {
            warn(context + " : required field :  \"if\" not there, item is ignored.");
            return null;
        }
        Object rawIf = statementMap.get("if");
        if (!(rawIf instanceof Map<?, ?>)) {
            warn(context + ".if : must be a map, statement ignored.");
            return null;
        }
        ConditionalBranch ifBranch = parseConditionalBranch(
                toStringMap((Map<?, ?>) rawIf), context + ".if", true
        );
        if (ifBranch == null) return null;

        List<ConditionalBranch> elseIfBranches = new ArrayList<>();
        Object rawElseIf = statementMap.get("else_if");

        if (rawElseIf instanceof List<?> elseIfList) {
            for (int i = 0; i < elseIfList.size(); i++) {
                Object elseIfEntry = elseIfList.get(i);
                if (!(elseIfEntry instanceof Map<?, ?> elseIfMap)) {
                    warn(context + ".else_if[" + i + "] : must be a map, ignored");
                    continue;
                }
                ConditionalBranch branch = parseConditionalBranch(
                        toStringMap(elseIfMap), context + ".else_if[" + i + "]", true
                );
                if (branch != null) elseIfBranches.add(branch);
            }
        } else if (rawElseIf instanceof Map<?, ?> elseIfMap) {
            ConditionalBranch branch = parseConditionalBranch(
                    toStringMap(elseIfMap), context + ".else_if", true
            );
            if (branch != null) elseIfBranches.add(branch);
        }

        List<ClickStep> elseBranch = new ArrayList<>();
        Object rawElse = statementMap.get("else");

        if (rawElse instanceof Map<?, ?> elseMap) {
            Map<String, Object> elseMapStr = toStringMap(elseMap);
            if (!elseMapStr.containsKey("actions")) {
                warn(context + ".else : required field : \"actions\" not there, item is ignored.");
            } else {
                Object rawActions = elseMapStr.get("actions");
                if (rawActions instanceof List<?> actionList) {
                    elseBranch = parseClickList(
                            (List<Map<?, ?>>) (List<?>) actionList, context + ".else.actions"
                    );
                }
            }
        }

        return new StatementStep(ifBranch, elseIfBranches, elseBranch);
    }


    private ConditionalBranch parseConditionalBranch(
            Map<String, Object> branchMap, String context, boolean conditionsRequired
    ) {

        List<ClickableConditionEntry> conditions = new ArrayList<>();
        if (!branchMap.containsKey("conditions")) {
            if (conditionsRequired) {
                warn(context + " : required field : \"conditions\" not there, item is ignored.");
                return null;
            }
        } else {
            Object rawConditions = branchMap.get("conditions");
            if (rawConditions instanceof List<?> condList) {
                conditions = parseConditions(
                        (List<Map<?, ?>>) (List<?>) condList, context + ".conditions"
                );
            }
        }


        if (!branchMap.containsKey("actions")) {
            warn(context + " : required field : \"actions\" not there, item is ignored.");
            return null;
        }
        List<ClickStep> actions = new ArrayList<>();
        Object rawActions = branchMap.get("actions");
        if (rawActions instanceof List<?> actionList) {
            actions = parseClickList(
                    (List<Map<?, ?>>) (List<?>) actionList, context + ".actions"
            );
        }

        if (actions.isEmpty()) {
            warn(context + " : \"actions\" is empty, branch ignored.");
            return null;
        }

        return new ConditionalBranch(conditions, actions);
    }

    private List<ClickableConditionEntry> parseConditions(List<Map<?, ?>> rawList, String context) {
        List<ClickableConditionEntry> conditions = new ArrayList<>();

        for (int i = 0; i < rawList.size(); i++) {
            Map<String, Object> map = toStringMap(rawList.get(i));
            String entryContext = context + "[" + i + "]";

            // type — obligatoire
            String type = (String) map.get("type");
            if (type == null) {
                warn(entryContext + " : required field : \"type\" not there, item is ignored.");
                continue;
            }


            if (!InventoryALaCarte.clickableConditionRegister.has(type)) {
                warn(entryContext + " : unknown condition \"" + type + "\", mayber a typo ? is it registered yet ?.");
            }

            String logic = map.containsKey("logic") ? map.get("logic").toString() : "AND";
            boolean not = map.containsKey("not") && Boolean.parseBoolean(map.get("not").toString());

            Map<String, Object> params = new HashMap<>(map);
            params.remove("type");
            params.remove("logic");
            params.remove("not");

            conditions.add(new ClickableConditionEntry(type, params, logic, not));
        }

        return conditions;
    }


    private Map<String, Object> toStringMap(Map<?, ?> raw) {
        if (raw == null) return new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<?, ?> e : raw.entrySet()) {
            result.put(e.getKey().toString(), e.getValue());
        }
        return result;
    }

    private Map<String, Object> toStringMap(Object obj) {
        if (obj instanceof Map<?, ?> map) return toStringMap(map);
        return new HashMap<>();
    }

    private void warn(String message) {
        InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] " + message);
    }
}