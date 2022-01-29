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

    public MainConfig() {

    }

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
            DonationExecutor.getInstance().streamerPlayersManager.reload();
        }

        config = DonationExecutor.getInstance().getConfig();

        dirtAmount = Integer.valueOf(config.getString("DirtAmount"));
        diamondsAmount = Integer.valueOf(config.getString("DiamondsAmount"));
        breadAmount = Integer.valueOf(config.getString("BreadAmount"));
        bigBoomRadius = Integer.valueOf(config.getString("BigBoomRadius"));

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = config.getStringList("BlacklistedSubstrings");
        listOfWhiteListedSubstrings = config.getStringList("WhitelistedSubstrings");

        twitchFilter = config.getBoolean("TwitchFilter");

    }


    public static void turnFilterOn() {
        twitchFilter = true;
    }

    public static void turnFilterOff() {
        twitchFilter = false;
    }

    public static Boolean getFilterStatus() {
        return twitchFilter;
    }

    public static HashMap<String, String> getNameAndExecution (@NotNull String donationAmount) {
        String thisDonateForStreamerName = null;
        String nameOfExecution = null;
        for (String p : donationAmountsHashMap.keySet()) {
            if (donationAmountsHashMap.get(p).containsKey(donationAmount)) {
                HashMap<String, String> temp = new HashMap<String, String>();
                thisDonateForStreamerName = p;
                nameOfExecution = donationAmountsHashMap.get(p).get(donationAmount);
                temp.put("name", p);
                temp.put("execution", nameOfExecution);
                return temp;
            }
        }

        return null;
    }


}
