package igorlink.executions;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class DropActiveItem implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, "безуспешно пытался выбить у тебя предмет из рук", "безуспешно пытался выбить предмет из рук", player, true);
        } else {
            announce(donationUsername, "выбил у тебя предмет из рук", "выбил предмет из рук", player, true);
            player.dropItem(true);
            player.updateInventory();
        }

    }
}
