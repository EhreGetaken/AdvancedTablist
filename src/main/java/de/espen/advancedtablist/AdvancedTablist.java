package de.espen.advancedtablist;

import de.espen.advancedtablist.api.Metrics;
import de.espen.advancedtablist.api.Settings;
import de.espen.advancedtablist.api.TabListUpdater;
import de.espen.advancedtablist.api.UpdateChecker;
import de.espen.advancedtablist.cmd.Help_CMD;
import de.espen.advancedtablist.cmd.Reload_CMD;
import de.espen.advancedtablist.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AdvancedTablist extends JavaPlugin {

    private static AdvancedTablist instance;
    private Logger logger;
    private Settings settings;
    private TabListUpdater tabListUpdater;

    @Override
    public void onEnable() {
        settings = new Settings();
        this.logger = this.getLogger();
        instance = this;
        this.logger.info("[ATL] Enabling AdvanedTablist version " + this.getDescription().getVersion());

        //UPDATE CHECKER
        new UpdateChecker(this, 85136).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.logger.info("[ATL] You run the latest build of AdvancedTablist.");
            } else {
                this.logger.info("[ATL] There is an update available! You can download it at: ");
            }
        });

        //REGISTER LISTENER
        registerListener();

        //CONFIGURATION
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveConfig();
        this.logger.info("[ATL] Loaded configuration.");

        //SETTINGS
        try {
            settings.PREFIX = getConfig().getString("Settings.Prefix").replaceAll("&", "§");
            settings.VERSION = this.getDescription().getVersion();
            settings.PERMISSION = getConfig().getString("Settings.Permission");

            settings.HEADER = (ArrayList<String>) getConfig().getStringList("TabList.Header");
            settings.FOOTER = (ArrayList<String>) getConfig().getStringList("TabList.Footer");

            settings.UPDATE_FREQUENTLY = getConfig().getBoolean("Settings.UpdateFrequently");
            settings.FIRE_EVENTS = getConfig().getBoolean("Settings.FireUpdateEvents");
            settings.DISABLE_B_STATS = getConfig().getBoolean("Settings.DisableBStats");
        } catch (NullPointerException exc) {
            this.logger.warning("[ATL] Catched exception while settings configuration variables -> "
                    + exc.getMessage() + " \n \n");
            exc.printStackTrace();
        }
        this.logger.info("[ATL] Inserted configuration values.");

        //UPDATE FREQUENTLY TASK
        if (settings.UPDATE_FREQUENTLY) {
            this.tabListUpdater = new TabListUpdater(3);
            tabListUpdater.startUpdater();
            this.logger.info("[ATL] Enabled frequent updating.");
        } else {
            this.tabListUpdater = new TabListUpdater(15);
            this.tabListUpdater.startUpdater();
            this.logger.info("[ATL] Disabled frequent updating.");
        }

        //B STATS METRICS
        if (!this.settings.DISABLE_B_STATS) {
            int pluginId = 9199;
            Metrics metrics = new Metrics(this, pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("Versions", () -> String.valueOf(this.settings.VERSION)));
            this.logger.info("[ATL] Enabled bStats. Thank you for your help.");
        } else {
            this.logger.info("[ATL] Disabled bStats. To help me, please enable it in the config.");
        }

    }

    @Override
    public void onDisable() {
        this.tabListUpdater.stopUpdater();
        this.logger.info("[ATL] Disabling AdvanedTablist version " + this.getDescription().getVersion());
    }

    public static AdvancedTablist getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

    public TabListUpdater getTabListUpdater() {
        return tabListUpdater;
    }

    public void setTabListUpdater(TabListUpdater tabListUpdater) {
        this.tabListUpdater = tabListUpdater;
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), instance);
        getCommand("atl").setExecutor(new Help_CMD());
        getCommand("atl").setExecutor(new Reload_CMD());
    }

}
