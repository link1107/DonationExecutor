package igorlink.service;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainConfig {
    private static HashMap<String, HashMap<String, String>> donationAmountsHashMap = new HashMap<String, HashMap<String, String>>();
    private static FileConfiguration config = DonationExecutor.getInstance().getConfig();
    public static int dirtAmount = 0;
    public static int diamondsAmount = 0;
    public static int breadAmount = 0;
    public static int bigBoomRadius = 0;
    public static String token;
    public static List<String> listOfBlackListedSubstrings = new ArrayList<>();
    public static List<String> listOfWhiteListedSubstrings = new ArrayList<>();
    private static Boolean twitchFilter;
    private static Boolean forceResourcePack;
    private static Boolean optifineNotification;

    //Геттер конфига
    public static FileConfiguration getConfig(){
        return config;
    }

    //Обновить данные из конфига
    public static void reloadMainConfig() throws InterruptedException {
        loadMainConfig(true);
    }

    //Загрузить данные из конфига без указания параметра перезагрузки
    public static void loadMainConfig() throws InterruptedException {
        loadMainConfig(false);
    }

    //Загрузка данных из конфигфайла с указанным параметром перезагрузки
    public static void loadMainConfig(Boolean isReload) throws InterruptedException {
        DonationExecutor.getInstance().saveDefaultConfig();

        //Если это перезагрузка, обновляем данные, очищаем список игроков
        if (isReload) {
            DonationExecutor.getInstance().reloadConfig();
        }

        config = DonationExecutor.getInstance().getConfig();

        dirtAmount = config.getInt("DirtAmount");
        diamondsAmount = config.getInt("DiamondsAmount");
        breadAmount = config.getInt("BreadAmount");
        bigBoomRadius = config.getInt("BigBoomRadius");

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = config.getStringList("BlacklistedSubstrings");
        listOfWhiteListedSubstrings = config.getStringList("WhitelistedSubstrings");

        optifineNotification = config.getBoolean("notify-about-optifine");
        twitchFilter = config.getBoolean("twitch-filter");
        forceResourcePack = config.getBoolean("force-download-resourcepack");
    }


    public static void turnFilterOn() {
        twitchFilter = true;
    }
    public static void turnFilterOff() { twitchFilter = false; }

    public static Boolean getFilterStatus() { return twitchFilter; }
    public static Boolean isForceResourcePack() {
        return forceResourcePack;
    }
    public static Boolean isOptifineNotificationOn() { return optifineNotification; }


}
