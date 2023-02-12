package me.doron.doronmc.handlers;

import me.doron.doronmc.util.ItemStackUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        egg.setItemMeta(metaEgg);
        liamKit[1] = egg;
        kits.put("liam", liamKit);

        ItemStack[] shakedKit = new ItemStack[1];
        ItemStack hiking_boots = new ItemStack(Material.LEATHER_BOOTS,1);
        ItemMeta metaBoots = hiking_boots.getItemMeta();
        metaBoots.setDisplayName("Hiking Boots");
        hiking_boots.setItemMeta(metaBoots);
        shakedKit[0] = hiking_boots;
        kits.put("shaked", shakedKit);

        ItemStack[] gabzoKit = new ItemStack[1];

        ItemStack staff = new ItemStack(Material.STICK, 1);
        staff.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        ItemMeta staffMeta = staff.getItemMeta();
        TextComponent gabzoText = Component.text("3108")
                .color(NamedTextColor.GOLD);
        staffMeta.displayName(gabzoText);
        staff.setItemMeta(staffMeta);
        gabzoKit[0] = staff;
        kits.put("gabzo", gabzoKit);

        ItemStack[] mamanKit = new ItemStack[1];
        ItemStack skull = new ItemStack(Material.SKELETON_SKULL, 1);
        skull.addUnsafeEnchantment(Enchantment.LUCK, 10);
        ItemMeta skullMeta = skull.getItemMeta();
        TextComponent mamanText = Component.text("SKULL MODE")
                .color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC);
        skullMeta.displayName(mamanText);
        skull.setItemMeta(skullMeta);
        mamanKit[0] = skull;
        kits.put("maman", mamanKit);

        ItemStack[] itamarKit = new ItemStack[1];
        ItemStack book = new ItemStack(Material.BOOK, 1);
        book.addUnsafeEnchantment(Enchantment.THORNS, 10);
        ItemMeta bookMeta = book.getItemMeta();
        TextComponent itamarText = Component.text("תנ\"ך").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC);
        bookMeta.displayName(itamarText);
        book.setItemMeta(bookMeta);
        itamarKit[0] = book;
        kits.put("itamar", itamarKit);

        ItemStack[] doronKit = new ItemStack[1];
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        helmet.addUnsafeEnchantment(Enchantment.MENDING, 10);
        ItemMeta helmetMeta = helmet.getItemMeta();
        TextComponent doronText = Component.text("Cipher Hat").color(NamedTextColor.GREEN).decorate(TextDecoration.ITALIC);
        helmetMeta.displayName(doronText);
        helmet.setItemMeta(helmetMeta);
        doronKit[0] = helmet;
        kits.put("doron", doronKit);
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
            else if (ItemStackUtils.isHelmet(item))
                equip.setHelmet(item);
            else
                inv.addItem(item);
        }
    }
}
