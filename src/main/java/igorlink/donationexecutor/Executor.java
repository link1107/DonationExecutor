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
            "KillStalins", "TakeOffBlock"));



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

        Location streamerPlayerLocation = streamerPlayer.getLocation();
        World world = streamerPlayer.getWorld();
        Vector direction = streamerPlayerLocation.getDirection();


        switch (executionName) {
            case "SetDay" -> setDay(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "SetNight" -> setNight(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveIronSet" -> giveIronSet(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveIronSword" -> giveIronSword(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveDiamondSet" -> giveDiamondSet(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveDiamondSword" -> giveDiamondSword(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "SpawnTamedDog" -> spawnTamedDog(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "SpawnTamedCat" -> spawnTamedCat(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "HealPlayer" -> healPlayer(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveIronKirka" -> giveIronKirka(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "GiveDiamondKirka" -> giveDiamondKirka(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "KillStalins" -> killStalins(streamerPlayer, validDonationUsername,fullDonationAmount);
            case "TakeOffBlock" -> takeOffBlock(streamerPlayer, validDonationUsername,fullDonationAmount);
        }

    }




    public static void shitToInventory (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "насрал тебе в инвентарь", "насрал в инвентарь", player, donationAmount, true);
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

    public static void dropActiveItem (Player player, String donationUsername, String donationAmount) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, "безуспешно пытался выбить у тебя предмет из рук", "безуспешно пытался выбить предмет из рук", player, donationAmount, true);
        } else {
            announce(donationUsername, "выбил у тебя предмет из рук", "выбил предмет из рук", player, donationAmount, true);
            player.dropItem(true);
            player.updateInventory();
        }
    }

    public static void lesch (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе леща", "дал леща", player, donationAmount, true);
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

    public static void nekoglai (Player player, String donationUsername, String donationAmount) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity sheep;
        announce(donationUsername, "призвал НЕКОГЛАЯ", "призвал Некоглая", player, donationAmount, true);
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

    public static void killStalins (Player player, String donationUsername, String donationAmount) {
        //Remove Last Death Dropped Items
        announce(donationUsername, "убил Сталиных вокруг тебя", "убил Сталиных вокруг", player, donationAmount, true);
        List<Entity> stalins = player.getNearbyEntities(200, 200, 200);
        for (Entity e: stalins) {
            if (e instanceof Giant) {
                ((LivingEntity) e).damage(1000, player);
            }
        }
    }

    public static void powerKick (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе смачного пинка под зад", "дал смачного пинка под зад", player, donationAmount, true);
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

    public static void clearLastDeathDrop (Player player, String donationUsername, String donationAmount) {
        //Remove Last Death Dropped Items
        if (DonationExecutor.getInstance().streamerPlayersManager.getStreamerPlayer(player.getName()).removeDeathDrop()) {
            announce(donationUsername, "уничтожил твой посмертный дроп", "уничтожил посмертный дроп", player, donationAmount, true);
        } else {
            announce(donationUsername, "безуспешно пытался уничтожить твой посмертный дроп...", "безуспешно пытался уничтожить посмертный дроп", player, donationAmount, true);
        }
    }

    public static void spawnCreeper (Player player, String donationUsername, String donationAmount) {
        //Spawn Creepers
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, "прислал тебе в подарок крипера", "прислал крипера в подарок", player, donationAmount, true);
        direction.setY(0);
        direction.normalize();
        player.getWorld().spawnEntity(player.getLocation().clone().subtract(direction.multiply(1)), EntityType.CREEPER);

    }

    public static void giveDiamonds (Player player, String donationUsername, String donationAmount) {
        //Give some diamonds to the player
        announce(donationUsername, "насыпал тебе §bАЛМАЗОВ", "насыпал §bАлмазов§f", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND, MainConfig.getDiamondsAmount(), donationUsername, "§bАлмазы");
    }

    public static void giveStackOfDiamonds (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "насыпал тебе КУЧУ §bАЛМАЗОВ!", "насыпал §bАлмазов§f", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND, 64, donationUsername, "§bАлмазы");
    }

    public static void giveBread (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе §6Советского Хлеба", "дал §6Советского §6Хлеба§f", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.BREAD, MainConfig.getBreadAmount(), donationUsername, "§6Советский Хлеб");
    }

    public static void callNKVD (Player player, String donationUsername, String donationAmount) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity nkvdMob;
        announce(donationUsername, "хочет отправить тебя в ГУЛАГ", "хочет отправить в ГУЛАГ", player, donationAmount, true);
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

    public static void callStalin (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "призвал Сталина разобраться с тобой", "призвал Сталина разобраться с", player, donationAmount, true);
        DonationExecutor.giantMobManager.addMob(player.getLocation(), "§cИосиф Сталин");
    }

    public static void randomChange (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "подменил тебе кое-что на камни", "призвал Сталина разобраться с", player, donationAmount, true);
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

        StringBuilder replacedItemsBuilder = new StringBuilder();
        int replacedCounter = 0;
        for (int i = 0; i <= 4; i++) {
            if (player.getInventory().getItem(randoms[i]) != null) {
                replacedCounter++;
                if (replacedCounter > 1) {
                    replacedItemsBuilder.append("§f, ");
                }
                replacedItemsBuilder
                        .append("§b")
                        .append(player.getInventory().getItem(randoms[i]).getAmount())
                        .append(" §f")
                        .append(player.getInventory().getItem(randoms[i]).getI18NDisplayName());
                }
                replacedItems.append("§b").append(Objects.requireNonNull(player.getInventory().getItem(randoms[i])).getAmount()).append(" §f").append(Objects.requireNonNull(player.getInventory().getItem(randoms[i])).getI18NDisplayName());
            }
            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player,"§cТебе повезло: все камни попали в пустые слоты!");
        } else {
            sendSysMsgToPlayer(player,"§cБыли заменены следующие предметусы: §f" + replacedItemsBuilder.toString());
        }
    }

    public static void halfHeart (Player player, String donationUsername, String donationAmount) {
        player.setHealth(1);
        announce(donationUsername, "оставил тебе лишь полсердечка", "оставил лишь полсердечка", player, donationAmount, true);
    }

    public static void tamedBecomesEnemies (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "настроил твоих питомцев против тебя", "настроил прирученных питомцев против", player, donationAmount, true);
        for (Entity e : player.getWorld().getEntitiesByClasses(Wolf.class, Cat.class)) {
            if (((Tameable) e).isTamed() && ((Tameable) e).getOwner().getName().equals(player.getName())) {
                if (e instanceof Cat cat) {
                    cat.setOwner(null);
                    cat.setSitting(false);
                    cat.setTarget(player);
                    player.sendMessage("+");
                } else {
                    ((Wolf) e).setSitting(false);
                    ((Tameable) e).setOwner(null);
                    ((Wolf) e).setTarget(player);
                }
            }
        }
    }

    public static void bigBoom (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "сейчас тебя РАЗНЕСЕТ В КЛОЧЬЯ", "сейчас РАЗНЕСЕТ В КЛОЧЬЯ", player, donationAmount, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.getBigBoomRadius(), true);

    }

    public static void setNight (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "включил на сервере ночь", "включил ночь ради", player, donationAmount, true);
        player.getWorld().setTime(18000);
    }

    public static void setDay (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "включил на сервере день", "включил день ради", player, donationAmount, true);
        player.getWorld().setTime(6000);
    }

    public static void giveIronSet (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе железную броню", "дал железную броню", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.IRON_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.IRON_LEGGINGS, 1, donationUsername);
    }

    public static void giveIronSword (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе железный меч", "дал железный меч", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.IRON_SWORD, 1, donationUsername);
    }

    public static void giveIronKirka (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе железную кирку", "дал железную кирку", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.IRON_PICKAXE, 1, donationUsername);
    }

    public static void giveDiamondKirka (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе алмазную кирку", "дал алмазную кирку", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_PICKAXE, 1, donationUsername);
    }

    public static void takeOffBlock (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "убрал блок у тебя из-пол ног", "убрал блок из-под ног", player, donationAmount, true);
        player.getWorld().getBlockAt(player.getLocation().clone().subtract(0,1,0)).setType(Material.AIR);
        player.getWorld().getBlockAt(player.getLocation().clone().subtract(1,1,0)).setType(Material.AIR);
        player.getWorld().getBlockAt(player.getLocation().clone().subtract(0,1,1)).setType(Material.AIR);
        player.getWorld().getBlockAt(player.getLocation().clone().subtract(-1,1,0)).setType(Material.AIR);
        player.getWorld().getBlockAt(player.getLocation().clone().subtract(0,1,-1)).setType(Material.AIR);
    }

    public static void giveDiamondSet (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе алмазную броню", "дал алмазную броню", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_HELMET, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_BOOTS, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_CHESTPLATE, 1, donationUsername);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_LEGGINGS, 1, donationUsername);
    }

    public static void giveDiamondSword (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "дал тебе алмазный меч", "дал алмазный меч", player, donationAmount, true);
        ExecUtils.giveToPlayer(player, Material.DIAMOND_SWORD, 1, donationUsername);
    }

    public static void spawnTamedDog (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "подарил тебе дружка", "подарил щенка", player, donationAmount, true);
        Entity wolf = player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
        ((Wolf) wolf).setTamed(true);
        ((Wolf) wolf).setOwner(player);
        ((Wolf) wolf).setRemoveWhenFarAway(false);
        wolf.setCustomName(donationUsername);
    }

    public static void spawnTamedCat (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "подарил тебе котейку", "подарил котейку", player, donationAmount, true);
        Entity cat = player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);
        ((Cat) cat).setTamed(true);
        ((Cat) cat).setOwner(player);
        ((Cat) cat).setRemoveWhenFarAway(false);
        cat.setCustomName(donationUsername);
    }

    public static void healPlayer (Player player, String donationUsername, String donationAmount) {
        announce(donationUsername, "полностью вас вылечил", "полностью вылечил", player, donationAmount, true);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

}
