package me.titruc.inventoryALaCarte.menu.clickableEvent;

import java.util.Map;

public class ClickableConditionEntry {

    public final String type;
    public final Map<String, Object> params;
    public final String logic;

    public ClickableConditionEntry(String type, Map<String, Object> params, String logic) {
        this.type = type;
        this.params = params;
        this.logic = logic == null ? "AND" : logic.toUpperCase();
    }
}
