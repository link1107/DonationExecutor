package igorlink.service;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtils {
    public static ItemStack buildItem(ItemStack stack, String name, String[] lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }
            meta.setLore(list);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack buildItem(Material leatherPiece, String displayName, String[] lore, Color color) {
        ItemStack item = new ItemStack(leatherPiece);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (lore != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }
            meta.setLore(list);
        }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack buildItem(Material material, int amount, String name, String[] lore) {
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }
            meta.setLore(list);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack addLore(ItemStack stack, String[] lore) {
        ItemMeta meta = stack.getItemMeta();
        List<String> list;
        if (meta.getLore() != null) {
            list = meta.getLore();
        } else {
            list = new ArrayList<>();
        }
        for (String str : lore) {
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        meta.setLore(list);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack setLore(ItemStack stack, String[] lore) {
        ItemMeta meta = stack.getItemMeta();
        List<String> list = new ArrayList<>();
        if (lore != null) {
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }
        }
        meta.setLore(list);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack setLore(ItemStack stack, int i, String lore) {
        ItemMeta meta = stack.getItemMeta();
        List<String> list;
        if (meta.getLore() != null) {
            list = meta.getLore();
        } else {
            list = new ArrayList<>();
        }
        list.set(i, lore);
        meta.setLore(list);
        stack.setItemMeta(meta);
        return stack;
    }
}