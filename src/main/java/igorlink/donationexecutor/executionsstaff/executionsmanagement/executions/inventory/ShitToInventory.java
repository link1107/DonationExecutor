package igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.inventory;

import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.AbstractExecution;
import igorlink.service.MainConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static igorlink.service.Utils.announce;

public class ShitToInventory extends AbstractExecution {

    @Override
    public Boolean execute(String donationUsername, Player player, String donationAmount) {
        announce(donationUsername, "насрал тебе в инвентарь", "насрал в инвентарь", player, donationAmount, true);
        Material itemType = Material.DIRT;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cГОВНО ОТ §f" + donationUsername.toUpperCase());
        meta.setLore(Arrays.asList("§7Это говно ужасно вонюче и занимает много места"));
        itemStack.setItemMeta(meta);
        for (int i = 0; i < MainConfig.getDirtAmount(); i++) {
            player.getInventory().addItem(itemStack);
        }
        return true;
    }
}
