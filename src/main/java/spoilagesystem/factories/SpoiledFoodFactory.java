package spoilagesystem.factories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import spoilagesystem.config.LocalConfigService;

import java.util.Collections;

/**
 * @author Daniel McCoy Stephenson
 */
public final class SpoiledFoodFactory {

    private final LocalConfigService configService;

    public SpoiledFoodFactory(LocalConfigService configService) {
        this.configService = configService;
    }

    public ItemStack createSpoiledFood(int amount,ItemStack item) {

        ItemStack spoiledFood = new ItemStack(configService.getSpoiledFoodType());

        ItemMeta meta = spoiledFood.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(configService.getSpoiledFoodName());
            meta.setLore(Collections.singletonList(ChatColor.WHITE + configService.getSpoiledFoodLore()));
        }

        spoiledFood.setItemMeta(meta);

        if (configService.canDegreesFood()) {
            Material type = item.getType();

            if (type == Material.GOLDEN_APPLE) {
                spoiledFood = new ItemStack(Material.APPLE);
            }

            if (type == Material.ENCHANTED_GOLDEN_APPLE) {
                spoiledFood = new ItemStack(Material.GOLDEN_APPLE);
            }

            if (type == Material.GOLDEN_CARROT) {
                spoiledFood = new ItemStack(Material.CARROT);
            }
        }

        spoiledFood.setAmount(amount);

        return spoiledFood;
    }
}
