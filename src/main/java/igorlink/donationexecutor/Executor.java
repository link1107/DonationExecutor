package igorlink.donationexecutor;

import igorlink.donationexecutor.executionsstaff.ExecUtils;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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
    public static List<String> executionsNamesList = new ArrayList<>(Arrays.asList("ShitToInventory", "Lesch", "DropActiveItem",
            "PowerKick", "ClearLastDeathDrop", "SpawnCreeper", "GiveDiamonds", "GiveStackOfDiamonds", "GiveBread",
            "CallNKVD", "CallStalin", "RandomChange", "TamedBecomesEnemies", "HalfHeart", "BigBoom", "Nekoglai", "SetNight", "SetDay", "GiveIronSet",
            "GiveIronSword", "GiveDiamondSet", "GiveDiamondSword", "SpawnTamedDog", "SpawnTamedCat", "HealPlayer", "GiveIronKirka", "GiveDiamondKirka",
            "KillStalins"));



    public static void DoExecute(String streamerName, String donationUsername, String fullDonationAmount, String executionName) {

        Player streamerPlayer = getPlayerExact(streamerName);
        boolean canContinue = true;
        //Определяем игрока (если он оффлайн - не выполняем донат и пишем об этом в консоль), а также определяем мир, местоположение и направление игрока
        if (streamerPlayer == null) {
            canContinue = false;
        } else if (streamerPlayer.isDead()) {
            canContinue = false;
        }

        //Если имя донатера не указано - устанавливаем в качестве имени "Кто-то"
        String validDonationUsername;
        if (donationUsername.equals("")) {
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


        switch (executionName) {
            case "ShitToInventory" -> shitToInventory(streamerPlayer, validDonationUsername);
            case "Lesch" -> lesch(streamerPlayer, validDonationUsername);
            case "DropActiveItem" -> dropActiveItem(streamerPlayer, validDonationUsername);
            case "PowerKick" -> powerKick(streamerPlayer, validDonationUsername);
            case "ClearLastDeathDrop" -> clearLastDeathDrop(streamerPlayer, validDonationUsername);
            case "SpawnCreeper" -> spawnCreeper(streamerPlayer, validDonationUsername);
            case "GiveDiamonds" -> giveDiamonds(streamerPlayer, validDonationUsername);
            case "GiveStackOfDiamonds" -> giveStackOfDiamonds(streamerPlayer, validDonationUsername);
            case "GiveBread" -> giveBread(streamerPlayer, validDonationUsername);
            case "CallNKVD" -> callNKVD(streamerPlayer, validDonationUsername);
            case "CallStalin" -> callStalin(streamerPlayer, validDonationUsername);
            case "RandomChange" -> randomChange(streamerPlayer, validDonationUsername);
            case "TamedBecomesEnemies" -> tamedBecomesEnemies(streamerPlayer, validDonationUsername);
            case "HalfHeart" -> halfHeart(streamerPlayer, validDonationUsername);
            case "BigBoom" -> bigBoom(streamerPlayer, validDonationUsername);
            case "Nekoglai" -> nekoglai(streamerPlayer, validDonationUsername);
            case "SetDay" -> setDay(streamerPlayer, validDonationUsername);
            case "SetNight" -> setNight(streamerPlayer, validDonationUsername);
            case "GiveIronSet" -> giveIronSet(streamerPlayer, validDonationUsername);
            case "GiveIronSword" -> giveIronSword(streamerPlayer, validDonationUsername);
            case "GiveDiamondSet" -> giveDiamondSet(streamerPlayer, validDonationUsername);
            case "GiveDiamondSword" -> giveDiamondSword(streamerPlayer, validDonationUsername);
            case "SpawnTamedDog" -> spawnTamedDog(streamerPlayer, validDonationUsername);
            case "SpawnTamedCat" -> spawnTamedCat(streamerPlayer, validDonationUsername);
            case "HealPlayer" -> healPlayer(streamerPlayer, validDonationUsername);
            case "GiveIronKirka" -> giveIronKirka(streamerPlayer, validDonationUsername);
            case "GiveDiamondKirka" -> giveDiamondKirka(streamerPlayer, validDonationUsername);
            case "KillStalins" -> killStalins(streamerPlayer, validDonationUsername);
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

    public static void dropActiveItem (Player player, String donationUsername) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, "безуспешно пытался выбить у тебя предмет из рук", "безуспешно пытался выбить предмет из рук", player, true);
        } else {
            announce(donationUsername, "выбил у тебя предмет из рук", "выбил предмет из рук", player, true);
            player.dropItem(true);
            player.updateInventory();
        }
    }

    public static void lesch (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе леща", "дал леща", player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.3);
        player.setVelocity(direction.multiply(0.8));
        if (player.getHealth()>2.0D) {
            player.setHealth(player.getHealth()-2);
        } else {
            player.setHealth(0);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void nekoglai (Player player, String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity sheep;
        announce(donationUsername, "призвал НЕКОГЛАЯ!", "призвал Некоглая", player, true);
        direction.setY(0);
        direction.normalize();
        Location newloc = player.getLocation().clone();
        Vector newdir = direction.clone().multiply(1.5);
        newloc.add(newdir);
        newloc.setDirection(player.getLocation().getDirection().clone().multiply(-1));
        sheep = (LivingEntity) player.getWorld().spawnEntity(newloc, EntityType.SHEEP);
        sheep.setCustomName("N3koglai");
        ((Sheep) sheep).setSheared(true);
    }

    public static void killStalins (Player player, String donationUsername) {
        //Remove Last Death Dropped Items
        announce(donationUsername, "убил Сталиных вокруг тебя!", "убил Сталиных вокруг", player, true);
        List<Entity> stalins = player.getNearbyEntities(200, 200, 200);
        for (Entity e: stalins) {
            if (e instanceof Giant) {
                ((LivingEntity) e).damage(1000, player);
            }
        }
    }

    public static void powerKick (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе смачного пинка под зад", "дал смачного пинка под зад", player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.5);
        player.setVelocity(direction.multiply(1.66));
        if (player.getHealth()>3.0D) {
            player.setHealth(player.getHealth()-3);
        } else {
            player.setHealth(0);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void clearLastDeathDrop (Player player, String donationUsername) {
        //Remove Last Death Dropped Items
        if (DonationExecutor.getInstance().streamerPlayersManager.getStreamerPlayer(player.getName()).removeDeathDrop()) {
            announce(donationUsername, "уничтожил твой посмертный дроп...", "уничтожил посмертный дроп", player, true);
        } else {
            announce(donationUsername, "безуспешно пытался уничтожить твой посмертный дроп...", "безуспешно пытался уничтожить посмертный дроп", player, true);
        }
    }

    public static void spawnCreeper (Player player, String donationUsername) {
        //Spawn Creepers
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, "прислал тебе в подарок крипера...", "прислал крипера в подарок", player, true);
        direction.setY(0);
        direction.normalize();
        player.getWorld().spawnEntity(player.getLocation().clone().subtract(direction.multiply(1)), EntityType.CREEPER);

    }

    public static void giveDiamonds (Player player, String donationUsername) {
        //Give some diamonds to the player
        announce(donationUsername, "насыпал тебе §bАЛМАЗОВ!", "насыпал §bАлмазов§f", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND, MainConfig.getDiamondsAmount(), donationUsername, "§bАлмазы");
    }

    public static void giveStackOfDiamonds (Player player, String donationUsername) {
        announce(donationUsername, "насыпал тебе КУЧУ §bАЛМАЗОВ!", "насыпал §bАлмазов§f", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND, 64, donationUsername, "§bАлмазы");
    }

    public static void giveBread (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе §6Советского Хлеба!", "дал §6Советского §6Хлеба§f", player, true);
        ExecUtils.giveToPlayer(player, Material.BREAD, MainConfig.getBreadAmount(), donationUsername, "§6Советский Хлеб");
    }

    public static void callNKVD (Player player, String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity nkvdMob;
        announce(donationUsername, "хочет отправить тебя в ГУЛАГ!", "хочет отправить в ГУЛАГ", player, true);
        direction.setY(0);
        direction.normalize();
        for (int i = 1; i <= MainConfig.getAmountOfNKVD(); i++) {
            Location newloc = player.getLocation().clone();
            Vector newdir = direction.clone();
            newdir = newdir.rotateAroundY(1.5708 * i).multiply(2);
            newloc.add(newdir);
            nkvdMob = (LivingEntity) player.getWorld().spawnEntity(newloc, EntityType.ZOMBIE);
            nkvdMob.setCustomName("§cСотрудник НКВД");
            Objects.requireNonNull(nkvdMob.getEquipment()).setItem(EquipmentSlot.HAND, new ItemStack(Material.WOODEN_SWORD));
            if (((Zombie) nkvdMob).isAdult()) {
                Objects.requireNonNull(nkvdMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.28);
            }
        }

    }

    public static void callStalin (Player player, String donationUsername) {
        announce(donationUsername, "призвал Сталина разобраться с тобой!", "призвал Сталина разобраться с", player, true);
        DonationExecutor.giantMobManager.addMob(player.getLocation(), "§cИосиф Сталин");
    }

    public static void randomChange (Player player, String donationUsername) {
        announce(donationUsername, "подменил тебе кое-что на камни...", "призвал Сталина разобраться с", player, true);
        int[] randoms = new int[5];
        for (int i = 0; i <= 4; i++) {

            int temp = 0;
            boolean isUnique = false;
            while (!isUnique) {
                temp = (int) (round(random() * 35));
                isUnique = true;
                int n;
                for (n = 0; n < i; n++) {
                    if (randoms[n] == temp) {
                        isUnique = false;
                        break;
                    }
                }
            }
            randoms[i] = temp;

        }

        StringBuilder replacedItems = new StringBuilder();
        int replacedCounter = 0;
        for (int i = 0; i <= 4; i++) {
            if (!(player.getInventory().getItem(randoms[i]) == null)) {
                replacedCounter++;
                if (replacedCounter > 1) {
                    replacedItems.append("§f, ");
                }
                replacedItems.append("§b").append(Objects.requireNonNull(player.getInventory().getItem(randoms[i])).getAmount()).append(" §f").append(Objects.requireNonNull(player.getInventory().getItem(randoms[i])).getI18NDisplayName());
            }
            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player,"§cТебе повезло: все камни попали в пустые слоты!");
        } else {
            sendSysMsgToPlayer(player,"§cБыли заменены следующие предметусы: §f" + replacedItems);
        }
    }

    public static void halfHeart (Player player, String donationUsername) {
        player.setHealth(1);
        announce(donationUsername, "оставил тебе лишь полсердечка...", "оставил лишь полсердечка", player, true);
    }

    public static void tamedBecomesEnemies (Player player, String donationUsername) {
        announce(donationUsername, "настроил твоих питомцев против тебя!", "настроил прирученных питомцев против", player, true);
        for (Entity e : player.getWorld().getEntitiesByClasses(Wolf.class, Cat.class)) {
            if (((Tameable) e).isTamed() && Objects.equals(Objects.requireNonNull(((Tameable) e).getOwner()).getName(), player.getName())) {
                if (e instanceof Cat) {
                    ((Tameable) e).setOwner(null);
                    ((Cat) e).setSitting(false);
                    ((Cat) e).setTarget(player);
                    player.sendMessage("+");
                } else {
                    ((Wolf) e).setSitting(false);
                    ((Tameable) e).setOwner(null);
                    ((Wolf) e).setTarget(player);
                }
            }
        }
    }

    public static void bigBoom (Player player, String donationUsername) {
        announce(donationUsername, "сейчас тебя РАЗНЕСЕТ В КЛОЧЬЯ!!!", "сейчас РАЗНЕСЕТ В КЛОЧЬЯ", player, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.getBigBoomRadius(), true);

    }

    public static void setNight (Player player, String donationUsername) {
        announce(donationUsername, "включил на сервере ночь!", "включил ночь ради", player, true);
        player.getWorld().setTime(18000);
    }

    public static void setDay (Player player, String donationUsername) {
        announce(donationUsername, "включил на сервере день!", "включил день ради", player, true);
        player.getWorld().setTime(6000);
    }

    public static void giveIronSet (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе железную броню!", "дал железную броню", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_LEGGINGS, 1, donationUsername);
    }

    public static void giveIronSword (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе железный меч!", "дал железный меч", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_SWORD, 1, donationUsername);
    }

    public static void giveIronKirka (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе железную кирку!", "дал железную кирку", player, true);
        ExecUtils.giveToPlayer(player, Material.IRON_AXE, 1, donationUsername);
    }

    public static void giveDiamondKirka (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе алмазную кирку!", "дал алмазную кирку", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_AXE, 1, donationUsername);
    }

    public static void giveDiamondSet (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе алмазную броню!", "дал алмазную броню", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_LEGGINGS, 1, donationUsername);
    }

    public static void giveDiamondSword (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе алмазный меч!", "дал алмазный меч", player, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_SWORD, 1, donationUsername);
    }

    public static void spawnTamedDog (Player player, String donationUsername) {
        announce(donationUsername, "подарил тебе дружка!", "подарил щенка", player, true);
        Entity wolf = player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
        ((Wolf) wolf).setTamed(true);
        ((Wolf) wolf).setOwner(player);
        ((Wolf) wolf).setRemoveWhenFarAway(false);
        wolf.setCustomName(donationUsername);
    }

    public static void spawnTamedCat (Player player, String donationUsername) {
        announce(donationUsername, "подарил тебе котейку!", "подарил котейку", player, true);
        Entity cat = player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);
        ((Cat) cat).setTamed(true);
        ((Cat) cat).setOwner(player);
        ((Cat) cat).setRemoveWhenFarAway(false);
        cat.setCustomName(donationUsername);
    }

    public static void healPlayer (Player player, String donationUsername) {
        announce(donationUsername, "полностью вас вылечил!", "полностью вылечил", player, true);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

}
