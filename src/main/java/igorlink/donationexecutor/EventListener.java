package igorlink.donationexecutor;

import igorlink.donationexecutor.executionsstaff.StreamerPlayer;
import igorlink.service.Utils;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static igorlink.service.Utils.*;

public class EventListener implements Listener {

    //Отмена горения НКВДшников
    @EventHandler
    public void onComburst(EntityCombustEvent e){
        if ((e.getEntity().getName().equals("§cСотрудник НКВД")) || (e.getEntity().getName().equals("§cИосиф Сталин"))) {
            e.setCancelled(true);
        }
    }

    //Закачка ресурспака и оповещение о том, что плагин не активен, если он не активен
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setResourcePack("https://download.mc-packs.net/pack/4923efe27212858f64c3ba65ff4bd35a42dadfb0.zip", Utils.decodeUsingBigInteger("4923efe27212858f64c3ba65ff4bd35a42dadfb0"));
        if (!isPluginActive) {
            sendSysMsgToPlayer(e.getPlayer(), " Плагин не активен. Укажите токен и свой никнейм в файле конфигурации плагина и перезапустите сервер.");
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        StreamerPlayer thisStreamerPlayer = DonationExecutor.getInstance().getStreamerPlayer(event.getPlayer().getName());
        List<Item> deathDrop = new ArrayList<>();
        if (event.getPlayer().getName().equals(thisStreamerPlayer.getName())) {
            for (ItemStack i : event.getDrops()) {
                deathDrop.add(event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), i));
            }
        }
        event.getDrops().clear();
        thisStreamerPlayer.setDeathDrop(deathDrop);
    }


    //Отмена дропа у НКВДшников
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntity().getName().equals("§cСотрудник НКВД")) {
            e.getDrops().clear();
        }
    }

}

