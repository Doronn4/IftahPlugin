package me.doron.doronmc.handlers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;

public class GabzoHandler implements Listener {
    private final String NAME = "Itay3108";
    private final double COOLDOWN = 40 * 20;
    private final String ITEM_NAME = "3108";
    private double lastUse;
    private boolean isFlying;
    private boolean isLanding;
    private final long FLIGHT_TIME = 10 * 20;

    public GabzoHandler() {
        lastUse = 0;
        isFlying = false;
        isLanding = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(NAME) && !isFlying) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.STICK && item.getItemMeta().getDisplayName().equals(ITEM_NAME)) {
                    double currentTime = player.getWorld().getTime();
                    if ((currentTime - lastUse) > COOLDOWN) {
                        player.sendMessage(ChatColor.GOLD+""+ChatColor.MAGIC+"AA"+ChatColor.RESET+""+ChatColor.GOLD+""+ChatColor.UNDERLINE+""+ChatColor.ITALIC+"GABZO MODE ACTIVE"+ChatColor.RESET+""+ChatColor.GOLD+""+ChatColor.MAGIC+"AA");
                        activateFlight(player);
                        lastUse = currentTime + COOLDOWN;
                        isFlying = true;
                        new BukkitRunnable() {
                            public void run() {
                                shutOffFlight(player);
                            }
                        }.runTaskLater(Bukkit.getPluginManager().getPlugin("DoronMC"), FLIGHT_TIME);
                    } else {
                        // Cooldown
                        player.sendMessage("Flight on cooldown.. " + ((COOLDOWN - (currentTime - lastUse)) / 20.0) + " seconds");
                    }
                }
            }
        }
    }

    public void activateFlight(Player player) {
        World world = player.getWorld();
        player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 0.5F, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_POLAR_BEAR_WARNING, 0.5F, 0.7F);

        double startHeight = player.getLocation().getY();
        double heightDelta = 40;
        double acceleration = 0.8; // acceleration in blocks per second squared

        stepTwoFlight(player);

        new BukkitRunnable() {
            double liftSpeed = 0;
            double elapsedTime = 0;
            boolean stepTwo = false;

            public void run() {
                elapsedTime += 0.05; // elapsed time in seconds (update every 50 milliseconds)
                liftSpeed += acceleration * elapsedTime; // lift speed in blocks per second
                if (player.getLocation().getY() - startHeight >= heightDelta) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }

                else {
                    player.setVelocity(new Vector(player.getVelocity().getX(), liftSpeed, player.getVelocity().getZ())); // set the lift velocity
                    world.spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 10);
                    world.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 10);

                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("DoronMC"), 0, 1);

    }

    private void shutOffFlight(Player player) {
        isFlying = false;
        isLanding = true;
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setVelocity(new Vector(0, -4, 0));
        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5F, 1F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 15 * 20, 1, true, false, false));
    }

    private void stepTwoFlight(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_POLAR_BEAR_AMBIENT, 0.5F, 2F);
        double launchSpeed = 7;
        Vector launchVector = player.getEyeLocation().getDirection().normalize().multiply(launchSpeed);
        launchVector.setY(0);
        player.setVelocity(player.getVelocity().add(launchVector));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (isLanding) {
            Location belowFeet = new Location(event.getTo().getWorld(), event.getTo().getX(), event.getTo().getY() - 1, event.getTo().getZ());
            if (belowFeet.getBlock().getType() != Material.AIR) {
                event.getPlayer().removePotionEffect(PotionEffectType.SLOW_FALLING);
                event.getPlayer().getWorld().strikeLightning(event.getPlayer().getLocation());
                isLanding = false;
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.getName().equals(NAME)) {
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }
}
