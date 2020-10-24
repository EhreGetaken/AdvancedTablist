package de.espen.advancedtablist.cmd;

import de.espen.advancedtablist.AdvancedTablist;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help_CMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(AdvancedTablist.getInstance()
                            .getSettings().PREFIX + "§7AdvancedTablist by Espen \n" +
                            "§8> §7/atl reload | Reload the configuration \n" +
                            "§8> §7/atl help | Get this page");
                }
            } else if (args.length == 0) {
                player.sendMessage(AdvancedTablist.getInstance()
                        .getSettings().PREFIX + "§7AdvancedTablist by Espen \n" +
                        "§8> §7/atl reload | Reload the configuration \n" +
                        "§8> §7/atl help | Get this page");
            }
        }
        return true;
    }
}
