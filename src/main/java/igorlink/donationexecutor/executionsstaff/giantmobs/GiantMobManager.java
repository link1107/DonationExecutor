package igorlink.donationexecutor.executionsstaff.giantmobs;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class GiantMobManager {
    private final HashMap<String, HashMap<UUID, GiantMob>> listOfMobLists = new HashMap<>();

    //Конструктор класса. Регистрируем в нем слушателя событий GiantEventListener
    public GiantMobManager(Plugin thisPlugin) {
            Bukkit.getPluginManager().registerEvents(new GiantMobEventListener(this), thisPlugin);
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (Entity ent : p.getWorld().getEntities()) {
                    if (ent instanceof Giant) {
                        this.addMob((LivingEntity) ent);
                    }
                }
            }
    }


    //Добавление и удаление моба, проверка его принадлежности к нашему классу
    //Добавляем нового моба, которого нужно заспавнить
    public void addMob(@NotNull Location playerLocation, @NotNull String mobName) {
        addMobToList(new GiantMob(playerLocation, mobName));
    }

    //Добавляем нового моба, который уже был заспавнен
    public void addMob(@NotNull LivingEntity giantMob) {
        addMobToList(new GiantMob(giantMob));
    }

    //ПРоверяем, есть ли у нас список с этим типом мобов
    public boolean contains(@NotNull LivingEntity livingEntity) {
        return listOfMobLists.containsKey(livingEntity.getCustomName());
    }

    //Удаляем моба
    public void removeMob(@NotNull LivingEntity giantMob) {
        removeMobFromList(giantMob);
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Добавление и удаления из списка мобов

    //Добавляем моба в оответствующий его типу список
    private void addMobToList(@NotNull GiantMob newGiantMob) {
        String giantName = newGiantMob.getName();
        UUID id = newGiantMob.getUUID();

        HashMap<UUID, GiantMob> hashMap = listOfMobLists.get(giantName);

        if(hashMap != null) {
            hashMap.put(id, newGiantMob);
        } else {
            hashMap = new HashMap<>();
            hashMap.put(id, newGiantMob);

            listOfMobLists.put(giantName, hashMap);
        }
    }

    //Удаляем моба из соответствующего ему списка
    private void removeMobFromList(@NotNull LivingEntity giantMob) {
        listOfMobLists.get(giantMob.getName()).remove(giantMob.getUniqueId());
    }
}
