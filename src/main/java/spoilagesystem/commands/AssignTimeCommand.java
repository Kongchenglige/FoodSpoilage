package spoilagesystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import spoilagesystem.config.LocalConfigService;
import spoilagesystem.timestamp.LocalTimeStampService;

import java.time.Duration;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public final class AssignTimeCommand implements CommandExecutor {

    private final LocalConfigService configService;
    private final LocalTimeStampService timeStampService;

    public AssignTimeCommand(LocalConfigService configService, LocalTimeStampService timeStampService) {
        this.configService = configService;
        this.timeStampService = timeStampService;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("fs.assign")) {
            sender.sendMessage(RED + "In order to use this command, you need one of the following permission: 'fs.assign'");
            return true;
        }
        if (!(sender instanceof Player player)) {
            return false;
        }
        ItemStack item = player.getInventory().getItemInMainHand();

        if (args.length == 0) {
            player.sendMessage(RED + "You need to specify a time in hours.");
            return true;
        }
        try {
            if(args[0].equals("forever")){
                timeStampService.assignTimeStamp(item, Duration.ofDays(999999999));
                player.sendMessage(GREEN + "Eternal time assigned to item.");
                return true;
            }
            int time = Integer.parseInt(args[0]);
            timeStampService.assignTimeStamp(item, Duration.ofHours(time));
            player.sendMessage(GREEN + "Time assigned to item.");
        } catch (NumberFormatException e) {
            player.sendMessage(RED + "Invalid time.");
        }


        return true;
    }
}
