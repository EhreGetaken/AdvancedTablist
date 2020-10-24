package de.espen.advancedtablist.api;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Settings {

    public String PREFIX = "";
    public String PERMISSION = "";
    public String VERSION = "";

    public boolean UPDATE_FREQUENTLY = true;
    public boolean FIRE_EVENTS = true;
    public boolean DISABLE_B_STATS = false;

    public ArrayList<String> HEADER = new ArrayList<>();
    public ArrayList<String> FOOTER = new ArrayList<>();

    public void sendTab(Player player, String header, String footer) {
        if (header == null) header = "";
        if (footer == null) footer = "";
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        IChatBaseComponent Title = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent Foot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(Title);
        try {

            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, Foot);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.sendPacket(headerPacket);
        }

    }

}
