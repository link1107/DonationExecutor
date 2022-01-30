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

public class GiantMobEventListener implements Listener {

    private GiantMobManager thisInstanceOfGiantMobManager;

    public GiantMobEventListener(GiantMobManager _thisInstanceOfGiantMobManager) {
        thisInstanceOfGiantMobManager = _thisInstanceOfGiantMobManager;
    }

    //Добавляем моба в наш список, если это гигант с кастомным именем
    @EventHandler
    private void onGiantMobAddTOWorld(EntityAddToWorldEvent e) {
        if ((e.getEntity() instanceof Giant) && (e.getEntity().getCustomName() != null)) {
            thisInstanceOfGiantMobManager.addMob((LivingEntity) e.getEntity());
        }
    }

    //Удаляем моба из нашего списка, когда он исчезает с карты
    @EventHandler
    private void onGiantMobRemoveFromWorld(EntityRemoveFromWorldEvent e) {
        if (thisInstanceOfGiantMobManager.contains((LivingEntity) e.getEntity())) {
            thisInstanceOfGiantMobManager.removeMob((LivingEntity) e.getEntity());
        }
    }

    @EventHandler
    public void onGiantMobDamage(EntityDamageEvent e){
        //Нашему мобу отменяем дамаг от падения
        if ( (e.getEntity() instanceof Giant) && ((e.getCause() == EntityDamageEvent.DamageCause.FALL) || (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (e.getCause() == EntityDamageEvent.DamageCause.FIRE) ||  (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))) {
            e.setCancelled(true);
            // sendSysMsgToPlayer(getPlayerExact(Executor.nameOfStreamerPlayer), "Cancelled DMG from: " + e.getCause().toString());
        } else if (e.getEntity() instanceof Giant) {
            // sendSysMsgToPlayer(getPlayerExact(Executor.nameOfStreamerPlayer), "Passed DMG from: " + e.getCause().toString());
        }
    }

    //Обрабатываем попадание снежка и файербола
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            //Урон от снежка
            if ((e.getHitEntity()) instanceof LivingEntity) {
                ((LivingEntity) e.getHitEntity()).damage(1.0D);
            }
        } else if (e.getEntity() instanceof Fireball) {
            //Взрыв от файербола
            if (!(e.getHitEntity() instanceof Giant)) {
                e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2.0F, true);
                e.getEntity().remove();
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onComeTooClose(EntityMoveEvent e){
        //дать пинка, если слишком близок к гигантскому мобу
        if (e.getEntity() instanceof Giant) {
            for (Entity ent : e.getEntity().getNearbyEntities(1.9, 4, 1.9)) {
                if (ent instanceof LivingEntity) {
                    Vector launchDirection = e.getEntity().getLocation().getDirection().clone().setY(0).normalize().multiply(0.8);
                    launchDirection.setY(0.4);
                    ent.setVelocity(launchDirection);
                }
            }
        }
    }

}
