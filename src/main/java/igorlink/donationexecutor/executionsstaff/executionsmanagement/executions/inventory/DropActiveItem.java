package igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.inventory;

import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.AbstractExecution;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static igorlink.service.Utils.announce;

public class DropActiveItem extends AbstractExecution {

    @Override
    public Boolean execute(String donationUsername, Player player, String donationAmount) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, "безуспешно пытался выбить у тебя предмет из рук", "безуспешно пытался выбить предмет из рук", player, donationAmount,true);
        } else {
            announce(donationUsername, "выбил у тебя предмет из рук", "выбил предмет из рук", player, donationAmount,true);
            player.dropItem(true);
            player.updateInventory();
        }
        return true;
    }
}
