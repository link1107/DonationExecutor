package igorlink.donationexecutor;

import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import org.jetbrains.annotations.NotNull;


import java.util.List;

import static igorlink.service.Utils.*;

public class GeneralEventListener implements Listener {
    private static final String TEXTURE_PACK = "https://download.mc-packs.net/pack/65429ea1f5aae3b47e879834a1c538fa390f4b9b.zip";

    //Отмена горения НКВДшников
    @EventHandler
    public void onComburst(@NotNull EntityCombustEvent e){
        if ((e.getEntity().getName().equals("§cСотрудник НКВД")) || (e.getEntity().getName().equals("§cИосиф Сталин"))) {
            e.setCancelled(true);
        }
    }

    //Закачка ресурспака и оповещение о том, что плагин не активен, если он не активен
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (MainConfig.isForceResourcePack()) {
            player.setResourcePack(TEXTURE_PACK, Utils.decodeUsingBigInteger("65429ea1f5aae3b47e879834a1c538fa390f4b9b"));
        }

        if (MainConfig.isOptifineNotificationOn()) {
            sendSysMsgToPlayer(player, "для отображения кастомных скинов плагина на вашем\nклиенте игры должен быть установлен мод §bOptiFine.\n \n§fЕсли у вас не установлен данный мод, скачать его вы\nможете по ссылке: §b§nhttps://optifine.net/downloads\n\n§7§oДанное оповещение можно отключить в файле настроек\nплагина в папке сервера /plugins/DonationExecutor/\n \n");
        }

        if (!isPluginActive()) {
            sendSysMsgToPlayer(player, "плагин не активен. Укажите токен и свой никнейм в файле конфигурации плагина и перезапустите сервер.");
        }
    }

    //Отмена дропа у НКВДшников
    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent e){
        List<ItemStack> drops = e.getDrops();

        if (e.getEntity().getName().equals("§cСотрудник НКВД")) {
            drops.clear();
        }

        if (e.getEntity().getName().equals("N3koglai")) {
            Location loc = e.getEntity().getLocation();
            for (int i = 1; i <= Math.round(Math.random()*7); i++) {
                Vector direction = new Vector(
                        Math.random() - Math.random(),
                        Math.random(),
                        Math.random() - Math.random()
                );

                Zombie dwarf = (Zombie) e.getEntity().getWorld().spawnEntity(loc.clone().setDirection(direction), EntityType.ZOMBIE);
                dwarf.setCustomName("Гном");
                dwarf.setBaby();

                direction.setY(0);
                direction.normalize();
                direction.setY(0.3);
                dwarf.setVelocity(direction);
            }

            drops.clear();
        }

        if (e.getEntity().getName().equals("Гном")) {
            drops.clear();
            drops.add(new ItemStack(Material.GOLD_NUGGET, 1));
        }
    }

    @EventHandler
    public void onEatingBread(@NotNull PlayerItemConsumeEvent e) {
        if ( (e.getItem().getItemMeta().getDisplayName().equals("§6Советский Хлеб")) && (Math.round(Math.random()*8) == 3) ) {
            Player player = e.getPlayer();

            player.sendActionBar("ЭТОТ ХЛЕБ ОКАЗАЛСЯ ПРОСРОЧКОЙ!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }
    }
}

