package me.doron.doronmc.handlers;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

public class MamanHandler implements Listener {
    private final String USERNAME = "Arielmaman";
    private final String HORSE_NAME = "jaquavious";
    private final double SPEED = 0.6;
    private final double HEALTH = 15;
    private final String COOL_NAME = "JAQUAVIOUS";
    private double originalSpeed;
    private Location lastLocation;
    private Horse savedHorse;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(USERNAME)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location currentLocation = player.getLocation();
                    if (lastLocation == null || !lastLocation.equals(currentLocation)) {
                        lastLocation = currentLocation;
                    }
                    else if (lastLocation.equals(currentLocation)) {
                        dropAnvil(player);
                    }
                }
            }.runTaskTimer(Bukkit.getPluginManager().getPlugin("DoronMC"), 0, 20*10);
        }
    }



    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {

            Player player = (Player) event.getExited();

            if (event.getVehicle() instanceof Horse) {
                Horse horse = (Horse) event.getVehicle();
                if (player.getName().equals(USERNAME) && horse.getName().equals(HORSE_NAME)) {
                    horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(originalSpeed);
                }
            }
        }
    }


    @EventHandler
    public void onMount(EntityMountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getMount() instanceof Horse) {
                Horse horse = (Horse) event.getMount();
                if (player.getName().equals(USERNAME) && horse.getName().equals(HORSE_NAME)) {
                    originalSpeed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
                    horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(SPEED);
                }

            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(USERNAME) && event.getHand().equals(EquipmentSlot.HAND) && event.getRightClicked() instanceof Horse) {
            Horse horse = (Horse) event.getRightClicked();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().toString().contains("NAME_TAG")) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    String name = meta.getDisplayName();
                    if (name.equals(HORSE_NAME)) {
                        horse.setColor(Horse.Color.BLACK);
                        horse.setStyle(Horse.Style.NONE);
                        horse.setHealth(HEALTH);
                        horse.setCustomName(ChatColor.GREEN + COOL_NAME);
                        Bukkit.broadcast(Component.text(ChatColor.GREEN + HORSE_NAME+" time!"));
                        horse.setCustomNameVisible(true);
                    }
                }
            }
        }
    }

    public void dropAnvil(Player player) {
        Location location = player.getLocation().add(0, 10, 0);
        FallingBlock anvil = player.getWorld().spawnFallingBlock(location, Material.ANVIL, (byte) 0);
        anvil.setHurtEntities(true);
        anvil.setDropItem(false);
    }

    @EventHandler
    public void onAnvilHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Entity damager = event.getDamager();
        if (!(damager instanceof FallingBlock)) {
            return;
        }

        FallingBlock fallingBlock = (FallingBlock) damager;
        if (fallingBlock.getBlockData().getMaterial() != Material.ANVIL) {
            return;
        }

        Player player = (Player) entity;
        if (player.getHealth() - 18 <= 0)
            player.setHealth(0.5);
        else
            player.setHealth(player.getHealth() - 18);
        player.playSound(player.getLocation(), Sound.ENTITY_PIG_DEATH, 1, 2);
    }

    @EventHandler
    public void onAnvilLand(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock f = (FallingBlock) event.getEntity();

            if (f.getBlockData().getMaterial() == Material.ANVIL) {
                event.setCancelled(true);
                Block block = event.getBlock();
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(USERNAME)) {
            Block leg = player.getLocation().getBlock();
            Block step = player.getWorld().getBlockAt(leg.getX(), leg.getY()-1, leg.getZ());
            if (step.getType().equals(Material.DEAD_FIRE_CORAL) || leg.getType().equals(Material.DEAD_FIRE_CORAL))
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 69*20, 2, true, true, true));
        }
    }
}
