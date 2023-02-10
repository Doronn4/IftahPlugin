package me.doron.doronmc.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class LiamHandler implements Listener {
    private final String NAME = "teleliamis";
    private Location spawn;
    private Random rnd = new Random();
    private final Material[] mats = {Material.EMERALD_BLOCK,Material.LAPIS_BLOCK,Material.BLACKSTONE,Material.LAPIS_ORE,Material.COBWEB,Material.COAL_BLOCK};

    public LiamHandler() {
        WorldCreator worldCreator = new WorldCreator("flat");
        World world = worldCreator.createWorld();

        spawn = new Location(world, 100, 110, 100);
        for (int i =-10; i<10; i++)
        {
            for (int j=-10; j<10; j++)
            {
                Block block = world.getBlockAt(100-i, 100, 100-j);
                block.setType(mats[rnd.nextInt(6)]);
            }
        }
    }

    private String getPlayerName(Player player) {
        Component playerNameComponent = player.displayName();
        String name = ((TextComponent)playerNameComponent).content();
        return name;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(getPlayerName(player).equals(NAME)) {

            World world = player.getWorld();
            Location to = event.getTo();
            Block stepped = world.getBlockAt(to.getBlockX(), to.blockY()-1, to.blockZ());

            if(stepped.getType().equals(Material.COAL_ORE)||stepped.getType().equals(Material.COAL_BLOCK)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 5, true, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 5, true, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, true, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5, true, true, true));
                player.playSound(player.getLocation(),Sound.AMBIENT_CAVE,1f,1f);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (getPlayerName(event.getPlayer()).equals(NAME)&&event.getRightClicked().getType() == EntityType.WOLF) {
            Wolf wolf = (Wolf) event.getRightClicked();
            AnimalTamer tamer = wolf.getOwner();

            // Check if the player is trying to tame the wolf
            if (tamer == null && event.getPlayer().getInventory().getItemInMainHand().getType().name().endsWith("BONE")) {
                wolf.setOwner(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.teleport(spawn);

    }
}
