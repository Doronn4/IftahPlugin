package me.doron.doronmc.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DoronHandler implements Listener {
    private final double COOLDOWN = 120*20;
    private final TextComponent ITEM_NAME = Component.text("Cipher Hat").color(NamedTextColor.GREEN).decorate(TextDecoration.ITALIC);;
    private final String PLAYER_NAME = "ProBooster";
    private double lastUse;

    public DoronHandler() {
        lastUse = 0;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(PLAYER_NAME)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = player.getInventory().getItemInMainHand();
                Bukkit.getLogger().info(""+item.getItemMeta().getDisplayName());
                if (item.getType() == Material.LEATHER_HELMET && item.getItemMeta().displayName().equals(ITEM_NAME)) {

                    double currentTime = player.getWorld().getTime();
                    if ((currentTime - lastUse) > COOLDOWN) {
                        lastUse = currentTime + COOLDOWN;
                        printIps(player);
                    } else {
                        player.sendMessage(ChatColor.GREEN+"HACKING THE MATRIX IS ON COOLDOWN FOR ANOTHER " + ((COOLDOWN - (currentTime - lastUse)) / 20.0) + " SECONDS");
                    }
                }
            }
        }
    }

    private void printIps(Player player) {
        String msg = "HACKING THE MATRIX";
        player.sendMessage(ChatColor.GREEN+msg);
        for (Player p : Bukkit.getOnlinePlayers()) {
            msg = p.getName()+": \nX: "+p.getLocation().getBlockX() + "\nY: "+p.getLocation().getBlockY() + "\nZ: "+p.getLocation().getBlockZ() + "\nIP: "+p.getAddress() + "\n//////////////////////////////////////";
            player.sendMessage(ChatColor.GREEN+msg);
        }
    }
}
