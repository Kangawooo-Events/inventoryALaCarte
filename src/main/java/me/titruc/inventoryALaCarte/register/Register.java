package me.titruc.inventoryALaCarte.register;

import java.util.HashMap;

public class Register<T> {

    public final HashMap<String, T> data;

    public Register() {
        data = new HashMap<>();
    }

    public T getFromKey(String key)
    {
        return data.get(key);
    }

    public void register(String key, T object)
    {
        data.put(key, object);
    }

    public boolean has(String key)
    {
        return data.containsKey(key);
    }
}
