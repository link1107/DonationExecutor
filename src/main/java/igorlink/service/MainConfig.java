package igorlink.service;

import igorlink.donationexecutor.DonationExecutor;
import kotlin.Suppress;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static igorlink.service.Utils.logToConsole;

public class MainConfig {
    private static final HashMap<String, HashMap<String, String>> donationAmountsHashMap = new HashMap<>();
    private static FileConfiguration config = DonationExecutor.getInstance().getConfig();
    public static int dirtAmount = 0;
    public static int diamondsAmount = 0;
    public static int breadAmount = 0;
    public static int bigBoomRadius = 0;
    public static String token;
    public @SuppressWarnings("unchecked") static List<String> listOfBlackListedSubstrings = new ArrayList<>();
    public @SuppressWarnings("unchecked") static List<String> listOfWhiteListedSubstrings = new ArrayList<>();
    private static Boolean twitchFilter;

    public MainConfig() {

    }

    //Геттер конфига
    public static FileConfiguration getConfig(){
        return config;
    }

    //Обновить данные из конфига
    public @Suppress(names = "UNUSED_PARAMETER") static void reloadMainConfig() {
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

        @SuppressWarnings("unchecked") List<String> streamerPlayersNamesList = (List<String>) getConfig().getList("StreamersNamesList");
        assert streamerPlayersNamesList != null;
        for (String playerName : streamerPlayersNamesList) {
            DonationExecutor.getInstance().listOfStreamerPlayers.addStreamerPlayer(playerName);
        }

        logToConsole("При чтении файла конфигурации было добавлено §b" + DonationExecutor.getInstance().listOfStreamerPlayers.getNumberOfStreamers() + "§f стримеров.");

        dirtAmount = Integer.parseInt(Objects.requireNonNull(config.getString("DirtAmount")));
        diamondsAmount = Integer.parseInt(Objects.requireNonNull(config.getString("DiamondsAmount")));
        breadAmount = Integer.parseInt(Objects.requireNonNull(config.getString("BreadAmount")));
        bigBoomRadius = Integer.parseInt(Objects.requireNonNull(config.getString("BigBoomRadius")));

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = (List<String>) config.getList("BlacklistedSubstrings");
        listOfWhiteListedSubstrings = (List<String>) config.getList("WhitelistedSubstrings");

        twitchFilter = config.getBoolean("TwitchFilter");
        logToConsole("Ошибка при чтении значение TwitchFilter");
    }

    public static void turnFilterOn() {
        twitchFilter = true;
    }

    public static void turnFilterOff() {
        twitchFilter = false;
    }

    public @Suppress(names = "UNUSED") static Boolean getFilterStatus() {
        return twitchFilter;
    }

    public @Suppress(names = "UNUSED") static HashMap<String, String> getNameAndExecution (@NotNull String donationAmount) {
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