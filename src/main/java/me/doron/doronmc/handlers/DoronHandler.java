package me.doron.doronmc.handlers;

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
    private final double COOLDOWN = 300*20;
    private final String ITEM_NAME = "Cipher Hat";
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
                if (item.getType() == Material.LEATHER_HELMET && item.getItemMeta().getDisplayName().equals(ITEM_NAME)) {
                    double currentTime = player.getWorld().getTime();
                    if ((currentTime - lastUse) > COOLDOWN) {
                        lastUse = currentTime + COOLDOWN;
                        printIps(player);
                    } else {
                        player.sendMessage(ChatColor.GREEN+"HACKING THE MATRIX IS ON COOLDOWN FOR ANOTHER" + ((COOLDOWN - (currentTime - lastUse)) / 20.0) + " SECONDS");
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
