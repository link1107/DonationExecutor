package igorlink.executions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static igorlink.service.Utils.announce;
import static igorlink.service.Utils.sendSysMsgToPlayer;
import static java.lang.Math.random;
import static java.lang.Math.round;

public class RandomChange implements IExecution {
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "подменил тебе кое-что на камни...", "призвал Сталина разобраться с", player, true);
        int[] randoms = new int[5];
        for (int i = 0; i <= 4; i++) {

            int temp = 0;
            Boolean isUnique = false;
            while (!isUnique) {
                temp = (int) (round(random() * 35));
                isUnique = true;
                int n;
                for (n = 0; n < i; n++) {
                    if (randoms[n] == temp) {
                        isUnique = false;
                        break;
                    }
                }
            }
            randoms[i] = temp;

        }

        String replacedItems = "";
        int replacedCounter = 0;
        for (int i = 0; i <= 4; i++) {
            if (!(player.getInventory().getItem(randoms[i]) == null)) {
                replacedCounter++;
                if (replacedCounter > 1) {
                    replacedItems = replacedItems + "§f, ";
                }
                replacedItems = replacedItems + "§b" + player.getInventory().getItem(randoms[i]).getAmount() + " §f" + player.getInventory().getItem(randoms[i]).getI18NDisplayName();
            }
            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player, "§cТебе повезло: все камни попали в пустые слоты!");
        } else {
            sendSysMsgToPlayer(player, "§cБыли заменены следующие предметусы: §f" + replacedItems);
        }

    }
}
