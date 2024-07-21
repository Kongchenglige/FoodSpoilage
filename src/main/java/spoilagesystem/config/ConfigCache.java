package spoilagesystem.config;

import org.bukkit.configuration.MemorySection;
import spoilagesystem.FoodSpoilage;

import java.util.HashMap;
import java.util.Map;

// Step 1: Define a Cache Class
public class ConfigCache {
    private Map<String, Object> cache = new HashMap<>();

    public void load(FoodSpoilage plugin) {
        // Assuming getConfig() returns a configuration object you can iterate over
        plugin.getConfig().getKeys(true).forEach(key -> {
            Object value = plugin.getConfig().get(key);
            if (value instanceof MemorySection) {
                return;
            }
            cache.put(key, value);
        });
    }

    public Object get(String key) {
        return cache.get(key);
    }

}