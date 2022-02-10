package igorlink.donationexecutor;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import static igorlink.service.Utils.*;

public class GeneralEventListener implements Listener {

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
        if (MainConfig.isForceResourcePack()) {
            e.getPlayer().setResourcePack("https://download.mc-packs.net/pack/5cd9b26a954b46683eeea1b7df1cc0fbe136f79f.zip", Utils.decodeUsingBigInteger("5cd9b26a954b46683eeea1b7df1cc0fbe136f79f"));
        }

        if (MainConfig.isOptifineNotificationOn()) {
            sendSysMsgToPlayer(e.getPlayer(), "для отображения кастомных скинов плагина на вашем\nклиенте игры должен быть установлен мод §bOptiFine.\n \n§fЕсли у вас не установлен данный мод, скачать его вы\nможете по ссылке: §b§nhttps://optifine.net/downloads\n\n§7§oДанное оповещение можно отключить в файле настроек\nплагина в папке сервера /plugins/DonationExecutor/\n \n");
        }

        if (!isPluginActive()) {
            sendSysMsgToPlayer(e.getPlayer(), "плагин не активен. Укажите токен и свой никнейм в файле конфигурации плагина и перезапустите сервер.");
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

