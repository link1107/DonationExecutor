package igorlink.executions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static igorlink.service.Utils.announce;

public class CallNKVD implements IExecution {
    @Override
    public void execute(Player player, String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity nkvdMob;
        announce(donationUsername, "хочет отправить тебя в ГУЛАГ!", "хочет отправить в ГУЛАГ", player, true);
        direction.setY(0);
        direction.normalize();
        for (int i = 1; i <= 3; i++) {
            Location newloc = player.getLocation().clone();
            Vector newdir = direction.clone();
            newdir = newdir.rotateAroundY(1.5708 * i).multiply(2);
            newloc.add(newdir);
            nkvdMob = (LivingEntity) player.getWorld().spawnEntity(newloc, EntityType.ZOMBIE);
            nkvdMob.setCustomName("§cСотрудник НКВД");
            nkvdMob.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
            nkvdMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        }

    }
}
