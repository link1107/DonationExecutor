package igorlink.service;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import static igorlink.service.Utils.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    public static boolean urlFilter;

    public MainConfig() {

    }

    //Геттер конфига
    public static FileConfiguration getConfig(){
        return config;
    }

    //Обновить данные из конфига
    public static void reloadMainConfig() {
        loadMainConfig(true);
    }

    //Загрузить данные из конфига без указания параметра перезагрузки
    public static void loadMainConfig() {
        loadMainConfig(false);
    }

    //Загрузка данных из конфигфайла с указанным параметром перезагрузки
    public static void loadMainConfig(Boolean isReload) {
        DonationExecutor.getInstance().saveDefaultConfig();

        //Если это перезагрузка, обновляем данные, очищаем список игроков
        if (isReload) {
            DonationExecutor.getInstance().reloadConfig();
            DonationExecutor.getInstance().listOfStreamerPlayers.clear();
        }

        config = DonationExecutor.getInstance().getConfig();

        List <String> streamerPlayersNamesList = new ArrayList<>();
        streamerPlayersNamesList = (List<String>) getConfig().getList("StreamersNamesList");
        for (String playerName : streamerPlayersNamesList) {
            DonationExecutor.getInstance().listOfStreamerPlayers.addStreamerPlayer(playerName);
        }

        logToConsole("При чтении файла конфигурации было добавлено §b" + DonationExecutor.getInstance().listOfStreamerPlayers.getNumberOfStreamers() + "§f стримеров.");

        dirtAmount = Integer.valueOf(config.getString("DirtAmount"));
        diamondsAmount = Integer.valueOf(config.getString("DiamondsAmount"));
        breadAmount = Integer.valueOf(config.getString("BreadAmount"));
        bigBoomRadius = Integer.valueOf(config.getString("BigBoomRadius"));

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = Arrays.asList(config.getString("BlacklistedSubstrings").split(","));
        listOfWhiteListedSubstrings = Arrays.asList(config.getString("WhitelistedSubstrings").split(","));

        // Если у человека стоит старая версия плагина, и не указано значение ключа URLFilter - ставим по умолчанию нужный ключ, и сейвим конфиг.
        // Вообще конфиг по-хорошему полностью переделать, но как нибудь потом.
        if (config.get("URLFilter") == null) {
            config.set("URLFilter", true);
            DonationExecutor.getInstance().saveConfig();
        }
        urlFilter = config.getBoolean("URLFilter");
        twitchFilter = config.getBoolean("TwitchFilter");
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
