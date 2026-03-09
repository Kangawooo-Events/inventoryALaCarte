package me.titruc.inventoryALaCarte.menu.menuLoader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MenuFactory {

    private static final Gson GSON = new Gson();

    public static <T> T read(Plugin plugin, String fileName, Type type) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName + ".json");

        try
        {
            BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(bufferedReader);
            return GSON.fromJson(jsonReader, type);
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
