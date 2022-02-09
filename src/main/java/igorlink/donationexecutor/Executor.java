package igorlink.donationexecutor;
import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.AbstractExecution;
import igorlink.service.MainConfig;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import java.util.*;
import static igorlink.service.Utils.*;
import static java.lang.Math.*;
import static org.bukkit.Bukkit.getPlayerExact;

public class Executor {
    public static String nameOfStreamerPlayer;
    public static String nameOfSecondStreamerPlayer;
    public static List<String> executionsNamesList = new ArrayList<>(Arrays.asList("ShitToInventory", "Lesch", "DropActiveItem",
            "PowerKick", "ClearLastDeathDrop", "SpawnCreeper", "GiveDiamonds", "GiveStackOfDiamonds", "GiveBread",
            "CallNKVD", "CallStalin", "RandomChange", "TamedBecomesEnemies", "HalfHeart", "BigBoom", "Nekoglai"));



    public static void DoExecute(CommandSender sender, String streamerName, String donationUsername, String fullDonationAmount, String donationMessage, String executionName) {

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
            validDonationUsername = "Донатер";
        } else if (!isBlackListed(donationUsername)){
            validDonationUsername = donationUsername;
        } else {
            validDonationUsername = "Донатер";
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
        }

    }




    public static void shitToInventory (Player player, String donationUsername) {
        announce(donationUsername, "насрал тебе в инвентарь", "насрал в инвентарь", player, true);
        Material itemType = Material.DIRT;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cГОВНО ОТ §f" + donationUsername.toUpperCase());
        meta.setLore(Arrays.asList("§7Это говно ужасно вонюче и занимает много места"));
        itemStack.setItemMeta(meta);

        for (int i = 0; i < MainConfig.dirtAmount; i++) {
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
        announce(donationUsername, "насыпал тебе алмазов!", "насыпал алмазов", player, true);
        Material itemType = Material.DIAMOND;
        ItemStack itemStack = new ItemStack(itemType, MainConfig.diamondsAmount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bАлмаз");
        meta.setLore(Arrays.asList("§7Эти алмазы подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        Item diamonds = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void giveStackOfDiamonds (Player player, String donationUsername) {
        announce(donationUsername, "насыпал тебе алмазов!", "насыпал алмазов", player, true);
        Material itemType = Material.DIAMOND;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bАлмаз");
        meta.setLore(Arrays.asList("§7Эти алмазы подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void giveBread (Player player, String donationUsername) {
        announce(donationUsername, "дал тебе хлеба!", "дал хлеба", player, true);
        Material itemType = Material.BREAD;
        ItemStack itemStack = new ItemStack(itemType, 4);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bХлеб");
        meta.setLore(Arrays.asList("§7Этот хлеб подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        Item bread = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void callNKVD (Player player, String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity nkvdMob;
        announce(donationUsername, "хочет отправить тебя в ГУЛАГ!", "хочет отправить в ГУЛАГ", player, true);
        direction.setY(0);
        direction.normalize();
        for (int i = 1; i <= 1; i++) {
            Location newloc = player.getLocation().clone();
            Vector newdir = direction.clone();
            newdir = newdir.rotateAroundY(1.5708 * i).multiply(2);
            newloc.add(newdir);
            nkvdMob = (LivingEntity) player.getWorld().spawnEntity(newloc, EntityType.ZOMBIE);
            nkvdMob.setCustomName("§cСотрудник НКВД");
            nkvdMob.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
            nkvdMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
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
            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player,"§cТебе повезло: все камни попали в пустые слоты!");
        } else {
            sendSysMsgToPlayer(player,"§cБыли заменены следующие предметусы: §f" + replacedItemsBuilder.toString());
        }
    }

    public static void halfHeart (Player player, String donationUsername) {
        player.setHealth(1);
        announce(donationUsername, "оставил тебе лишь полсердечка...", "оставил лишь полсердечка", player, true);
    }

    public static void tamedBecomesEnemies (Player player, String donationUsername) {
        announce(donationUsername, "настроил твоих питомцев против тебя!", "настроил прирученных питомцев против", player, true);
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

    public static void bigBoom (Player player, String donationUsername) {
        announce(donationUsername, "сейчас тебя РАЗНЕСЕТ В КЛОЧЬЯ!!!", "сейчас РАЗНЕСЕТ В КЛОЧЬЯ", player, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.bigBoomRadius, true);

    }


}
