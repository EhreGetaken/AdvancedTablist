package de.espen.advancedtablist.api;

import de.espen.advancedtablist.AdvancedTablist;
import de.espen.advancedtablist.events.TabListUpdateEvent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class TabListUpdater {

    private int schedulerId;
    private int multi;

    public TabListUpdater(int multi) {
        this.multi = multi;
    }

    public void startUpdater() {
        schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AdvancedTablist.getInstance(), () -> {
            ArrayList<String> header = AdvancedTablist.getInstance().getSettings().HEADER;
            ArrayList<String> footer = AdvancedTablist.getInstance().getSettings().FOOTER;

            double currentTps = 20;
            double[] tps = MinecraftServer.getServer().recentTps;
            currentTps = tps[0];

            double finalCurrentTps = currentTps;
            Bukkit.getOnlinePlayers().forEach(player -> {
                int ping = ((CraftPlayer) player).getHandle().ping;
                AtomicReference<String> headerString = new AtomicReference<>("null");
                AtomicReference<String> footerString = new AtomicReference<>("null");
                header.forEach(s -> {
                    s = s.replaceAll("%ping%", String.valueOf(ping))
                            .replaceAll("%tps%", String.valueOf(finalCurrentTps)
                            .replaceAll("%version%",
                                    String.valueOf(AdvancedTablist.getInstance().getSettings().VERSION))
                            .replaceAll("%spacer%", "\n")
                            .replaceAll("%currentPlayers%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replaceAll("%maxPlayers%", String.valueOf(Bukkit.getMaxPlayers()))
                            .replaceAll("%serverName%", Bukkit.getServerName()));
                    if (headerString.get().equalsIgnoreCase("null")) {
                        headerString.set("");
                        headerString.set(s);
                    } else {
                        headerString.set(headerString + "\n" + s);
                    }
                });
                footer.forEach(s -> {
                    s = s.replaceAll("%ping%", String.valueOf(ping))
                            .replaceAll("%tps%", String.valueOf(finalCurrentTps)
                                    .replaceAll("%version%",
                                            String.valueOf(AdvancedTablist.getInstance().getSettings().VERSION))
                                    .replaceAll("%spacer%", "\n")
                                    .replaceAll("%currentPlayers%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                                    .replaceAll("%maxPlayers%", String.valueOf(Bukkit.getMaxPlayers()))
                                    .replaceAll("%serverName%", Bukkit.getServerName()));
                    if (footerString.get().equalsIgnoreCase("null")) {
                        footerString.set("");
                        footerString.set(s);
                    } else {
                        footerString.set(headerString + "\n" + s);
                    }
                });
                AdvancedTablist.getInstance().getSettings().sendTab(player, headerString.get(), footerString.get());
            });
            if (AdvancedTablist.getInstance().getSettings().FIRE_EVENTS) {
                Bukkit.getPluginManager().callEvent(new TabListUpdateEvent(this.schedulerId));
            }
        }, 20L * this.multi, 20L * this.multi);
    }

    public void stopUpdater() {
        Bukkit.getScheduler().cancelTask(this.schedulerId);
    }

    public int getSchedulerId() {
        return schedulerId;
    }

    public int getMulti() {
        return multi;
    }
}
