package igorlink.executions;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static igorlink.service.Utils.announce;

public class GiveBread implements IExecution {
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "дал тебе хлеба!", "дал хлеба", player, true);
        Material itemType = Material.BREAD;
        ItemStack itemStack = new ItemStack(itemType, 4);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bХлеб");
        meta.setLore(Arrays.asList("§7Этот хлеб подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        Item bread = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);

    }
}
