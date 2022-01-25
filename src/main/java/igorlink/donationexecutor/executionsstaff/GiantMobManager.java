package igorlink.donationexecutor.executionsstaff;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import igorlink.donationexecutor.DonationExecutor;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.*;

import static igorlink.service.Utils.*;
import static java.lang.Math.sqrt;
import static org.bukkit.Bukkit.getPlayer;

import org.bukkit.event.Listener;

public class GiantMobManager {
    private HashMap<String, HashMap> listOfMobLists = new HashMap<String, HashMap>();
    private HashMap<UUID, GiantMob> listOfGiantMob = new HashMap<UUID, GiantMob>();

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Добавление и удаление моба
    //Добавляем нового моба, которого нужно заспавнить
    public void addMob(@NotNull Location playerLocation, @NotNull String mobName) {
        addMobToList(new GiantMob(playerLocation, mobName));
    }

    //Добавляем нового моба, который уже был заспавнен
    public void addMob(@NotNull LivingEntity giantMob) {
        addMobToList(new GiantMob(giantMob));
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
        if (listOfMobLists.containsKey(newGiantMob.getName())) {
            listOfMobLists.get(newGiantMob.getName()).put(newGiantMob.getUUID(), newGiantMob);
        } else {
            listOfMobLists.put(newGiantMob.getName(), new HashMap());
            listOfMobLists.get(newGiantMob.getName()).put(newGiantMob.getUUID(), newGiantMob);
        }

    }

    //Удаляем моба из соответствующего ему списка
    private void removeMobFromList(@NotNull LivingEntity giantMob) {
        listOfMobLists.get(giantMob.getName()).remove(giantMob.getUniqueId());
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Класс гигантского моба
    private class GiantMob {
        private int timesThisGiantMobIsOnOnePlace = 0;
        private String thisGiantMobPlayerCurrentTargetName = null;
        private int stepsAfterHiding = 0;
        private LivingEntity giantMobLivingEntity = null;
        private UUID thisGiantMobUUID = null;

        final private int howManySnowballsMobLaunches = 4;
        final private Boolean SnowballsFollowingTarget = false;
        final private int giantMobTrackingRange = 40;
        final private static int timeBeforeThisGiantMobForgetHisTarget = 5;
        final private static int ticksBetweenSnowballsShots = 7;

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Конструктор гигантского моба

        //Создаем моба, заспавнив его и указав Имя-тип
        public GiantMob(@NotNull Location playerLocation, String mobName) {

            //Определяем направление игрока и спавним моба перед ним, повернутым к игроку лицом
            Vector playerDirection = playerLocation.getDirection();
            playerDirection.setY(0);
            playerDirection.normalize();
            this.giantMobLivingEntity = (LivingEntity) playerLocation.getWorld().spawnEntity(playerLocation.clone().add(playerDirection.clone().multiply(5)).setDirection(playerDirection.multiply(-1)), EntityType.GIANT);
            if (!(mobName == null)) {
                this.giantMobLivingEntity.setCustomName(mobName);
            }
            this.giantMobLivingEntity.setRemoveWhenFarAway(false);
            this.thisGiantMobUUID = giantMobLivingEntity.getUniqueId();
            this.giantMobLivingEntity.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));

            //Заставляем бегать и стрелять
            this.turnOnGiantMobAi();
        }

        //Добавляем существующего
        private GiantMob(@NotNull LivingEntity giantMobLivingEntity) {
            this.giantMobLivingEntity = giantMobLivingEntity;

            //Заставляем бегать и стрелять
            this.turnOnGiantMobAi();
        }

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Геттеры

        //Отдает имя моба
        public String getName() {
            return this.giantMobLivingEntity.getName();
        }

        //Отдает UUID моба
        public UUID getUUID() {
            return this.thisGiantMobUUID;
        }

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Поиск ближайшей цели
        private LivingEntity findGiantMobTarget() {
            //получаем список ближайших врагов dв радиусе sralinShootingRadius
            Double sralinShootingRadius = (double) giantMobTrackingRange;
            List<Entity> listOfNearbyEntities = giantMobLivingEntity.getNearbyEntities(sralinShootingRadius, sralinShootingRadius, sralinShootingRadius);
            List<LivingEntity> listOfNearbyPlayers = new ArrayList<>();
            List<LivingEntity> listOfNearbyLivingEntities = new ArrayList<>();

            //Пробегаемся и ищем игроков
            for (Entity e : listOfNearbyEntities) {
                RayTraceResult rtRes1 = null;
                RayTraceResult rtRes2 = null;
                RayTraceResult rtRes3 = null;
                RayTraceResult rtRes4 = null;
                RayTraceResult rtRes5 = null;
                RayTraceResult rtRes6 = null;
                RayTraceResult rtRes7 = null;
                RayTraceResult rtRes8 = null;

                if (e instanceof LivingEntity) {
                    //Позиции псевдоглаз вокруг головы с каждой стороны
                    Location rtGiantMobPseudoEyesPos1 = giantMobLivingEntity.getLocation().clone().add(2, 11, 0);
                    Location rtGiantMobPseudoEyesPos2 = giantMobLivingEntity.getLocation().clone().add(-2, 11, 0);
                    Location rtGiantMobPseudoEyesPos3 = giantMobLivingEntity.getLocation().clone().add(0, 11, 2);
                    Location rtGiantMobPseudoEyesPos4 = giantMobLivingEntity.getLocation().clone().add(0, 11, -2);

                    //Пускаем лучи из каждой точки псевдоглаз до верха и низа каждой сущности
                    rtRes1 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos1, genVec(rtGiantMobPseudoEyesPos1, ((LivingEntity) e).getEyeLocation()), rtGiantMobPseudoEyesPos1.distance(((LivingEntity) e).getEyeLocation()), FluidCollisionMode.NEVER, true);
                    rtRes2 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos2, genVec(rtGiantMobPseudoEyesPos2, ((LivingEntity) e).getEyeLocation()), rtGiantMobPseudoEyesPos2.distance(((LivingEntity) e).getEyeLocation()), FluidCollisionMode.NEVER, true);
                    rtRes3 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos3, genVec(rtGiantMobPseudoEyesPos3, ((LivingEntity) e).getEyeLocation()), rtGiantMobPseudoEyesPos3.distance(((LivingEntity) e).getEyeLocation()), FluidCollisionMode.NEVER, true);
                    rtRes4 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos4, genVec(rtGiantMobPseudoEyesPos4, ((LivingEntity) e).getEyeLocation()), rtGiantMobPseudoEyesPos4.distance(((LivingEntity) e).getEyeLocation()), FluidCollisionMode.NEVER, true);
                    rtRes5 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos1, genVec(rtGiantMobPseudoEyesPos1, ((LivingEntity) e).getLocation()), rtGiantMobPseudoEyesPos1.distance(((LivingEntity) e).getLocation()), FluidCollisionMode.NEVER, true);
                    rtRes6 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos2, genVec(rtGiantMobPseudoEyesPos2, ((LivingEntity) e).getLocation()), rtGiantMobPseudoEyesPos2.distance(((LivingEntity) e).getLocation()), FluidCollisionMode.NEVER, true);
                    rtRes7 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos3, genVec(rtGiantMobPseudoEyesPos3, ((LivingEntity) e).getLocation()), rtGiantMobPseudoEyesPos3.distance(((LivingEntity) e).getLocation()), FluidCollisionMode.NEVER, true);
                    rtRes8 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos4, genVec(rtGiantMobPseudoEyesPos4, ((LivingEntity) e).getLocation()), rtGiantMobPseudoEyesPos4.distance(((LivingEntity) e).getLocation()), FluidCollisionMode.NEVER, true);

                    //Если Сталин может из любой позиции поврота голвоы увидеть верх или низ цели, то эта цель вносится в список кандидатов
                    if ((rtRes1 == null) || (rtRes2 == null) || (rtRes3 == null) || (rtRes4 == null) || (rtRes5 == null) || (rtRes6 == null) || (rtRes7 == null) || (rtRes8 == null)) {
                        if ((e instanceof Player) && (!(((Player) e).getGameMode() == GameMode.SPECTATOR)) && (!(((Player) e).getGameMode() == GameMode.CREATIVE)) ) {
                            listOfNearbyPlayers.add((LivingEntity) e);
                        } else if (!(e instanceof Player)) {
                            listOfNearbyLivingEntities.add((LivingEntity) e);
                        }
                    }
                }

            }

            //Создаем переменную будущей цели
            LivingEntity target = null;
            Double minDistance = null;

            if (!(listOfNearbyPlayers.isEmpty())) {

                //Если есть игроки - ищем среди них ближайшего
                for (LivingEntity e : listOfNearbyPlayers) {
                    if (target == null) {
                        target = e;
                        minDistance = giantMobLivingEntity.getLocation().distance(e.getLocation());
                    } else if (minDistance > giantMobLivingEntity.getLocation().distance(e.getLocation())) {
                        target = e;
                        minDistance = giantMobLivingEntity.getLocation().distance(e.getLocation());
                    }
                }

                //Если новая цель - сбрасываем счетчик забвения после скрытия из области видимости моба, и назначаем цель текущей целью моба
                if (!(target.getName().equals(thisGiantMobPlayerCurrentTargetName))) {
                    stepsAfterHiding = 0;
                    thisGiantMobPlayerCurrentTargetName = target.getName();
                }

            } else if (!(listOfNearbyLivingEntities.isEmpty()))  {
                    //Если игроков рядом не было, проверяем все живые сущности
                    for (LivingEntity e : listOfNearbyLivingEntities) {
                        if (target == null) {
                            target = e;
                            minDistance = this.giantMobLivingEntity.getLocation().distance(e.getLocation());
                        } else if (minDistance > this.giantMobLivingEntity.getLocation().distance(e.getLocation())) {
                            target = e;
                            minDistance = this.giantMobLivingEntity.getLocation().distance(e.getLocation());
                        }
                    }

            }

            if ( (!(target instanceof Player)) && (!(thisGiantMobPlayerCurrentTargetName == null)) ) {
                //Если прошлая цель-игрок существует, и он не мертв и находится в том же мире, что и наш моб
                if ( (!(getPlayer(thisGiantMobPlayerCurrentTargetName).isDead())) && (getPlayer(thisGiantMobPlayerCurrentTargetName).getWorld() == giantMobLivingEntity.getWorld()) ) {

                    //Если не подошло время забыть о нем и он не стал спектэйтором, фокусим моба на него
                    if ((stepsAfterHiding <= timeBeforeThisGiantMobForgetHisTarget * 2) && (!(getPlayer(thisGiantMobPlayerCurrentTargetName).getGameMode()==GameMode.SPECTATOR)) && (!(getPlayer(thisGiantMobPlayerCurrentTargetName).getGameMode()==GameMode.CREATIVE)) ){
                        target = getPlayer(thisGiantMobPlayerCurrentTargetName);
                        stepsAfterHiding++;
                    } else {
                        //если подошло время забыть про него - забываем
                        stepsAfterHiding = 0;
                        thisGiantMobPlayerCurrentTargetName = null;
                    }

                }
            }

            //Возвращаем ближайшее ентити (игрок в приоритете)
            return target;
        }

        private void turnOnGiantMobAi() {
            this.forceGiantMobToMove();
            this.makeGiantMobAttackWithFireballs();
            this.makeGiantMobAttackWithSnowballs();
        }

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Заставляем нашего моба двигаться
        private void forceGiantMobToMove() {
            final GiantMob thisGiantMob = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    LivingEntity thisGiantMobLivingEntity = thisGiantMob.giantMobLivingEntity;

                    if ( (thisGiantMobLivingEntity.isDead()) || (!(DonationExecutor.isRunning())) ) {
                        //Если Сталин уже помер, отрубаем задание
                        this.cancel();
                        return;
                    } else {
                        //Если не помер, находим ближайшую цель (игроки в приоритете)
                        LivingEntity target;
                        target = thisGiantMob.findGiantMobTarget();

                        //Если цели нет...
                        if (target == null) {
                            //Если моб не в воде, то прижимаем его к Земле, чтобы не улетел в небо на прыжке
                            if (!(thisGiantMobLivingEntity.getEyeLocation().clone().add(0,2,0).getBlock().getType()==Material.WATER)) {
                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLivingEntity.getLocation().getDirection().clone().normalize().setY(-4.5));
                            } else {
                                //сли над мобом вода - всплываем
                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLivingEntity.getLocation().getDirection().clone().normalize().setY(3));
                            }
                            return;
                        }

                        //Поворачиваем в плоскости XZ
                        float newYaw = (float) Math.toDegrees(
                                Math.atan2(
                                        target.getLocation().getZ() - thisGiantMobLivingEntity.getLocation().getZ(),
                                        target.getLocation().getX() - thisGiantMobLivingEntity.getLocation().getX()))
                                - 90;

                        //Поворачиваем по вертикальной оси Y (не работает из-за зачатков AI, которые сами крутят моба)
                        double xDiff = target.getEyeLocation().getX() - thisGiantMobLivingEntity.getEyeLocation().getX();
                        double yDiff = target.getEyeLocation().getY() - thisGiantMobLivingEntity.getEyeLocation().getY();
                        double zDiff = target.getEyeLocation().getZ() - thisGiantMobLivingEntity.getEyeLocation().getZ();

                        double distanceXZ = sqrt(xDiff * xDiff + zDiff * zDiff);
                        double distanceY = sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

                        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
                        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
                        if (zDiff < 0.0D) {
                            yaw += Math.abs(180.0D - yaw) * 2.0D;
                        }

                        thisGiantMobLivingEntity.setRotation(newYaw, (float) pitch);


                        //Готовимся к совершению движения
                        Location thisGiantMobLoc = thisGiantMobLivingEntity.getLocation().clone();
                        Location thisGiantMobLocXZ = thisGiantMobLivingEntity.getLocation().clone();
                        thisGiantMobLocXZ.setY(0);
                        double thisGiantMobLocY = thisGiantMobLoc.clone().getY();

                        Location targetLoc = target.getLocation().clone();
                        Location targetLocXZ = target.getLocation().clone();
                        targetLocXZ.setY(0);
                        double targetY = targetLoc.clone().getY();


                        //Совершаем движение, если мы далеко от цели, либо если мы сильно под ней
                        if ((thisGiantMobLocXZ.distance(targetLocXZ) > 1) || (((targetY - thisGiantMobLocY) > 20) && (thisGiantMobLocY < targetY))) {
                            double oldX = thisGiantMobLoc.getX();
                            double oldZ = thisGiantMobLoc.getZ();
                            //sendSysMsgToPlayer(Bukkit.getPlayer("Evropejets"), "ДВИЖЕНИЕ! До цели " + (int) (Math.round(thisGiantMobLoc.distance(target.getLocation()))) + "\nЦЕЛЬ: " + target.getName());
                            //Стандартное движение к цели, если она за радиусом досягаемости
                            if (!(thisGiantMobLivingEntity.getEyeLocation().clone().add(0,1,0).getBlock().getType()==Material.WATER)) {
                                thisGiantMobLivingEntity.setRemainingAir(thisGiantMobLivingEntity.getMaximumAir());
                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().setY(-4.5));
                            } else {
                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().setY(1.4));
                                return;
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {

                                    if ( (thisGiantMobLivingEntity.isDead()) || (!(DonationExecutor.isRunning())) ) {
                                        this.cancel();
                                        return;
                                    }

                                    //Если моб не сдвинулся за счет стандартного движения (разница между новыми и старыми кордами<2), и при этом он находится ниже цели по Y, но дальше по XZ, чем на 2.5 блока от нее (то есть ему есть куда стремиться до цели)...
                                    if ((Math.abs(thisGiantMobLivingEntity.getLocation().getX() - oldX) < 1) && (Math.abs(thisGiantMobLivingEntity.getLocation().getZ() - oldZ) < 1) && ((thisGiantMobLocY < targetY) || (thisGiantMobLocXZ.distance(targetLocXZ) > 2.5))) {
                                        thisGiantMob.timesThisGiantMobIsOnOnePlace++;
                                        if (thisGiantMob.timesThisGiantMobIsOnOnePlace > 2) {
                                            if (timesThisGiantMobIsOnOnePlace == 3) {
                                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().multiply(1).setY(4));
                                            } else if (timesThisGiantMobIsOnOnePlace == 6) {
                                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(0.9).multiply(2).setY(5));
                                            } else if (timesThisGiantMobIsOnOnePlace == 9) {
                                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(-0.9).multiply(2).setY(5));
                                            } else if (timesThisGiantMobIsOnOnePlace == 10) {
                                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(-0.9).multiply(2).setY(5));
                                            } else if (timesThisGiantMobIsOnOnePlace == 13) {
                                                thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(0.9).multiply(2).setY(5));
                                                thisGiantMob.timesThisGiantMobIsOnOnePlace = 0;
                                            }

                                        } else {
                                            //обычный прыжок
                                            thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().multiply(1).setY(1));
                                            // sendSysMsgToPlayer(Bukkit.getPlayer("Evropejets"), "HIGH");
                                        }

                                    } else {
                                        //если он сдвинулся, обнуляем счетчик нахождения на одном месте по XZ
                                        thisGiantMob.timesThisGiantMobIsOnOnePlace = 0;
                                    }

                                }
                            }.runTaskLater(DonationExecutor.getInstance(), 7);
                        }
                    }
                }
            }.runTaskTimer(DonationExecutor.getInstance(), 0, 10);
        }

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Заставляем моба стрелять файерболами
        private void makeGiantMobAttackWithFireballs() {
            final GiantMob thisGiantMob = this;
            new BukkitRunnable() {
                @Override
                public void run() {


                    LivingEntity thisGiantMobLivingEntity = thisGiantMob.giantMobLivingEntity;

                    //Если моб уже не существует, отменяем стрельбу
                    if ((thisGiantMobLivingEntity.isDead()) || (!(DonationExecutor.isRunning())) ) {
                        this.cancel();
                        return;
                    }

                    //Находим ближайшее ентити (игроки в приоритете), чтобы сделать из него цель для моба
                    LivingEntity target;
                    target = thisGiantMob.findGiantMobTarget();
                    if (target == null) {
                        return;
                    }

                    //Спавним файер болл на 2 блока ниже глаз (район между рук)
                    Fireball stalinBall;
                    stalinBall = (Fireball) thisGiantMobLivingEntity.getWorld().spawnEntity(thisGiantMobLivingEntity.getEyeLocation().clone()
                                    .add(genVec(thisGiantMobLivingEntity.getEyeLocation().clone(), target.getLocation()).clone().multiply(3.5)).clone()
                                    .add(0, -2, 0),
                            EntityType.FIREBALL);
                    stalinBall.setDirection(genVec(stalinBall.getLocation(), target.getLocation()).clone().multiply(2));

                }

            }.runTaskTimer(DonationExecutor.getInstance(), 0, 45);
        }

        private void makeGiantMobAttackWithSnowballs() {
            final GiantMob thisGiantMob = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    LivingEntity thisGiantMobLivingEntity = thisGiantMob.giantMobLivingEntity;

                    //Если моб уже не существует, отменяем стрельбу
                    if ( (thisGiantMobLivingEntity.isDead()) || (!(DonationExecutor.isRunning())) ) {
                        this.cancel();
                        return;
                    }

                    //Находим ближайшее ентити (игроки в приоритете), чтобы сделать из него цель для моба
                    LivingEntity target;
                    target = thisGiantMob.findGiantMobTarget();
                    if (target == null) {
                        return;
                    }

                    //Запускаем снежки в количестве howManySnowballsMobLaunches
                    for (int i = 0; i <= howManySnowballsMobLaunches; i++) {
                        final int finali = i;
                        final LivingEntity finalTarget = target;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if ( (thisGiantMob.giantMobLivingEntity.isDead())  || (!(DonationExecutor.isRunning())) ) {
                                    this.cancel();
                                    return;
                                }
                                Snowball snowball;
                                Location handHeightPoint;
                                handHeightPoint = thisGiantMobLivingEntity.getLocation().clone().add(0, 7, 0);
                                snowball = (Snowball) thisGiantMobLivingEntity.getWorld().spawnEntity(handHeightPoint.clone()
                                                .add(handHeightPoint.getDirection().rotateAroundY(0.7).multiply(3.5)),
                                        EntityType.SNOWBALL);
                                ItemStack itemStack = new ItemStack(Material.SNOWBALL, 1);
                                ItemMeta meta = snowball.getItem().getItemMeta();
                                meta.setLore(Arrays.asList("Stalinball"));
                                itemStack.setItemMeta(meta);
                                snowball.setItem(itemStack);
                                snowball.setVelocity(genVec(snowball.getLocation(), finalTarget.getEyeLocation()).multiply(2.2));
                                snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3, 2);

                                //Каждый тик направляем снежок в игрока
                                if (SnowballsFollowingTarget) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if ( (snowball.isDead()) || finalTarget.isDead() || (!(finalTarget.getWorld() == snowball.getWorld())) || (!(DonationExecutor.isRunning())) ) {
                                                this.cancel();
                                                return;
                                            } else {
                                                snowball.setVelocity(genVec(snowball.getLocation(), finalTarget.getEyeLocation()));
                                            }
                                        }
                                    }.runTaskTimer(DonationExecutor.getInstance(), finali * ticksBetweenSnowballsShots, 1);
                                }

                            }
                        }.runTaskLater(DonationExecutor.getInstance(), i * ticksBetweenSnowballsShots);
                    }


                }
            }.runTaskTimer(DonationExecutor.getInstance(), 0, 150);
        }
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Проверка всех добавлений и удалений из мира, чтобы ловить и упаковывать подходящих гигантов или удалять их из списка
    private class GiantMobEventListener implements Listener{

        GiantMobManager thisInstanceOfGiantMobManager;
        private GiantMobEventListener(GiantMobManager _thisInstanceOfGiantMobManager) {
            thisInstanceOfGiantMobManager = _thisInstanceOfGiantMobManager;
        }

        @EventHandler
        private void onGiantMobAddTOWorld(EntityAddToWorldEvent e) {
            if ((e.getEntity() instanceof Giant) && (e.getEntity().getCustomName() != null)) {
                thisInstanceOfGiantMobManager.addMob((LivingEntity) e.getEntity());
            }
        }

        @EventHandler
        private void onGiantMobRemoveFromWorld(EntityRemoveFromWorldEvent e) {
            if (thisInstanceOfGiantMobManager.listOfMobLists.containsKey(e.getEntity().getName())) {
                thisInstanceOfGiantMobManager.removeMob((LivingEntity) e.getEntity());
            }
        }

        @EventHandler
        public void onGiantMobDamage(EntityDamageEvent e){
            //Нашему мобу отменяем дамаг от падения
            if ( (e.getEntity() instanceof Giant) && ((e.getCause() == EntityDamageEvent.DamageCause.FALL) || (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (e.getCause() == EntityDamageEvent.DamageCause.FIRE) ||  (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))) {
                e.setCancelled(true);
               // sendSysMsgToPlayer(getPlayer(Executor.nameOfStreamerPlayer), "Cancelled DMG from: " + e.getCause().toString());
            } else if (e.getEntity() instanceof Giant) {
               // sendSysMsgToPlayer(getPlayer(Executor.nameOfStreamerPlayer), "Passed DMG from: " + e.getCause().toString());
            }
        }

        @EventHandler
        public void onProjectileHit(ProjectileHitEvent e) {
            if (e.getEntity() instanceof Snowball) {

                if ((e.getHitEntity()) instanceof LivingEntity) {

                    ((LivingEntity) e.getHitEntity()).damage(1.0D);
                }


            } else if (e.getEntity() instanceof Fireball) {

                if (!(e.getHitEntity() instanceof Giant)) {
                    e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2.0F, true);
                    e.getEntity().remove();
                    e.setCancelled(true);
                }
            }
        }


        @EventHandler
        public void onComeTooClose(EntityMoveEvent e){
            //дать пинка, если слишком близок к Сталину
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


}
