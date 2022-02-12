package igorlink.donationexecutor.executionsstaff.giantmobs;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

class GiantMobEventListener implements Listener {
    private final GiantMobManager thisInstanceOfGiantMobManager;

    GiantMobEventListener(@NotNull GiantMobManager _thisInstanceOfGiantMobManager) {
        thisInstanceOfGiantMobManager = _thisInstanceOfGiantMobManager;
    }

    //Добавляем моба в наш список, если это гигант с кастомным именем
    @EventHandler
    private void onGiantMobAddToWorld(@NotNull EntityAddToWorldEvent e) {
        Entity entity = e.getEntity();

        if ((entity instanceof Giant) && entity.getCustomName() != null) {
            thisInstanceOfGiantMobManager.addMob((LivingEntity) entity);
        }
    }

    //Удаляем моба из нашего списка, когда он исчезает с карты
    @EventHandler
    private void onGiantMobRemoveFromWorld(@NotNull EntityRemoveFromWorldEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Giant) {
            thisInstanceOfGiantMobManager.removeMob((LivingEntity) entity);
        }
    }

    @EventHandler
    public void onGiantMobDamage(@NotNull EntityDamageEvent e){
        //Нашему мобу отменяем дамаг от падения
        if(e.getEntity() instanceof Giant) {
            switch (e.getCause()) {
                case FALL, BLOCK_EXPLOSION, FIRE, FIRE_TICK -> e.setCancelled(true);
            }
        }
    }

    //Обрабатываем попадание снежка и файербола
    @EventHandler
    public void onProjectileHit(@NotNull ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        Entity hitEntity = e.getHitEntity();

        if (entity instanceof Snowball && (((Snowball) entity).getItem().getLore().contains("Stalinball")) ) {
            //Урон от снежка
            if (hitEntity instanceof LivingEntity) {
                ((LivingEntity) hitEntity).damage(1.0D);
            }
        } else if (entity instanceof Fireball && entity.hasMetadata("type")) {
            //Взрыв от файербола
            if (!(hitEntity instanceof Giant)) {
                entity.getWorld().createExplosion(entity.getLocation(), 2.0F, true);
                entity.remove();
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onComeTooClose(@NotNull EntityMoveEvent e){
        //дать пинка, если слишком близок к гигантскому мобу
        if (e.getEntity() instanceof Giant) {
            for (Entity ent : e.getEntity().getNearbyEntities(1.9, 4, 1.9)) {
                if (ent instanceof LivingEntity) {
                    Vector launchDirection = e.getEntity().getLocation().getDirection()
                            .setY(0)
                            .normalize()
                            .multiply(0.8)
                            .setY(0.4);

                    ent.setVelocity(launchDirection);
                }
            }
        }
    }

}
