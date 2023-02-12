package me.doron.doronmc.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class EtamarHandler implements Listener {

    private final String USERNAME = "Marb_4";
    private final TextComponent BOOK_NAME = Component.text("תנ\"ך").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC);;
    private final double KOSHER_TIME = 20 * 60 * 20;
    private final double COOLDOWN = 120 * 20;
    private double lastMilk;
    private double lastMeat;
    private boolean interceptDeath;
    private double lastUse;
    private boolean abilityActive;

    public EtamarHandler() {
        lastMeat = 0;
        lastMilk = 0;
        lastUse = 0;
        abilityActive = false;
        interceptDeath = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals(USERNAME))
            return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.BOOK)
            return;

        if (!item.getItemMeta().displayName().equals(BOOK_NAME))
            return;

        double currentTime = player.getWorld().getTime();
        if (player.getWorld().getTime() - lastUse < COOLDOWN) {
            player.sendMessage("Prayer on cooldown.. " + ((COOLDOWN - (currentTime - lastUse)) / 20.0) + " seconds");
            return;
        }

        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 200, 5, 5, 5);
        player.getWorld().spawnParticle(Particle.FLASH, player.getLocation(), 100, 3, 3, 3);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1F, 2F);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1F, 2F);
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1F, 2F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 10, true, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 10, true, true, true));

        new BukkitRunnable() {
            @Override
            public void run() {
                endAbility(player);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("DoronMC"), 20*20);


        TextComponent message = Component.text("\"תמות נפשי עם פלשתים\"" + " - " + player.getName()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC);
        Bukkit.broadcast(message);
        lastUse = currentTime;
        abilityActive = true;
    }

    private void endAbility(Player player) {
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 100);
        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 2F);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getHealth() > 0)
                    player.getWorld().strikeLightning(player.getLocation());
                else {
                    interceptDeath = true;
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("DoronMC"), 1*20, 5);

    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (!event.getPlayer().getName().equals(USERNAME))
            return;

        ItemStack itemEaten = event.getItem();
        if (!isKosher(itemEaten, event.getPlayer())) {
            punish(event.getPlayer());
        }

        if (isMilk(itemEaten))
            lastMilk = event.getPlayer().getWorld().getTime();
        else if (isMeat(itemEaten))
            lastMeat = event.getPlayer().getWorld().getTime();

    }

    private boolean isKosher(ItemStack itemEaten, Player player) {
        boolean kosher = true;

        if (itemEaten.getType() == Material.PORKCHOP || itemEaten.getType() == Material.COOKED_PORKCHOP || itemEaten.getType() == Material.ROTTEN_FLESH || itemEaten.getType() == Material.RABBIT || itemEaten.getType() == Material.COOKED_RABBIT || itemEaten.getType() == Material.RABBIT_STEW || itemEaten.getType() == Material.PUFFERFISH || itemEaten.getType() == Material.SUSPICIOUS_STEW)
            return false;

        double current = player.getWorld().getTime();
        if (isMeat(itemEaten))
            kosher = Math.abs((current - lastMilk)) > KOSHER_TIME;

        else if (isMilk(itemEaten))
            kosher = Math.abs((current - lastMeat)) > KOSHER_TIME;

        return kosher;

    }

    private boolean isMilk(ItemStack item) {
        return item.getType() == Material.MILK_BUCKET || item.getType() == Material.CAKE;
    }

    private boolean isMeat(ItemStack item) {
        Material type = item.getType();
        return type == Material.BEEF  || type == Material.CHICKEN || type == Material.COOKED_BEEF || type == Material.COOKED_CHICKEN || type == Material.COOKED_MUTTON || type == Material.MUTTON;
    }

    private void punish(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2F, 2F);
        player.getWorld().spawnParticle(Particle.CRIMSON_SPORE, player.getLocation().add(0, 1, 0), 30);
        lastMeat = 0;
        lastMilk = -100000;
        interceptDeath = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().strikeLightning(player.getLocation());
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*20, 4, true, true, true));
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("DoronMC"), 12*2);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().getName().equals(USERNAME))
            return;

        if (!interceptDeath)
            return;

        if (!abilityActive)
            event.setDeathMessage(event.getPlayer().getName() + ChatColor.DARK_RED + " was smitten down by THE ALMIGHTY GOD for his sins.");
        else {
            event.setDeathMessage(event.getPlayer().getName() + ChatColor.DARK_RED + " was too strong to be kept alive.");
            abilityActive = false;
        }
        interceptDeath = false;
    }
}
