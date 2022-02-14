package igorlink.donationexecutor;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


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
            e.getPlayer().setResourcePack("https://download.mc-packs.net/pack/65429ea1f5aae3b47e879834a1c538fa390f4b9b.zip", Utils.decodeUsingBigInteger("65429ea1f5aae3b47e879834a1c538fa390f4b9b"));
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
        if (e.getEntity().getName().equals("N3koglai")) {
            Location loc = e.getEntity().getLocation().clone();
            for (int i = 1; i <= Math.round(Math.random()*7); i++) {
                LivingEntity dwarf = (LivingEntity) e.getEntity().getWorld().spawnEntity(loc.clone().setDirection(new Vector((Math.random() - Math.random()), Math.random(), (Math.random() - Math.random()))), EntityType.ZOMBIE);
                dwarf.setCustomName("Гном");
                ((Zombie) dwarf).setBaby();
                Vector direction = dwarf.getLocation().getDirection().clone();
                direction.setY(0);
                direction.normalize();
                direction.setY(0.3);
                dwarf.setVelocity(direction.multiply(1));
            }
            e.getDrops().clear();
        }
        if (e.getEntity().getName().equals("Гном")) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 1));
        }
    }

    @EventHandler
    public void onEatingBread(PlayerItemConsumeEvent e) {
        if ( (e.getItem().getItemMeta().getDisplayName().equals("§6Советский Хлеб")) && (Math.round(Math.random()*8) == 3) ) {
            e.getPlayer().sendActionBar("ЭТОТ ХЛЕБ ОКАЗАЛСЯ ПРОСРОЧКОЙ!");
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }
    }

}

