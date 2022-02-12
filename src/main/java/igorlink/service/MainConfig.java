package igorlink.service;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MainConfig {
    private static FileConfiguration config = DonationExecutor.getInstance().getConfig();
    private static int dirtAmount;
    private static int diamondsAmount;
    private static int breadAmount;
    private static int bigBoomRadius;
    private static int timeForAnnouncement;
    private static int amountOfNKVD;
    private static String token;
    private static List<String> listOfBlackListedSubstrings = new ArrayList<>();
    public static List<String> listOfWhiteListedSubstrings = new ArrayList<>();
    private static boolean twitchFilter;
    private static boolean forceResourcePack;
    private static boolean optifineNotification;
    public static boolean showBigAnnouncement;

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
    public static void loadMainConfig(Boolean isReload) {
        DonationExecutor.getInstance().saveDefaultConfig();

        //Если это перезагрузка, обновляем данные, очищаем список игроков
        if (isReload) {
            DonationExecutor.getInstance().reloadConfig();
        }

        config = DonationExecutor.getInstance().getConfig();

        dirtAmount = config.getInt("dirt-amount");
        diamondsAmount = config.getInt("diamonds-amount");
        breadAmount = config.getInt("bread-amount");
        bigBoomRadius = config.getInt("big-boom-radius");
        timeForAnnouncement = config.getInt("announcement-duration-seconds");
        timeForAnnouncement = config.getInt("announcement-duration-seconds");
        amountOfNKVD = config.getInt("nkvd-officers-amount");

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = config.getStringList("BlacklistedSubstrings");
        listOfWhiteListedSubstrings = config.getStringList("WhitelistedSubstrings");

        showBigAnnouncement = config.getBoolean("show-big-announcement");
        optifineNotification = config.getBoolean("notify-about-optifine");
        twitchFilter = config.getBoolean("twitch-filter");
        forceResourcePack = config.getBoolean("force-download-resourcepack");

    }


    public static void turnFilterOn() {
        twitchFilter = true;
    }
    public static void turnFilterOff() { twitchFilter = false; }

    public static boolean getFilterStatus() { return twitchFilter; }
    public static boolean isForceResourcePack() { return forceResourcePack; }
    public static boolean isOptifineNotificationOn() { return optifineNotification; }
    public static boolean getshowBigAnnouncement() { return showBigAnnouncement; }
    public static int getDirtAmount() { return dirtAmount; }
    public static int getBigBoomRadius() { return bigBoomRadius; }
    public static int getDiamondsAmount() { return diamondsAmount; }
    public static int getBreadAmount() { return breadAmount; }
    public static int getTimeForAnnouncement() { return timeForAnnouncement; }
    public static int getAmountOfNKVD() { return amountOfNKVD; }
    public static List<String> getListOfBlackListedSubstrings() { return listOfBlackListedSubstrings; }


}
