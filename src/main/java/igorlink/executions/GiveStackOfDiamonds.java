package igorlink.executions;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static igorlink.service.Utils.announce;

public class GiveStackOfDiamonds implements IExecution {
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "насыпал тебе алмазов!", "насыпал алмазов", player, true);
        Material itemType = Material.DIAMOND;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bАлмаз");
        meta.setLore(Arrays.asList("§7Эти алмазы подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        Item diamonds = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);

    }
}
