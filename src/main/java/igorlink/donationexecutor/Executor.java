package igorlink.donationexecutor;

import igorlink.donationexecutor.executionsstaff.Donation;
import igorlink.donationexecutor.executionsstaff.StreamerPlayer;
import igorlink.executions.*;
import igorlink.service.MainConfig;
import igorlink.service.Patterns;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.HashMap;
import java.util.List;

import static igorlink.service.Utils.*;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.bukkit.Bukkit.getPlayer;

public class Executor {
    public static String nameOfStreamerPlayer;
    public static String nameOfSecondStreamerPlayer;
    public static List<String> executionsList = new ArrayList<>(Arrays.asList("ShitToInventory", "Lesch", "DropActiveItem",
            "PowerKick", "ClearLastDeathDrop", "SpawnCreeper", "GiveDiamonds", "GiveStackOfDiamonds", "GiveBread",
            "CallNKVD", "CallStalin", "RandomChange", "TamedBecomesEnemies", "HalfHeart", "BigBoom"));

    // Хешмапа, структура типа: строка/обьект.
    // В строке название "ивента". В обьекте - сам обьект с логикой ивента.
    // Не срём в один метод, а под каждый ивент делаем свой обьект, который будет наследовать интерфейс IExecution.
    public static HashMap<String, IExecution> executionHashMap = new HashMap<>();

    // Регистрируем события.
    static {
        executionHashMap.put("ShitToInventory", new ShitToInventory());
        executionHashMap.put("Lesch", new Lesch());
        executionHashMap.put("DropActiveItem", new DropActiveItem());
        executionHashMap.put("PowerKick", new PowerKick());
        executionHashMap.put("ClearLastDeathDrop", new ClearLastDeathDrop());
        executionHashMap.put("SpawnCreeper", new SpawnCreeper());
        executionHashMap.put("GiveDiamonds", new GiveDiamonds());
        executionHashMap.put("GiveStackOfDiamonds", new GiveStackOfDiamonds());
        executionHashMap.put("GiveBread", new GiveBread());
        executionHashMap.put("CallNKVD", new CallNKVD());
        executionHashMap.put("CallStalin", new CallStalin());
        executionHashMap.put("RandomChange", new RandomChange());
        executionHashMap.put("TamedBecomesEnemies", new TamedBecomesEnemies());
        executionHashMap.put("HalfHeart", new HalfHeart());
        executionHashMap.put("BigBoom", new BigBoom());
    }

    public static void execute(CommandSender sender, StreamerPlayer streamer, Donation donation) {
        String streamerName = streamer.getName();
        String donationUsername = donation.getName();
        String fullDonationAmount = donation.getAmount();
        String donationMessage = donation.getMessage();
        String executionName = donation.getexecutionName();

        //Если имя донатера не указано - устанавливаем в качестве имени "Кто-то"
        if (donationUsername.equals("")) {
            donationUsername = "Кто-то";
        }

        Boolean canContinue = true;
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

        if (MainConfig.urlFilter) {
            if (Patterns.WEB_URL.matcher(donationUsername).matches()) {
                donationMessage = Patterns.WEB_URL.matcher(donationUsername).replaceAll("*ССЫЛКА УДАЛЕНА*");
            }

            // Пусть будет на будущее. Удаление ссылок из сообщения доната. На данный момент donationMessage нигде не использовался.
            if (Patterns.WEB_URL.matcher(donationMessage).matches()) {
                donationMessage = Patterns.WEB_URL.matcher(donationMessage).replaceAll("*ССЫЛКА УДАЛЕНА*");
            }
        }


        Location streamerPlayerLocation = streamerPlayer.getLocation();
        World world = streamerPlayer.getWorld();
        Vector direction = streamerPlayerLocation.getDirection();

        //streamerPlayer.sendActionBar(donationMessage);


        if (executionHashMap.containsKey(executionName)) {
            executionHashMap.get(executionName).execute(streamerPlayer, donationUsername);
        }
    }




}
