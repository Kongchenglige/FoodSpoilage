package spoilagesystem.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import spoilagesystem.FoodSpoilage;
import spoilagesystem.config.migration.ConfigMigration;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author Daniel McCoy Stephenson
 */
public final class LocalConfigService {

    private final FoodSpoilage plugin;
    private final List<ConfigMigration> migrations;
    private final ConfigCache configCache;

    public LocalConfigService(FoodSpoilage plugin) {
        this.plugin = plugin;
        this.migrations = List.of();
        this.random = new Random();
        configCache = new ConfigCache();
        configCache.load(plugin);
        runMigrations();
        plugin.saveDefaultConfig();
    }

    public void reload(FoodSpoilage plugin) {
        configCache.load(plugin);
    }

    private final Random random;

    /**
     * Method to obtain the Spoilage Time for the given Material.
     * 
     * @param type to obtain the spoilage time for.
     * @return int time.
     * @see org.bukkit.configuration.MemorySection#getInt(String)
     */
    public Duration getTime(Material type) {
        String path = "spoil-time." + type.toString();
        String durationString = (String) configCache.get(path);
        //String durationString = plugin.getConfig().getString("spoil-time." + type.toString(), plugin.getConfig().getString("spoil-time.default"));
        if (durationString == null) return Duration.ZERO;
        Duration time = Duration.parse(durationString); // Get the time from the config.
        plugin.getLogger().fine("Time from configuration for " + type.name() + ":\t" + time);
        return time; // Return the key.
    }

    /**
     * Determines how much of a given material should spoil, given the amount that would be produced should spoiling
     * not have been present.
     *
     * @param type The type of the item
     * @param qty The quantity of the item that would be produced were none of the item to spoil
     * @return amount of the item that has spoiled
     */
    public int determineSpoiledAmount(Material type, int qty) {
        String path = "spoil-chance." + type.toString();
        double chance = (double) configCache.get(path);
        //double chance = plugin.getConfig().getDouble("spoil-chance." + type.toString(), 0);
        if (chance <= 0) return 0;
        int amountSpoiled = 0;
        for (int i = 0; i < qty; i++) {
            if (random.nextDouble() <= chance) {
                amountSpoiled++;
            }
        }
        return amountSpoiled;
    }

    public void runMigrations() {
        migrations.forEach(migration -> {
            if (migration.getPreviousVersion().equals(plugin.getConfig().getString("version"))) {
                migration.run();
                plugin.getConfig().set("version", migration.getNewVersion());
                plugin.saveConfig();
            }
        });
    }

    public List<String> getExpiryDateText() {
        return ((List<String>) configCache.get("text.expiry-date-lore")).stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .toList();
    }

    public String getValuesLoadedText() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.values-loaded")));
    }

    public String getNoPermsReloadText() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.no-permission-reload")));
    }

    public String getSpoiledFoodName() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.spoiled-food-name")));
    }

    public String getSpoiledFoodLore() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.spoiled-food-lore")));
    }

    public String getNeverSpoilText() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.never-spoil")));
    }

    public String getTimeLeftText() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.time-left")));
    }

    public String getLessThanAnHour() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.less-than-an-hour")));
    }

    public String getLessThanADay() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.less-than-a-day")));
    }

    public String getNoTimeLeftText() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull((String) configCache.get("text.no-time-left")));
    }

    public Material getSpoiledFoodType(){
        return Material.getMaterial(Objects.requireNonNull((String) configCache.get("spoiled-food-type")));
    }

    public boolean canDegreesFood(){
        return (Boolean) Objects.requireNonNull(configCache.get("will-degrees-food"));
    }
}