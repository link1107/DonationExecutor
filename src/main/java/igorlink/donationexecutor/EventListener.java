package igorlink.donationexecutor;

import igorlink.service.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import static igorlink.service.Utils.*;

public class EventListener implements Listener {

    private Map<Integer, BukkitTask> projectiles = new HashMap<Integer, BukkitTask>();

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
            sendSysMsgToPlayer(e.getPlayer(), " плагин не активен. Укажите токен и свой никнейм в файле конфигурации плагина и перезапустите сервер.");
        }
    }


    //Отмена дропа у НКВДшников
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntity().getName().equals("§cСотрудник НКВД")) {
            e.getDrops().clear();
        }
    }

}

