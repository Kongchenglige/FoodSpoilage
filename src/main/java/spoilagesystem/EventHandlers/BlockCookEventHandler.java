package spoilagesystem.EventHandlers;

import org.bukkit.Material;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import spoilagesystem.FoodSpoilage;

public class BlockCookEventHandler {

    public void handle(BlockCookEvent event) {

        ItemStack item = event.getResult();
        Material type = item.getType();
        int time = FoodSpoilage.getInstance().storage.getTime(type);

        if (time != 0) {
            event.setResult(FoodSpoilage.getInstance().timestamp.assignTimeStamp(item, time));
        }

    }

}
