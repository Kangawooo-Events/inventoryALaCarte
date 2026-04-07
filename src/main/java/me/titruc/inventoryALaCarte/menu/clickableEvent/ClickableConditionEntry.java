package me.titruc.inventoryALaCarte.menu.clickableEvent;

import java.util.Map;

public record ClickableConditionEntry(String type, Map<String, Object> params, String logic, boolean not) {

    public ClickableConditionEntry(String type, Map<String, Object> params, String logic, boolean not) {
        this.type = type;
        this.params = params;
        this.logic = logic == null ? "AND" : logic.toUpperCase();
        this.not = not;
    }
}