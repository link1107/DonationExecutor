package igorlink.executions;

import igorlink.service.MainConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static igorlink.service.Utils.announce;

public class ShitToInventory implements IExecution {
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "насрал тебе в инвентарь", "насрал в инвентарь", player, true);
        Material itemType = Material.DIRT;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cГОВНО ОТ §f" + donationUsername.toUpperCase());
        meta.setLore(Arrays.asList("§7Это говно ужасно вонюче и занимает много места"));
        itemStack.setItemMeta(meta);

        for (int i = 0; i < MainConfig.dirtAmount; i++) {
            player.getInventory().addItem(itemStack);
        }

    }
}
