package me.doron.doronmc.handlers;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MamanHandlerNew implements Listener {
    private final String USERNAME = "ProBooster";
    private final String HORSE_NAME = "jaquavious";
    private final double COOLDOWN = 30 * 20;
    private final double SPEED = 0.8;
    private final double HEALTH = 15;
    private final String COOL_NAME = "JAQUAVIOUS";
    private final long ABILITY_DURATION = 10 * 20;
    private Location lastLocation;
    private Horse savedHorse;
    private SkeletonHorse skullHorse;
    private double lastUse;
    private boolean abilityActive;

    public MamanHandlerNew() {
        lastUse = 0;
        abilityActive = false;
    }

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
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.getName().equals(USERNAME))
            return;

        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;


        if (!player.isInsideVehicle())
            return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.SKELETON_SKULL)
            return;

        if (abilityActive) {
            // Disable ability
            endAbility(player);
            return;
        }

        if (player.getVehicle().getType() != EntityType.HORSE)
            return;

        Horse horse = (Horse) player.getVehicle();
//        if (!horse.getName().equals(HORSE_NAME))
//            return;

        if (!horse.getPassengers().contains(player))
            return;


        double currentTime = player.getWorld().getTime();

        if (currentTime - lastUse > COOLDOWN) {
            // Activate ability
            abilityActive = true;
            lastUse = currentTime;
            savedHorse = horse;
            skullHorse = horse.getWorld().spawn(horse.getLocation(), SkeletonHorse.class);

            skullHorse.setTamed(horse.isTamed());
            skullHorse.setOwner(horse.getOwner());
            skullHorse.getInventory().setContents(horse.getInventory().getContents());
            skullHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(SPEED);
            skullHorse.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));

            horse.remove();
            skullHorse.addPassenger(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    endAbility(player);
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("DoronMC"), ABILITY_DURATION);
        }

    }


    @EventHandler
    public void onVehicleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.isInsideVehicle())
            return;

        if (!(player.getVehicle() instanceof SkeletonHorse))
            return;

        SkeletonHorse horse = (SkeletonHorse) player.getVehicle();
        if (!horse.equals(skullHorse))
            return;

        Vector lookDirection = player.getEyeLocation().getDirection();
        Vector behindVector = lookDirection.multiply(-3);
        Location behindLocation = horse.getLocation().add(behindVector).subtract(0, 1, 0);

        behindLocation.getBlock().setType(Material.FIRE);
    }

        private void endAbility(Player player) {
        Horse restoredHorse = savedHorse.getWorld().spawn(skullHorse.getLocation(), Horse.class);
        restoredHorse.setTamed(savedHorse.isTamed());
        restoredHorse.setOwner(savedHorse.getOwner());
        restoredHorse.setMaxHealth(savedHorse.getMaxHealth());
        restoredHorse.setHealth(savedHorse.getHealth());
        restoredHorse.setJumpStrength(savedHorse.getJumpStrength());
        restoredHorse.getInventory().setContents(savedHorse.getInventory().getContents());
        restoredHorse.setCustomNameVisible(true);
        restoredHorse.setCustomName(savedHorse.getName());

        skullHorse.remove();
        restoredHorse.addPassenger(player);
        abilityActive = false;
        lastUse = player.getWorld().getTime();
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
