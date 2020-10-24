package de.espen.advancedtablist.cmd;

import de.espen.advancedtablist.AdvancedTablist;
import de.espen.advancedtablist.api.TabListUpdater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload_CMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission(AdvancedTablist.getInstance().getSettings().PERMISSION)) {
                        player.sendMessage(AdvancedTablist.getInstance().getSettings().PREFIX +
                                "§cReloading configuration...");
                        AdvancedTablist.getInstance().getTabListUpdater().stopUpdater();
                        AdvancedTablist.getInstance().saveConfig();
                        if (AdvancedTablist.getInstance().getSettings().UPDATE_FREQUENTLY) {
                            TabListUpdater tabListUpdater;
                            tabListUpdater = new TabListUpdater(3);
                            AdvancedTablist.getInstance().setTabListUpdater(tabListUpdater);
                            tabListUpdater.startUpdater();
                            AdvancedTablist.getInstance().getLogger().info("[ATL] Enabled frequent updating.");
                        } else {
                            TabListUpdater tabListUpdater;
                            tabListUpdater = new TabListUpdater(15);
                            AdvancedTablist.getInstance().setTabListUpdater(tabListUpdater);
                            tabListUpdater.startUpdater();
                            AdvancedTablist.getInstance().getLogger().info("[ATL] Disabled frequent updating.");
                        }
                        player.sendMessage(AdvancedTablist.getInstance().getSettings().PREFIX +
                                "§aReload complete.");
                    } else {
                        player.sendMessage(AdvancedTablist.getInstance().getSettings().PREFIX
                                + "§cYou don't have enough permission to execute this command!");
                    }
                }
            }
        }
        return true;
    }
}
