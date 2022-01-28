package igorlink.executions;

import org.bukkit.entity.*;

import static igorlink.service.Utils.announce;

public class TamedBecomesEnemies implements IExecution{
    @Override
    public void execute(Player player, String donationUsername) {
        announce(donationUsername, "настроил твоих питомцев против тебя!", "настроил прирученных питомцев против", player, true);
        for (Entity e : player.getWorld().getEntitiesByClasses(Wolf.class, Cat.class)) {
            if (((Tameable) e).isTamed() && ((Tameable) e).getOwner().getName().equals(player.getName())) {
                if (e instanceof Cat) {
                    ((Tameable) e).setOwner(null);
                    ((Cat) e).setSitting(false);
                    ((Cat) e).setTarget(player);
                    player.sendMessage("+");
                } else {
                    ((Wolf) e).setSitting(false);
                    ((Tameable) e).setOwner(null);
                    ((Wolf) e).setTarget(player);
                }
            }
        }

    }
}
