package spoilagesystem.eventhandlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import spoilagesystem.config.LocalConfigService;
import spoilagesystem.timestamp.LocalTimeStampService;

import java.time.Duration;

/**
 * @author Daniel McCoy Stephenson
 */
public final class ItemSpawnEventHandler implements Listener {

    private final LocalConfigService configService;
    private final LocalTimeStampService timeStampService;

    public ItemSpawnEventHandler(LocalConfigService configService, LocalTimeStampService timeStampService) {
        this.configService = configService;
        this.timeStampService = timeStampService;
    }

    @EventHandler()
    public void handle(ItemSpawnEvent event) {

        ItemStack item = event.getEntity().getItemStack();
        Material type = item.getType();
        Duration time = configService.getTime(type);

        // if timestamp not already assigned
        if (!time.equals(Duration.ZERO) && !timeStampService.timeStampAssigned(item)) {
            event.getEntity().setItemStack(timeStampService.assignTimeStamp(item, time));
        }
    }
}