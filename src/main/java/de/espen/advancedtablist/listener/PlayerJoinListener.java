package de.espen.advancedtablist.listener;

import de.espen.advancedtablist.AdvancedTablist;
import de.espen.advancedtablist.events.TabListUpdatePlayerEvent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(AdvancedTablist.getInstance(), () -> {
            update(player);
        }, 40L);
    }

    private void update(Player player) {
        ArrayList<String> header = AdvancedTablist.getInstance().getSettings().HEADER;
        ArrayList<String> footer = AdvancedTablist.getInstance().getSettings().FOOTER;

        double currentTps = 20;
        double[] tps = MinecraftServer.getServer().recentTps;
        currentTps = tps[0];

        double finalCurrentTps = currentTps;
        int ping = ((CraftPlayer) player).getHandle().ping;
        AtomicReference<String> headerString = new AtomicReference<>("null");
        AtomicReference<String> footerString = new AtomicReference<>("null");
        header.forEach(s -> {
            s = s.replaceAll("%ping%", String.valueOf(ping));
            s = s.replaceAll("%tps%", String.valueOf(Math.round(finalCurrentTps)));
            s = s.replaceAll("%version%",
                    AdvancedTablist.getInstance().getSettings().VERSION);
            s = s.replaceAll("%spacer%", "\n");
            s = s.replaceAll("%currentPlayers%","" + String.valueOf(Bukkit.getOnlinePlayers().size()));
            s = s.replaceAll("%maxPlayers%","" + String.valueOf(Bukkit.getMaxPlayers()));
            s = s.replaceAll("%serverName%", Bukkit.getServerName());
            s = s.replaceAll("&", "§");
            if (headerString.get().equalsIgnoreCase("null")) {
                headerString.set("");
                headerString.set(s);
            } else {
                headerString.set(headerString + "\n" + s);
            }
        });
        footer.forEach(s -> {
            s = s.replaceAll("%ping%", String.valueOf(ping));
            s = s.replaceAll("%tps%", String.valueOf(Math.round(finalCurrentTps)));
            s = s.replaceAll("%version%",
                    AdvancedTablist.getInstance().getSettings().VERSION);
            s = s.replaceAll("%spacer%", "\n");
            s = s.replaceAll("%currentPlayers%","" + String.valueOf(Bukkit.getOnlinePlayers().size()));
            s = s.replaceAll("%maxPlayers%","" + String.valueOf(Bukkit.getMaxPlayers()));
            s = s.replaceAll("%serverName%", Bukkit.getServerName());
            s = s.replaceAll("&", "§");
            if (footerString.get().equalsIgnoreCase("null")) {
                footerString.set("");
                footerString.set(s);
            } else {
                footerString.set(footerString + "\n" + s);
            }
        });
        AdvancedTablist.getInstance().getSettings().sendTab(player, headerString.get(), footerString.get());
        if (AdvancedTablist.getInstance().getSettings().FIRE_EVENTS) {
            Bukkit.getPluginManager().callEvent(new TabListUpdatePlayerEvent(headerString.get(), footerString.get(), player));
        }
    }

}
