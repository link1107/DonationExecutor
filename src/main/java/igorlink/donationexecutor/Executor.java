package igorlink.donationexecutor;

import org.dark0ghost.annotations.annotations.MagicConst;
import igorlink.service.MainConfig;
import kotlin.Suppress;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
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
import static org.bukkit.Bukkit.getPlayer;
import static org.dark0ghost.annotations.configs.Text.RU.ExecutorText.*;

public class Executor {

    private @MagicConst static final int eventOneHealth = 1;
    private @MagicConst static final int eventZeroHealth = 1;
    private @MagicConst static final double rotateAroundY = 1.5708;

    public @Suppress(names = "UNUSED_PARAMETER")
    static String nameOfStreamerPlayer;
    public @Suppress(names = "UNUSED_PARAMETER")
    static String nameOfSecondStreamerPlayer;
    public static List<String> executionsList = new ArrayList<>(Arrays.asList("ShitToInventory", "Lesch", "DropActiveItem",
            "PowerKick", "ClearLastDeathDrop", "SpawnCreeper", "GiveDiamonds", "GiveStackOfDiamonds", "GiveBread",
            "CallNKVD", "CallStalin", "RandomChange", "TamedBecomesEnemies", "HalfHeart", "BigBoom"));

    public static void DoExecute(CommandSender sender, String streamerName, String donationUsername, String fullDonationAmount, String donationMessage, String executionName) {

        //Если имя донатера не указано - устанавливаем в качестве имени "Кто-то"
        @Suppress(names = "UNUSED_PARAMETER") String _donationUsername;
        if (donationUsername.equals("")) {
            _donationUsername = UNIDENTIFIED_USER_NAME;
        } else {
            _donationUsername = donationUsername;
        }

        boolean canContinue = true;
        //Определяем игрока (если он оффлайн - не выполняем донат и пишем об этом в консоль), а также определяем мир, местоположение и направление игрока
        Player streamerPlayer = getPlayer(streamerName);
        if (streamerPlayer == null) {
            canContinue = false;
        } else if (streamerPlayer.isDead()) {
            canContinue = false;
        }

        if (!canContinue) {
            logToConsole("Донат от §b" + donationUsername + " §f в размере §b" + fullDonationAmount + "§f выполнен из-за того, что целевой стример был недоступен.");
            return;
        }

        Location streamerPlayerLocation = streamerPlayer.getLocation();
        @Suppress(names = "UNUSED_PARAMETER") World world = streamerPlayer.getWorld();
        @Suppress(names = "UNUSED_PARAMETER") Vector direction = streamerPlayerLocation.getDirection();

        //streamerPlayer.sendActionBar(donationMessage);

        switch (executionName) {
            case "ShitToInventory" -> shitToInventory(streamerPlayer, donationUsername);
            case "Lesch" -> lesch(streamerPlayer, donationUsername);
            case "DropActiveItem" -> dropActiveItem(streamerPlayer, donationUsername);
            case "PowerKick" -> powerKick(streamerPlayer, donationUsername);
            case "ClearLastDeathDrop" -> clearLastDeathDrop(streamerPlayer, donationUsername);
            case "SpawnCreeper" -> spawnCreeper(streamerPlayer, donationUsername);
            case "GiveDiamonds" -> giveDiamonds(streamerPlayer, donationUsername);
            case "GiveStackOfDiamonds" -> giveStackOfDiamonds(streamerPlayer, donationUsername);
            case "GiveBread" -> giveBread(streamerPlayer, donationUsername);
            case "CallNKVD" -> callNKVD(streamerPlayer, donationUsername);
            case "CallStalin" -> callStalin(streamerPlayer, donationUsername);
            case "RandomChange" -> randomChange(streamerPlayer, donationUsername);
            case "TamedBecomesEnemies" -> tamedBecomesEnemies(streamerPlayer, donationUsername);
            case "HalfHeart" -> halfHeart(streamerPlayer, donationUsername);
            case "BigBoom" -> bigBoom(streamerPlayer, donationUsername);
        }
    }

    public static void shitToInventory(Player player, String donationUsername) {
        announce(donationUsername, SHIT_INVENTORY_EVENT_TEXT, SHIT_INVENTORY_EVENT_TEXT, player, true);
        Material itemType = Material.DIRT;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§cГОВНО ОТ §f" + donationUsername.toUpperCase());
        meta.setLore(List.of(SHIT_INVENTORY_LORE));
        itemStack.setItemMeta(meta);

        for (int i = 0; i < MainConfig.dirtAmount; i++) {
            player.getInventory().addItem(itemStack);
        }
    }

    public static void dropActiveItem(Player player, String donationUsername) {
        if (player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
            announce(donationUsername, FAIL_DROP_EVENT_TEXT, FAIL_DROP_EVENT_TEXT, player, true);
            return;
        }
        announce(donationUsername, SUCCESS_DROP_EVENT_TEXT, SUCCESS_DROP_EVENT_TEXT, player, true);
        player.dropItem(true);
        player.updateInventory();
    }

    public static void lesch(Player player, String donationUsername) {
        announce(donationUsername, LESH_EVENT_TEXT, LESH_EVENT_TEXT, player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.3);
        player.setVelocity(direction.multiply(0.8));
        if (player.getHealth() > 2.0D) {
            player.setHealth(player.getHealth() - 2);
        } else {
            player.setHealth(eventZeroHealth);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void powerKick(Player player, String donationUsername) {
        announce(donationUsername,  KICK_EVENT_TEXT,  KICK_EVENT_TEXT, player, true);
        Vector direction = player.getLocation().getDirection();
        direction.setY(0);
        direction.normalize();
        direction.setY(0.5);
        player.setVelocity(direction.multiply(1.66));
        if (player.getHealth() > 3.0D) {
            player.setHealth(player.getHealth() - 3);
        } else {
            player.setHealth(eventZeroHealth);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

    public static void clearLastDeathDrop(Player player, String donationUsername) {
        //Remove Last Death Dropped Items
        if (DonationExecutor.getInstance().listOfStreamerPlayers.getStreamerPlayer(player.getName()).removeDeathDrop()) {
            announce(donationUsername, SUCCESS_CLEAR_DROP_EVENT_TEXT + SPECIAL_END_TEXT, SUCCESS_CLEAR_DROP_EVENT_TEXT, player, true);
            return;
        }
        announce(donationUsername, FAIL_CLEAR_DROP_EVENT_TEXT + SPECIAL_END_TEXT, FAIL_CLEAR_DROP_EVENT_TEXT, player, true);
    }

    public static void spawnCreeper(Player player, String donationUsername) {
        //Spawn Creepers
        Vector direction = player.getLocation().getDirection();
        announce(donationUsername, SPAWN_CREEPER_EVENT_TEXT + SPECIAL_END_TEXT, SPAWN_CREEPER_EVENT_TEXT, player, true);
        direction.setY(0);
        direction.normalize();
        player.getWorld().spawnEntity(player.getLocation().clone().subtract(direction.multiply(1)), EntityType.CREEPER);
    }

    public static void giveDiamonds(Player player, String donationUsername) {
        //Give some diamonds to the player
        announce(donationUsername, GIVE_DIAMOND_EVENT_TEXT, GIVE_DIAMOND_EVENT_TEXT, player, true);
        Material itemType = Material.DIAMOND;
        ItemStack itemStack = new ItemStack(itemType, MainConfig.diamondsAmount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bАлмаз");
        meta.setLore(List.of("§7Эти алмазы подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        @Suppress(names = "UNUSED_PARAMETER") Item diamonds = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void giveStackOfDiamonds(Player player, String donationUsername) {
        announce(donationUsername, GIVE_DIAMOND_EVENT_TEXT, GIVE_DIAMOND_EVENT_TEXT, player, true);
        Material itemType = Material.DIAMOND;
        ItemStack itemStack = new ItemStack(itemType, 64);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bАлмаз");
        meta.setLore(List.of("§7Эти алмазы подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        @Suppress(names = "UNUSED_PARAMETER") Item diamonds = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void giveBread(Player player, String donationUsername) {
        announce(donationUsername, BREAD_EVENT_SUBTEXT, BREAD_EVENT_ALERT_TEXT, player, true);
        Material itemType = Material.BREAD;
        ItemStack itemStack = new ItemStack(itemType, 4);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bХлеб");
        meta.setLore(List.of("§7Этот хлеб подарил §f" + donationUsername));
        itemStack.setItemMeta(meta);
        @Suppress(names = "UNUSED_PARAMETER") Item bread = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static void callNKVD(Player player, String donationUsername) {
        Vector direction = player.getLocation().getDirection();
        LivingEntity nkvdMob;
        announce(donationUsername, NKVD_EVENT_TEXT, NKVD_EVENT_TEXT, player, true);
        direction.setY(0);
        direction.normalize();
        for (int i = 1; i <= 3; i++) {
            Location newloc = player.getLocation().clone();
            Vector newdir = direction.clone();
            newdir = newdir.rotateAroundY(rotateAroundY * i).multiply(2);
            newloc.add(newdir);
            nkvdMob = (LivingEntity) player.getWorld().spawnEntity(newloc, EntityType.ZOMBIE);
            nkvdMob.setCustomName("§cСотрудник НКВД");
            Objects.requireNonNull(nkvdMob.getEquipment()).setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
            Objects.requireNonNull(nkvdMob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.3);
        }

    }

    public static void callStalin(Player player, String donationUsername) {
        announce(donationUsername, STALING_EVENT_TEXT + "тобой!" , STALING_EVENT_TEXT, player, true);
        DonationExecutor.giantMobManager.addMob(player.getLocation(), "§cИосиф Сталин");
    }

    public static void randomChange(Player player, String donationUsername) {
        announce(donationUsername, RANDOM_EVENT_TEXT, STALING_EVENT_TEXT, player, true);
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
                replacedItems
                        .append("§b")
                        .append(
                                Objects
                                        .requireNonNull(
                                                player
                                                        .getInventory()
                                                        .getItem(randoms[i])
                                        )
                                        .getAmount())
                        .append(" §f")
                        .append(
                                Objects
                                        .requireNonNull(
                                                player
                                                        .getInventory()
                                                        .getItem(randoms[i])
                                        )
                                        .getI18NDisplayName());
            }
            player.getInventory().setItem(randoms[i], new ItemStack(Material.STONE, 1));
        }

        if (replacedCounter == 0) {
            sendSysMsgToPlayer(player, "§cТебе повезло: все камни попали в пустые слоты!");
            return;
        }
        sendSysMsgToPlayer(player, "§cБыли заменены следующие предметусы: §f" + replacedItems);
    }

    public static void halfHeart(Player player, String donationUsername) {
        player.setHealth(eventOneHealth);
        announce(donationUsername, HALF_HEART_EVENT_TEXT + SPECIAL_END_TEXT, HALF_HEART_EVENT_TEXT, player, true);
    }

    public static void tamedBecomesEnemies(Player player, String donationUsername) {
        announce(donationUsername, ENEMIES_EVENT_TEXT, ENEMIES_EVENT_TEXT, player, true);
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

    public static void bigBoom(Player player, String donationUsername) {
        announce(donationUsername, BOOM_EVENT_TEXT, BOOM_EVENT_TEXT, player, true);
        player.getWorld().createExplosion(player.getLocation(), MainConfig.bigBoomRadius, true);
    }
}
