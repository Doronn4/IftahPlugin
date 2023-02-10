package me.doron.doronmc.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ShakedHandler implements Listener {
    private Location shak_jail;
    private World world;
    private final Material[][][] shak_jail_layout  = new Material[][][]{
            {{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK}},
            {{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK}},
            {{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK}},
            {{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.AIR,Material.AIR,Material.AIR,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK}},
            {{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK},{Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK,Material.BEDROCK}}
    };

    private ItemStack hiking_boots = new ItemStack(Material.LEATHER_BOOTS,1);

    public ShakedHandler() {
        WorldCreator worldCreator = new WorldCreator("flat");
        world = worldCreator.createWorld();
        Block temp;

        ItemMeta metaBoots = hiking_boots.getItemMeta();
        metaBoots.setDisplayName("Hiking Boots");
        hiking_boots.setItemMeta(metaBoots);

        shak_jail = new Location(world,-6900,10,-6900);
        for(int i = -2;i<=2;i++) {
            for(int j = -2;j<=2;j++) {
                for(int k = -2;k<=2;k++) {
                    temp = world.getBlockAt(shak_jail.getBlockX()+i, shak_jail.getBlockY()+k, shak_jail.getBlockZ()+j);
                    temp.setType(shak_jail_layout[k+2][j+2][i+2],true);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(getPlayerName(player).equals("DevBot__")&&player.getEquipment().getBoots().equals(hiking_boots)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1, true, false, true));
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(getPlayerName(player).equals("DevBot__")&&player.getEquipment().getBoots().equals(hiking_boots)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1, true, false, true));
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item != null && getPlayerName(player).equals("DevBot__") &&item.equals(hiking_boots)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 1, true, false, true));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player || (event.getDamager() instanceof Projectile)&&(((Projectile)event.getDamager()).getShooter() instanceof Player)) {
            Player player;
            if(event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            }
            else
                player =  (Player) ((Projectile) event.getDamager()).getShooter();
            if (getPlayerName(player).equals("DevBot__")&&((event.getEntity() instanceof Animals) || (event.getEntity() instanceof Villager))) {
                Location current = player.getLocation();
                player.teleport(shak_jail);
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HURT, 1f, 1f);
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 1f, 1f);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(current);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("EpicServerPlugin"), 200);
            }
        }
    }

    private String getPlayerName(Player player) {
        Component playerNameComponent = player.displayName();
        String name = ((TextComponent)playerNameComponent).content();
        return name;
    }
}
