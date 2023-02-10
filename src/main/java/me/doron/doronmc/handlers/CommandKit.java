package me.doron.doronmc.handlers;

import me.doron.doronmc.util.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandKit implements CommandExecutor {
    private HashMap<String, ItemStack[]> kits;

    public CommandKit() {
        kits = new HashMap<>();
        ItemStack[] liamKit = new ItemStack[2];

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 2);
        ItemMeta meta = boots.getItemMeta();
        meta.setDisplayName("Liam eggs");
        boots.setItemMeta(meta);
        liamKit[0] = boots;

        ItemStack egg = new ItemStack(Material.EGG, 1);
        egg.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
        ItemMeta metaEgg = egg.getItemMeta();
        metaEgg.setDisplayName("E G G");
        egg.setItemMeta(meta);
        liamKit[1] = egg;
        kits.put("liam", liamKit);

        ItemStack[] shakedKit = new ItemStack[1];
        ItemStack hiking_boots = new ItemStack(Material.LEATHER_BOOTS,1);
        ItemMeta metaBoots = hiking_boots.getItemMeta();
        metaBoots.setDisplayName("Hiking Boots");
        hiking_boots.setItemMeta(metaBoots);
        shakedKit[0] = hiking_boots;
        kits.put("shaked", shakedKit);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length > 0)
            setKit(player, kits.get(args[0]));
        return true;
    }


    private void setKit(Player player, ItemStack[] kit)
    {
        EntityEquipment equip = player.getEquipment();
        Inventory inv = player.getInventory();

        for (ItemStack item : kit) {
            if (ItemStackUtils.isBoots(item))
                equip.setBoots(item);
            else if (ItemStackUtils.isChestplate(item))
                equip.setChestplate(item);
            else if (ItemStackUtils.isLeggings(item))
                equip.setLeggings(item);
            else
                inv.addItem(item);
        }
    }
}
