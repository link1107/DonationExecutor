package igorlink.donationexecutor.executionsstaff;

import igorlink.service.MainConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExecUtils {

    public static void giveToPlayer(Player player, Material material, int amount, String donationUsername, String itemName) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setLore(List.of("§7Подарочек от §e" + donationUsername));
        itemStack.setItemMeta(meta);
        player.getWorld().dropItemNaturally(player.getLocation().clone().add(player.getLocation().getDirection().setY(0).normalize()), itemStack);
    }

    public static void giveToPlayer(Player player, Material material, int amount, String donationUsername) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(List.of("§7Подарочек от §e" + donationUsername));
        itemStack.setItemMeta(meta);
        player.getWorld().dropItemNaturally(player.getLocation().clone().add(player.getLocation().getDirection().setY(0).normalize()), itemStack);
    }
}
