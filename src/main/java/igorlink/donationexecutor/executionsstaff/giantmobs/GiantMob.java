package igorlink.donationexecutor.executionsstaff.giantmobs;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static igorlink.service.Utils.genVec;
import static java.lang.Math.sqrt;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getPlayerExact;

//Класс гигантского моба
class GiantMob {
    private int timesThisGiantMobIsOnOnePlace = 0;
    private String thisGiantMobPlayerCurrentTargetName = null;
    private int stepsAfterHiding = 0;
    private final LivingEntity giantMobLivingEntity;
    private UUID thisGiantMobUUID = null;

    final private int NUMBER_OF_SNOWBALLS_AT_ONE_LAUNCH = 4;
    final private boolean SnowballsFollowingTarget = false;
    final private int GIANT_MOBS_TRACKING_RANGE = 40;
    final private static int TIME_BEFORE_THIS_GIANT_MOB_FORGET_HIS_TARGET = 4;
    final private static int TICKS_BETWEEN_SNOWBALLS_SHOTS = 7;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Конструктор гигантского моба

    //Создаем моба, заспавнив его и указав Имя-тип
    GiantMob(@NotNull Location playerLocation, @Nullable String mobName) {
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
        Objects.requireNonNull(this.giantMobLivingEntity.getEquipment()).setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));

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
    @NotNull
    public String getName() {
        return this.giantMobLivingEntity.getName();
    }

    //Отдает UUID моба
    @Nullable
    public UUID getUUID() {
        return this.thisGiantMobUUID;
    }

    private static boolean checkNoCollision(
            @NotNull World world,
            @NotNull Location @NotNull [] pseudoEyes,
            @NotNull Location baseLocation
    ) {
        for (Location pseudoEyeLoc : pseudoEyes) {
            RayTraceResult rt = world.rayTraceBlocks(
                    pseudoEyeLoc,
                    genVec(pseudoEyeLoc, baseLocation),
                    pseudoEyeLoc.distance(baseLocation),
                    FluidCollisionMode.NEVER,
                    true
            );

            if (rt == null) {
                return true;
            }
        }

        return false;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Поиск ближайшей цели ( приоритете - игроки)
    @Nullable
    private LivingEntity findGiantMobTarget() {
        Location giantLocation = giantMobLivingEntity.getLocation();
        World world = giantMobLivingEntity.getWorld();

        //получаем список ближайших врагов dв радиусе sralinShootingRadius
        double sralinShootingRadius = GIANT_MOBS_TRACKING_RANGE;
        List<Entity> listOfNearbyEntities = giantMobLivingEntity.getNearbyEntities(sralinShootingRadius, sralinShootingRadius, sralinShootingRadius);
        List<LivingEntity> listOfNearbyPlayers = new ArrayList<>();
        List<LivingEntity> listOfNearbyLivingEntities = new ArrayList<>();

        //Позиции псевдоглаз вокруг головы с каждой стороны
        Location[] rtPseudoEyesPositions = new Location[]{
                giantLocation.clone().add(2, 11, 0),
                giantLocation.clone().add(-2, 11, 0),
                giantLocation.clone().add(0, 11, 2),
                giantLocation.clone().add(0, 11, -2)
        };

        //Пробегаемся и ищем игроков
        for (Entity e : listOfNearbyEntities) {
            if (e instanceof LivingEntity) {
                Location entityLocation = e.getLocation();
                Location entityEyeLocation = ((LivingEntity) e).getEyeLocation();

                // Пускаем лучи из каждой точки псевдоглаз до верха и низа каждой сущности.
                // Если Сталин может из любой позиции поврота голвоы увидеть верх или низ цели, то эта цель вносится в список кандидатов
                if (checkNoCollision(world, rtPseudoEyesPositions, entityLocation) ||
                        checkNoCollision(world, rtPseudoEyesPositions, entityEyeLocation)) {

                    if (e instanceof Player && ((Player) e).getGameMode() != GameMode.SPECTATOR) {
                        listOfNearbyPlayers.add((LivingEntity) e);
                    } else if (!(e instanceof Player)) {
                        listOfNearbyLivingEntities.add((LivingEntity) e);
                    }
                }
            }

        }

        //Создаем переменную будущей цели
        LivingEntity target = null;
        double minDistance = 0;

        if (!(listOfNearbyPlayers.isEmpty())) {
            //Если есть игроки - ищем среди них ближайшего
            for (LivingEntity e : listOfNearbyPlayers) {
                double distToPlayer = giantLocation.distance(e.getLocation());

                if (target == null) {
                    target = e;
                    minDistance = distToPlayer;
                } else if (minDistance > distToPlayer) {
                    target = e;
                    minDistance = distToPlayer;
                }
            }

            //Если новая цель - сбрасываем счетчик забвения после скрытия из области видимости моба, и назначаем цель текущей целью моба
            if (!(target.getName().equals(thisGiantMobPlayerCurrentTargetName))) {
                stepsAfterHiding = 0;
                thisGiantMobPlayerCurrentTargetName = target.getName();
            }

        } else if (!(listOfNearbyLivingEntities.isEmpty())) {
            //Если игроков рядом не было, проверяем все живые сущности
            for (LivingEntity e : listOfNearbyLivingEntities) {
                double distToEntity = giantLocation.distance(e.getLocation());

                if (target == null) {
                    target = e;
                    minDistance = distToEntity;
                } else if (minDistance > distToEntity) {
                    target = e;
                    minDistance = distToEntity;
                }
            }

        }

        if ((!(target instanceof Player)) && thisGiantMobPlayerCurrentTargetName != null) {
            Player currentTarget = getPlayerExact(thisGiantMobPlayerCurrentTargetName);

            //Если прошлая цель-игрок существует, и он не мертв и находится в том же мире, что и наш моб
            if (currentTarget != null && !currentTarget.isDead() && currentTarget.getWorld() == giantMobLivingEntity.getWorld()) {

                //Если не подошло время забыть о нем и он не стал спектэйтором, фокусим моба на него
                if ((stepsAfterHiding <= TIME_BEFORE_THIS_GIANT_MOB_FORGET_HIS_TARGET * 2) && currentTarget.getGameMode() != GameMode.SPECTATOR) {
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
        new BukkitRunnable() {
            @Override
            public void run() {
                LivingEntity giant = giantMobLivingEntity;

                if ((giant.isDead()) || (!(DonationExecutor.isRunning()))) {
                    //Если Сталин уже помер, отрубаем задание
                    this.cancel();
                } else {
                    //Если не помер, находим ближайшую цель (игроки в приоритете)
                    LivingEntity target;
                    target = findGiantMobTarget();

                    //Если цели нет...
                    if (target == null) {
                        //Если моб не в воде, то прижимаем его к Земле, чтобы не улетел в небо на прыжке
                        if (!(giant.getEyeLocation().clone().add(0, 2, 0).getBlock().getType() == Material.WATER)) {
                            giant.setVelocity(giant.getLocation().getDirection().clone().normalize().setY(-4.5));
                        } else {
                            //сли над мобом вода - всплываем
                            giant.setVelocity(giant.getLocation().getDirection().clone().normalize().setY(3));
                        }
                        return;
                    }

                    Location targetEyeLoc = target.getEyeLocation();
                    Location giantEyeLoc = giant.getEyeLocation();

                    //Поворачиваем по вертикальной оси Y (вертикальный поворот головы конфликтует с дефолтной системой позиционирования)
                    double xDiff = targetEyeLoc.getX() - giantEyeLoc.getX();
                    double yDiff = targetEyeLoc.getY() - giantEyeLoc.getY();
                    double zDiff = targetEyeLoc.getZ() - giantEyeLoc.getZ();
                    double distance = sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);

                    float newYaw = (float) Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90; //Новый угол рысканья (панорамирование)
                    float newPitch = (float) Math.toDegrees(Math.acos(yDiff / distance)) - 90; //Новый угол тангажа (вертикальный наклон головы)

                    giant.setRotation(newYaw, newPitch);

                    //Готовимся к совершению движения
                    Location giantLoc = giant.getLocation();
                    Location giantLocXZ = giantLoc.clone();
                    giantLocXZ.setY(0);

                    double thisGiantMobLocY = giantLoc.getY();

                    Location targetLoc = target.getLocation();
                    Location targetLocXZ = targetLoc.clone();
                    targetLocXZ.setY(0);

                    double targetY = targetLoc.getY();

                    //Совершаем движение, если мы далеко от цели, либо если мы сильно под ней
                    if ((giantLocXZ.distance(targetLocXZ) > 1) || (((targetY - thisGiantMobLocY) > 20) && (thisGiantMobLocY < targetY))) {
                        double oldX = giantLoc.getX();
                        double oldZ = giantLoc.getZ();
                        Vector normalizedDirection = giantLoc.getDirection().normalize();

                        //Стандартное движение к цели, если она за радиусом досягаемости
                        //Если моб не под водой - даем ему вдохнуть полной грудью и прижимаем вниз, чтобы не улетал слишком высоко вверх после прыжков
                        if (giantEyeLoc.clone().add(0, 1, 0).getBlock().getType() != Material.WATER) {
                            giant.setRemainingAir(giant.getMaximumAir());
                            giant.setVelocity(normalizedDirection.clone().setY(-4.5));
                            //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ШАГ ВПЕРЕД");
                        } else {
                            //Если моб под водой - помогаем ему вынырнуть, толкая его вверх
                            giant.setVelocity(normalizedDirection.clone().setY(1.4));
                            //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ПОПЫТКА ВСПЛЫТЬ");
                            return;
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if ((giant.isDead()) || (!(DonationExecutor.isRunning()))) {
                                    this.cancel();
                                    return;
                                }

                                //Если моб не сдвинулся за счет стандартного движения (разница между новыми и старыми кордами<2),
                                // и при этом он находится ниже цели по Y, но дальше по XZ, чем на 2.5 блока от нее (то есть ему есть
                                // куда стремиться до цели)...
                                if ((Math.abs(giant.getLocation().getX() - oldX) < 1)
                                        && (Math.abs(giant.getLocation().getZ() - oldZ) < 1)
                                        && ((thisGiantMobLocY < targetY) || (giantLocXZ.distance(targetLocXZ) > 2.5))) {

                                    timesThisGiantMobIsOnOnePlace++; //+1 к счетчику итераций, в течение которых моб остается на одном и том же месте
                                    //Если моб находится на одном и том же месте больше, заставляем его подпрыгивать...
                                    //Если он на одном и том же месте 2 или менее итераций подряд, то сначала пробуем невысокий прыжок...
                                    if (timesThisGiantMobIsOnOnePlace <= 2) {
                                        giant.setVelocity(giantLoc.getDirection().clone().normalize().multiply(1).setY(1));
                                        //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ОБЫЧНЫЙ ПРЫЖОК");
                                        //Если моб остается на одном и том же месте 3 и более итераций подряд, начинаем высокие прыжки, в том числе и в разные стороны
                                    } else {
                                        switch (timesThisGiantMobIsOnOnePlace) {
                                            case 3:
                                                giant.setVelocity(normalizedDirection.clone().setY(4));
                                                //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК");

                                                break;
                                            case 6:
                                                giant.setVelocity(normalizedDirection.clone().rotateAroundY(0.9).multiply(2).setY(5));
                                                //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВПРАВО");

                                                break;
                                            case 9:
                                            case 10:
                                                giant.setVelocity(normalizedDirection.clone().rotateAroundY(-0.9).multiply(2).setY(5));
                                                //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВЛЕВО");

                                                break;
                                            case 13:
                                                giant.setVelocity(normalizedDirection.clone().rotateAroundY(0.9).multiply(2).setY(5));
                                                //sendSysMsgToPlayer(getPlayerExact("Evropejets"), "ВЫСОКИЙ ПРЫЖОК ВПРАВО");
                                                timesThisGiantMobIsOnOnePlace = 0;
                                        }
                                    }

                                } else {
                                    //если он сдвинулся, обнуляем счетчик нахождения на одном месте по XZ
                                    timesThisGiantMobIsOnOnePlace = 0;
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
        new BukkitRunnable() {
            @Override
            public void run() {
                LivingEntity giant = giantMobLivingEntity;

                //Если моб уже не существует, отменяем стрельбу
                if (giant.isDead() || !DonationExecutor.isRunning()) {
                    this.cancel();
                    return;
                }

                //Находим ближайшее ентити (игроки в приоритете), чтобы сделать из него цель для моба
                LivingEntity target = findGiantMobTarget();

                if (target == null) {
                    return;
                }

                //Спавним файер болл на 2 блока ниже глаз (район между рук)
                Location giantEyeLoc = giant.getEyeLocation();
                Location ballLoc = giantEyeLoc.clone()
                        .add(genVec(giantEyeLoc, target.getLocation()).multiply(3.5))
                        .add(0, -2, 0);

                Fireball stalinBall = (Fireball) giant.getWorld().spawnEntity(ballLoc, EntityType.FIREBALL);
                stalinBall.setDirection(genVec(ballLoc, target.getLocation()).multiply(2));
                stalinBall.setMetadata("type", new FixedMetadataValue(DonationExecutor.getInstance(), "giantball"));
            }

        }.runTaskTimer(DonationExecutor.getInstance(), 0, 45);
    }

    private void makeGiantMobAttackWithSnowballs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LivingEntity giant = giantMobLivingEntity;

                //Если моб уже не существует, отменяем стрельбу
                if (giant.isDead() || !DonationExecutor.isRunning()) {
                    this.cancel();

                    return;
                }

                //Находим ближайшее ентити (игроки в приоритете), чтобы сделать из него цель для моба
                LivingEntity target = findGiantMobTarget();
                if (target == null) {
                    return;
                }

                //Запускаем снежки в количестве howManySnowballsMobLaunches
                for (int i = 0; i <= NUMBER_OF_SNOWBALLS_AT_ONE_LAUNCH; i++) {
                    final int finali = i;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (giant.isDead() || !DonationExecutor.isRunning()) {
                                this.cancel();
                                return;
                            }

                            Location handHeightPoint = giant.getLocation().add(0, 7, 0);
                            Snowball snowball = (Snowball) giant.getWorld().spawnEntity(
                                    handHeightPoint.clone().add(handHeightPoint.getDirection().rotateAroundY(0.7).multiply(3.5)),
                                    EntityType.SNOWBALL
                            );
                            ItemStack itemStack = new ItemStack(Material.SNOWBALL, 1);
                            ItemMeta meta = snowball.getItem().getItemMeta();
                            meta.setLore(List.of("Stalinball"));
                            itemStack.setItemMeta(meta);
                            snowball.setItem(itemStack);
                            snowball.setVelocity(genVec(snowball.getLocation(), target.getEyeLocation()).multiply(2.2));
                            snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3, 2);

                            //Каждый тик направляем снежок в игрока
                            if (SnowballsFollowingTarget) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if ((snowball.isDead()) || target.isDead() || target.getWorld() != snowball.getWorld() || !DonationExecutor.isRunning()) {
                                            this.cancel();
                                        } else {
                                            snowball.setVelocity(genVec(snowball.getLocation(), target.getEyeLocation()));
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
