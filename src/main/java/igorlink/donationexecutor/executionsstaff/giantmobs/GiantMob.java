package igorlink.donationexecutor.executionsstaff.giantmobs;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static igorlink.service.Utils.genVec;
import static java.lang.Math.sqrt;
import static org.bukkit.Bukkit.getPlayerExact;

//Класс гигантского моба
class GiantMob {
    private int timesThisGiantMobIsOnOnePlace = 0;
    private String thisGiantMobPlayerCurrentTargetName = null;
    private int stepsAfterHiding = 0;
    private LivingEntity giantMobLivingEntity = null;
    private UUID thisGiantMobUUID = null;

    private static final Boolean SnowballsFollowingTarget = false;
    private static final int NUMBER_OF_SNOWBALLS_AT_ONE_LAUNCH = 4;
    private static final int GIANT_MOBS_TRACKING_RANGE = 40;
    private static final int TIME_BEFORE_THIS_GIANT_MOB_FORGET_HIS_TARGET = 4;
    private static final int TICKS_BETWEEN_SNOWBALLS_SHOTS = 7;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Конструктор гигантского моба

    //Создаем моба, заспавнив его и указав Имя-тип
    GiantMob(@NotNull Location playerLocation, String mobName) {

        //Определяем направление игрока и спавним моба перед ним, повернутым к игроку лицом
        Vector playerDirection = playerLocation.getDirection();
        playerDirection.setY(0);
        playerDirection.normalize();
        this.giantMobLivingEntity = (LivingEntity) playerLocation.getWorld().spawnEntity(playerLocation.clone().add(playerDirection.clone().multiply(5)).setDirection(playerDirection.multiply(-1)), EntityType.GIANT);
        if (mobName != null) {
            this.giantMobLivingEntity.setCustomName(mobName);
        }
        this.giantMobLivingEntity.setRemoveWhenFarAway(false);
        this.thisGiantMobUUID = giantMobLivingEntity.getUniqueId();
        this.giantMobLivingEntity.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));

        //Заставляем бегать и стрелять
        this.turnOnGiantMobAi();
    }

    //Добавляем существующего
    GiantMob(@NotNull LivingEntity giantMobLivingEntity) {
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
    //Поиск ближайшей цели ( приоритете - игроки)
    private LivingEntity findGiantMobTarget() {
        //получаем список ближайших врагов dв радиусе sralinShootingRadius
        double sralinShootingRadius = GIANT_MOBS_TRACKING_RANGE;
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

            if (e instanceof LivingEntity livingEntity) {
                //Позиции псевдоглаз вокруг головы с каждой стороны
                Location rtGiantMobPseudoEyesPos1 = giantMobLivingEntity.getLocation().clone().add(2, 11, 0);
                Location rtGiantMobPseudoEyesPos2 = giantMobLivingEntity.getLocation().clone().add(-2, 11, 0);
                Location rtGiantMobPseudoEyesPos3 = giantMobLivingEntity.getLocation().clone().add(0, 11, 2);
                Location rtGiantMobPseudoEyesPos4 = giantMobLivingEntity.getLocation().clone().add(0, 11, -2);

                //Пускаем лучи из каждой точки псевдоглаз до верха и низа каждой сущности
                rtRes1 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos1, genVec(rtGiantMobPseudoEyesPos1, (livingEntity).getEyeLocation()), rtGiantMobPseudoEyesPos1.distance((livingEntity).getEyeLocation()), FluidCollisionMode.NEVER, true);
                rtRes2 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos2, genVec(rtGiantMobPseudoEyesPos2, (livingEntity).getEyeLocation()), rtGiantMobPseudoEyesPos2.distance((livingEntity).getEyeLocation()), FluidCollisionMode.NEVER, true);
                rtRes3 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos3, genVec(rtGiantMobPseudoEyesPos3, (livingEntity).getEyeLocation()), rtGiantMobPseudoEyesPos3.distance((livingEntity).getEyeLocation()), FluidCollisionMode.NEVER, true);
                rtRes4 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos4, genVec(rtGiantMobPseudoEyesPos4, (livingEntity).getEyeLocation()), rtGiantMobPseudoEyesPos4.distance((livingEntity).getEyeLocation()), FluidCollisionMode.NEVER, true);
                rtRes5 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos1, genVec(rtGiantMobPseudoEyesPos1, (livingEntity).getLocation()), rtGiantMobPseudoEyesPos1.distance((livingEntity).getLocation()), FluidCollisionMode.NEVER, true);
                rtRes6 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos2, genVec(rtGiantMobPseudoEyesPos2, (livingEntity).getLocation()), rtGiantMobPseudoEyesPos2.distance((livingEntity).getLocation()), FluidCollisionMode.NEVER, true);
                rtRes7 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos3, genVec(rtGiantMobPseudoEyesPos3, (livingEntity).getLocation()), rtGiantMobPseudoEyesPos3.distance((livingEntity).getLocation()), FluidCollisionMode.NEVER, true);
                rtRes8 = giantMobLivingEntity.getWorld().rayTraceBlocks(rtGiantMobPseudoEyesPos4, genVec(rtGiantMobPseudoEyesPos4, (livingEntity).getLocation()), rtGiantMobPseudoEyesPos4.distance((livingEntity).getLocation()), FluidCollisionMode.NEVER, true);

                //Если Сталин может из любой позиции поврота голвоы увидеть верх или низ цели, то эта цель вносится в список кандидатов
                if ((rtRes1 == null) || (rtRes2 == null) || (rtRes3 == null) || (rtRes4 == null) || (rtRes5 == null) || (rtRes6 == null) || (rtRes7 == null) || (rtRes8 == null)) {
                    if ((e instanceof Player player) && (player.getGameMode() != GameMode.SPECTATOR)) {
                        listOfNearbyPlayers.add(livingEntity);
                    } else if (!(e instanceof Player)) {
                        listOfNearbyLivingEntities.add(livingEntity);
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
            if ((getPlayerExact(thisGiantMobPlayerCurrentTargetName) != null) && (!(getPlayerExact(thisGiantMobPlayerCurrentTargetName).isDead()))&& (getPlayerExact(thisGiantMobPlayerCurrentTargetName).getWorld() == giantMobLivingEntity.getWorld()) ) {

                //Если не подошло время забыть о нем и он не стал спектэйтором, фокусим моба на него
                if ((stepsAfterHiding <= TIME_BEFORE_THIS_GIANT_MOB_FORGET_HIS_TARGET * 2) && (!(getPlayerExact(thisGiantMobPlayerCurrentTargetName).getGameMode()==GameMode.SPECTATOR)) ){
                    target = getPlayerExact(thisGiantMobPlayerCurrentTargetName);
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


                    //Поворачиваем по вертикальной оси Y (вертикальный поворот головы конфликтует с дефолтной системой позиционирования)
                    double xDiff = target.getEyeLocation().getX() - thisGiantMobLivingEntity.getEyeLocation().getX();
                    double yDiff = target.getEyeLocation().getY() - thisGiantMobLivingEntity.getEyeLocation().getY();
                    double zDiff = target.getEyeLocation().getZ() - thisGiantMobLivingEntity.getEyeLocation().getZ();
                    double distanceXZ = sqrt(xDiff * xDiff + zDiff * zDiff);
                    double distanceY = sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

                    float newYaw = (float) Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90; //Новый угол рысканья (панорамирование)
                    float newPitch = (float) Math.toDegrees(Math.acos(yDiff / distanceY)) - 90; //Новый угол тангажа (вертикальный наклон головы)

                    thisGiantMobLivingEntity.setRotation(newYaw, newPitch);


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
                    if ((thisGiantMobLocXZ.distance(targetLocXZ) > 1) || ( ((targetY - thisGiantMobLocY) > 20) && (thisGiantMobLocY < targetY)) ) {
                        double oldX = thisGiantMobLoc.getX();
                        double oldZ = thisGiantMobLoc.getZ();
                        //Стандартное движение к цели, если она за радиусом досягаемости
                        //Если моб не под водой - даем ему вдохнуть полной грудью и прижимаем вниз, чтобы не улетал слишком высоко вверх после прыжков
                        if (!(thisGiantMobLivingEntity.getEyeLocation().clone().add(0,1,0).getBlock().getType()==Material.WATER)) {
                            thisGiantMobLivingEntity.setRemainingAir(thisGiantMobLivingEntity.getMaximumAir());
                            thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().setY(-4.5));
                            //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ШАГ ВПЕРЕД");
                        } else {
                            //Если моб под водой - помогаем ему вынырнуть, толкая его вверх
                            thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().setY(1.4));
                            //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ПОПЫТКА ВСПЛЫТЬ");
                            return;
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                if ( (thisGiantMobLivingEntity.isDead()) || (!(DonationExecutor.isRunning())) ) {
                                    this.cancel();
                                    return;
                                }

                                //Если моб не сдвинулся за счет стандартного движения (разница между новыми и старыми кордами<2),
                                // и при этом он находится ниже цели по Y, но дальше по XZ, чем на 2.5 блока от нее (то есть ему есть
                                // куда стремиться до цели)...
                                if ((Math.abs(thisGiantMobLivingEntity.getLocation().getX() - oldX) < 1)
                                        && (Math.abs(thisGiantMobLivingEntity.getLocation().getZ() - oldZ) < 1)
                                        && ((thisGiantMobLocY < targetY) || (thisGiantMobLocXZ.distance(targetLocXZ) > 2.5))) {

                                    thisGiantMob.timesThisGiantMobIsOnOnePlace++; //+1 к счетчику итераций, в течение которых моб остается на одном и том же месте
                                    //Если моб находится на одном и том же месте больше, заставляем его подпрыгивать...
                                    //Если он на одном и том же месте 2 или менее итераций подряд, то сначала пробуем невысокий прыжок...
                                    if (thisGiantMob.timesThisGiantMobIsOnOnePlace <= 2) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().multiply(1).setY(1));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ОБЫЧНЫЙ ПРЫЖОК");
                                        //Если моб остается на одном и том же месте 3 и более итераций подряд, начинаем высокие прыжки, в том числе и в разные стороны
                                    } else if (timesThisGiantMobIsOnOnePlace == 3) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().multiply(1).setY(4));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК");
                                    } else if (timesThisGiantMobIsOnOnePlace == 6) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(0.9).multiply(2).setY(5));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВПРАВО");
                                    } else if (timesThisGiantMobIsOnOnePlace == 9) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(-0.9).multiply(2).setY(5));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВЛЕВО");
                                    } else if (timesThisGiantMobIsOnOnePlace == 10) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(-0.9).multiply(2).setY(5));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВЛЕВО");
                                    } else if (timesThisGiantMobIsOnOnePlace == 13) {
                                        thisGiantMobLivingEntity.setVelocity(thisGiantMobLoc.getDirection().clone().normalize().rotateAroundY(0.9).multiply(2).setY(5));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВПРАВО");
                                        thisGiantMob.timesThisGiantMobIsOnOnePlace = 0;
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
                for (int i = 0; i <= NUMBER_OF_SNOWBALLS_AT_ONE_LAUNCH; i++) {
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
                                }.runTaskTimer(DonationExecutor.getInstance(), finali * TICKS_BETWEEN_SNOWBALLS_SHOTS, 1);
                            }

                        }
                    }.runTaskLater(DonationExecutor.getInstance(), i * TICKS_BETWEEN_SNOWBALLS_SHOTS);
                }


            }
        }.runTaskTimer(DonationExecutor.getInstance(), 0, 150);
    }
}
