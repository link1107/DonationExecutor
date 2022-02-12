package igorlink.donationexecutor;

import igorlink.ArrayUtils;
import igorlink.donationexecutor.executionsstaff.ExecUtils;
import igorlink.donationexecutor.playersmanagement.ExecutionType;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static igorlink.service.Utils.*;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.bukkit.Bukkit.getPlayerExact;

public class Executor {
    public static String nameOfStreamerPlayer;
    public static String nameOfSecondStreamerPlayer;

    public static void doExecute(
            @NotNull String streamerName,
            @NotNull String donationUsername,
            @NotNull String fullDonationAmount,
            @NotNull ExecutionType execType
    ) {
        Player streamerPlayer = getPlayerExact(streamerName);
        boolean canContinue = streamerPlayer != null && !streamerPlayer.isDead();
        //Определяем игрока (если он оффлайн - не выполняем донат и пишем об этом в консоль), а также определяем мир, местоположение и направление игрока

        //Если имя донатера не указано - устанавливаем в качестве имени "Кто-то"
        String validDonationUsername;
        if (donationUsername.length() == 0) {
            validDonationUsername = "Аноним";
        } else if (!isBlackListed(donationUsername)){
            validDonationUsername = donationUsername;
        } else {
            validDonationUsername = "Донатер";
            assert streamerPlayer != null;
            Utils.logToConsole("§eникнейм донатера §f" + donationUsername + "§e был скрыт, как подозрительный");
            streamerPlayer.sendActionBar("НИКНЕЙМ ДОНАТЕРА БЫЛ СКРЫТ");
        }

        if (!canContinue) {
            logToConsole("Донат от §b" + donationUsername + " §f в размере §b" + fullDonationAmount + "§f выполнен из-за того, что целевой стример был недоступен.");
            return;
        }

        switch (execType) {
            case ShitToInventory -> shitToInventory(streamerPlayer, validDonationUsername);
            case Lesch -> lesch(streamerPlayer, validDonationUsername);
            case DropActiveItem -> dropActiveItem(streamerPlayer, validDonationUsername);
            case PowerKick -> powerKick(streamerPlayer, validDonationUsername);
            case ClearLastDeathDrop -> clearLastDeathDrop(streamerPlayer, validDonationUsername);
            case SpawnCreeper -> spawnCreeper(streamerPlayer, validDonationUsername);
            case GiveDiamonds -> giveDiamonds(streamerPlayer, validDonationUsername);
            case GiveStackOfDiamonds -> giveStackOfDiamonds(streamerPlayer, validDonationUsername);
            case GiveBread -> giveBread(streamerPlayer, validDonationUsername);
            case CallNKVD -> callNKVD(streamerPlayer, validDonationUsername);
            case CallStalin -> callStalin(streamerPlayer, validDonationUsername);
            case RandomChange -> randomChange(streamerPlayer, validDonationUsername);
            case TamedBecomesEnemies -> tamedBecomesEnemies(streamerPlayer, validDonationUsername);
            case HalfHeart -> halfHeart(streamerPlayer, validDonationUsername);
            case BigBoom -> bigBoom(streamerPlayer, validDonationUsername);
            case Nekoglai -> nekoglai(streamerPlayer, validDonationUsername);
            case SetDay -> setDay(streamerPlayer, validDonationUsername);
            case SetNight -> setNight(streamerPlayer, validDonationUsername);
            case GiveIronSet -> giveIronSet(streamerPlayer, validDonationUsername);
            case GiveIronSword -> giveIronSword(streamerPlayer, validDonationUsername);
            case GiveDiamondSet -> giveDiamondSet(streamerPlayer, validDonationUsername);
            case GiveDiamondSword -> giveDiamondSword(streamerPlayer, validDonationUsername);
            case SpawnTamedDog -> spawnTamedDog(streamerPlayer, validDonationUsername);
            case SpawnTamedCat -> spawnTamedCat(streamerPlayer, validDonationUsername);
            case HealPlayer -> healPlayer(streamerPlayer, validDonationUsername);
            case GiveIronKirka -> giveIronKirka(streamerPlayer, validDonationUsername);
            case GiveDiamondKirka -> giveDiamondKirka(streamerPlayer, validDonationUsername);
            case KillStalins -> killStalins(streamerPlayer, validDonationUsername);
        }
    }

    public static void shitToInventory (Player player, String donationUsername) {
        announce(donationUsername, "насрал тебе в инвентарь", "насрал в инвентарь", player, true);
        Material itemType = Material.DIRT;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cГОВНО ОТ §f" + donationUsername.toUpperCase());
        meta.setLore(List.of("§7Это говно ужасно вонюче и занимает много места"));
        itemStack.setItemMeta(meta);

        for (int i = 0; i < MainConfig.getDirtAmount(); i++) {
            player.getInventory().addItem(itemStack);
        }
    }

    public static void dropActiveItem (@NotNull Player player, @NotNull String donationUsername) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, "безуспешно пытался выбить у тебя предмет из рук", "безуспешно пытался выбить предмет из рук", player, true);
        } else {
            announce(donationUsername, "выбил у тебя предмет из рук", "выбил предмет из рук", player, true);
            player.dropItem(true);
            player.updateInventory();
        }
    }

    public static void lesch (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе леща", "дал леща", player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.3);

        player.setVelocity(direction.multiply(0.8));
        player.setHealth(Math.max(0, player.getHealth() - 2));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void nekoglai (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "призвал НЕКОГЛАЯ!", "призвал Некоглая", player, true);

        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();

        Location newLoc = player.getLocation();
        Vector newDir = direction.clone().multiply(1.5);
        newLoc.add(newDir);
        newLoc.setDirection(player.getLocation().getDirection().multiply(-1));

        Sheep sheep = (Sheep) player.getWorld().spawnEntity(newLoc, EntityType.SHEEP);
        sheep.setCustomName("N3koglai");
        sheep.setSheared(true);
    }

    public static void killStalins (@NotNull Player player, @NotNull String donationUsername) {
        //Remove Last Death Dropped Items
        announce(donationUsername, "убил Сталиных вокруг тебя!", "убил Сталиных вокруг", player, true);
        List<Entity> stalins = player.getNearbyEntities(200, 200, 200);
        for (Entity e: stalins) {
            if (e instanceof Giant) {
                ((LivingEntity) e).damage(1000, player);
            }
        }
    }

    public static void powerKick (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе смачного пинка под зад", "дал смачного пинка под зад", player, true);

        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.5);
        player.setVelocity(direction.multiply(1.66));
        player.setHealth(Math.max(0, player.getHealth() - 3));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void clearLastDeathDrop (@NotNull Player player, @NotNull String donationUsername) {
        //Remove Last Death Dropped Items
        if (DonationExecutor.getInstance().streamerPlayersManager.getStreamerPlayer(player.getName()).removeDeathDrop()) {
            announce(donationUsername, "уничтожил твой посмертный дроп...", "уничтожил посмертный дроп", player, true);
        } else {
            announce(donationUsername, "безуспешно пытался уничтожить твой посмертный дроп...", "безуспешно пытался уничтожить посмертный дроп", player, true);
        }
    }

    public static void spawnCreeper (@NotNull Player player, @NotNull String donationUsername) {
        //Spawn Creepers
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, "прислал тебе в подарок крипера...", "прислал крипера в подарок", player, true);
        direction.setY(0);
        direction.normalize();
        player.getWorld().spawnEntity(player.getLocation().subtract(direction), EntityType.CREEPER);
    }

    public static void giveDiamonds (@NotNull Player player, @NotNull String donationUsername) {
        //Give some diamonds to the player
        announce(donationUsername, "насыпал тебе §bАЛМАЗОВ!", "насыпал §bАлмазов§f", player, true);

        ExecUtils.giveToPlayer(player, Material.DIAMOND, MainConfig.getDiamondsAmount(), donationUsername, "§bАлмазы");
    }

    public static void giveStackOfDiamonds (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "насыпал тебе КУЧУ §bАЛМАЗОВ!", "насыпал §bАлмазов§f", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND, 64, donationUsername, "§bАлмазы");
    }

    public static void giveBread (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе §6Советского Хлеба!", "дал §6Советского §6Хлеба§f", player, true);
        ExecUtils.giveToPlayer(player, Material.BREAD, MainConfig.getBreadAmount(), donationUsername, "§6Советский Хлеб");
    }

    public static void callNKVD (@NotNull Player player, @NotNull String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, "хочет отправить тебя в ГУЛАГ!", "хочет отправить в ГУЛАГ", player, true);
        direction.setY(0);
        direction.normalize();

        Location newLoc = player.getLocation();
        World world = player.getWorld();

        for (int i = 1; i <= MainConfig.getAmountOfNKVD(); i++) {
            Vector newDir = direction.clone();
            newDir = newDir.rotateAroundY(1.5708 * i).multiply(2);
            newLoc.add(newDir);

            Zombie nkvdMob = (Zombie) world.spawnEntity(newLoc, EntityType.ZOMBIE);
            nkvdMob.setCustomName("§cСотрудник НКВД");
            Objects.requireNonNull(nkvdMob.getEquipment()).setItem(EquipmentSlot.HAND, new ItemStack(Material.WOODEN_SWORD));

            if (nkvdMob.isAdult()) {
                Objects.requireNonNull(nkvdMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.28);
            }
        }
    }

    public static void callStalin (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "призвал Сталина разобраться с тобой!", "призвал Сталина разобраться с", player, true);
        DonationExecutor.giantMobManager.addMob(player.getLocation(), "§cИосиф Сталин");
    }

    public static void randomChange (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "подменил тебе кое-что на камни...", "призвал Сталина разобраться с", player, true);
        int[] randoms = new int[5];
        for (int i = 0; i < 5; i++) {
            while(true) {
                int number = (int)round(random() * 35);

                if(!ArrayUtils.contains(randoms, i, number)) {
                    randoms[i] = number;
                    break;
                }
            }
        }

        PlayerInventory inventory = player.getInventory();

        StringBuilder replacedItems = new StringBuilder();
        int replacedCounter = 0;

        for (int i = 0; i < 5; i++) {
            ItemStack item = inventory.getItem(randoms[i]);
            if(item != null) {
                replacedCounter++;

                if (replacedCounter > 1) {
                    replacedItems.append("§f, ");
                }

                replacedItems.append("§b")
                        .append(item.getAmount())
                        .append(" §f")
                        .append(item.getI18NDisplayName());
            }

            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player,"§cТебе повезло: все камни попали в пустые слоты!");
        } else {
            sendSysMsgToPlayer(player,"§cБыли заменены следующие предметусы: §f" + replacedItems);
        }
    }

    public static void halfHeart (@NotNull Player player, @NotNull String donationUsername) {
        player.setHealth(1);
        announce(
                donationUsername,
                "оставил тебе лишь полсердечка...",
                "оставил лишь полсердечка",
                player,
                true
        );
    }

    public static void tamedBecomesEnemies (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "настроил твоих питомцев против тебя!", "настроил прирученных питомцев против", player, true);
        for (Entity e : player.getWorld().getEntitiesByClasses(Wolf.class, Cat.class)) {
            Tameable tameable = (Tameable) e;

            // если питомец приручен, тогда у него есть владелец
            //noinspection ConstantConditions
            if(tameable.isTamed() && tameable.getOwner().getName().equals(player.getName())) {
                if (e instanceof Cat cat) {
                    cat.setOwner(null);
                    cat.setSitting(false);
                    cat.setTarget(player);
                    player.sendMessage("+");
                } else {
                    Wolf wolf = (Wolf) e;

                    wolf.setSitting(false);
                    wolf.setOwner(null);
                    wolf.setTarget(player);
                }
            }
        }
    }

    public static void bigBoom (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "сейчас тебя РАЗНЕСЕТ В КЛОЧЬЯ!!!", "сейчас РАЗНЕСЕТ В КЛОЧЬЯ", player, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.getBigBoomRadius(), true);

    }

    public static void setNight (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "включил на сервере ночь!", "включил ночь ради", player, true);
        player.getWorld().setTime(18000);
    }

    public static void setDay (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "включил на сервере день!", "включил день ради", player, true);
        player.getWorld().setTime(6000);
    }

    public static void giveIronSet (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе железную броню!", "дал железную броню", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_LEGGINGS, 1, donationUsername);
    }

    public static void giveIronSword (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе железный меч!", "дал железный меч", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_SWORD, 1, donationUsername);
    }

    public static void giveIronKirka (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе железную кирку!", "дал железную кирку", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_AXE, 1, donationUsername);
    }

    public static void giveDiamondKirka (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе алмазную кирку!", "дал алмазную кирку", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_AXE, 1, donationUsername);
    }

    public static void giveDiamondSet (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе алмазную броню!", "дал алмазную броню", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_LEGGINGS, 1, donationUsername);
    }

    public static void giveDiamondSword (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "дал тебе алмазный меч!", "дал алмазный меч", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_SWORD, 1, donationUsername);
    }

    public static void spawnTamedDog (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "подарил тебе дружка!", "подарил щенка", player, true);

        Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(player);
        wolf.setRemoveWhenFarAway(false);
        wolf.setCustomName(donationUsername);
    }

    public static void spawnTamedCat (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "подарил тебе котейку!", "подарил котейку", player, true);
        Cat cat = (Cat)player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);

        cat.setTamed(true);
        cat.setOwner(player);
        cat.setRemoveWhenFarAway(false);
        cat.setCustomName(donationUsername);
    }

    public static void healPlayer (@NotNull Player player, @NotNull String donationUsername) {
        announce(donationUsername, "полностью вас вылечил!", "полностью вылечил", player, true);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

}
