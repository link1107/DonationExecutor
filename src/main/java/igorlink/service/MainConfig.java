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

    // TODO: Статические постоянные значения принято писать большими буквами.
    public static int dirtAmount, diamondsAmount, breadAmount, bigBoomRadius;
    public static String token;
    public static List<String> listOfBlackListedSubstrings = new ArrayList<>(), listOfWhiteListedSubstrings = new ArrayList<>();
    private static boolean twitchFilter;

    public MainConfig() {

    }

    //Геттер конфига
    public static FileConfiguration getConfig(){
        return config;
    }

    //Загрузить данные из конфига без указания параметра перезагрузки
    public static void loadMainConfig() {
        loadMainConfig(false);
    }

    // Метод для конвертаций значений конфига
    public static void convert() {
        int v = config.getInt("_version");

        switch (v) {
            case 0: {
                if(config.getString("StreamersNamesList") != null) {
                    for (String playerName : config.getString("StreamersNamesList").split(",")) {
                        config.set("DonationAmounts." + playerName + ".Active", true);
                    }
                }

                config.set("StreamersNamesList", null);

                List<String> listOfBlackListedSubstrings = Arrays.asList(config.getString("BlacklistedSubstrings").split(",")),
                             listOfWhiteListedSubstrings = Arrays.asList(config.getString("WhitelistedSubstrings").split(","));

                config.set("BlacklistedSubstrings", listOfBlackListedSubstrings);
                config.set("WhitelistedSubstrings", listOfWhiteListedSubstrings);

                config.set("_version", config.getInt("_version") + 1);
                DonationExecutor.getInstance().saveConfig();
                break;
            }
        }
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
        convert();

        /*
          Игорь, посмотри методы в MemorySection и ConfigurationSection.
          В IDEA ты можешь это сделать при помощи: лев. CTRL + ПКМ по методу.

          Тебе не нужно каждый раз получать значение в виде строки.
          Есть готовые методы, как для листов, так и для integer, double и т.д.

          WhitelistedSubstrings:
          - книг
          - нигерия
         */

        for (String playerName : config.getConfigurationSection("DonationAmounts").getKeys(false)) {
            if(!config.getBoolean("DonationAmounts." + playerName + ".Active"))
                continue;

            DonationExecutor.getInstance().listOfStreamerPlayers.addStreamerPlayer(playerName);
        }

        logToConsole("При чтении файла конфигурации было добавлено §b" + DonationExecutor.getInstance().listOfStreamerPlayers.getNumberOfStreamers() + "§f стримеров.");

        dirtAmount = config.getInt("DirtAmount");
        diamondsAmount = config.getInt("DiamondsAmount");
        breadAmount = config.getInt("BreadAmount");
        bigBoomRadius = config.getInt("BigBoomRadius");

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = config.getStringList("BlacklistedSubstrings");
        listOfWhiteListedSubstrings = config.getStringList("WhitelistedSubstrings");

        twitchFilter = config.getBoolean("TwitchFilter");

    }

    public static void turnFilterOn() {
        config.set("TwitchFilter", (twitchFilter = true));
        DonationExecutor.getInstance().saveConfig();

    }

    public static void turnFilterOff() {
        config.set("TwitchFilter", (twitchFilter = false));
        DonationExecutor.getInstance().saveConfig();
    }

    public static boolean getFilterStatus() {
        return twitchFilter;
    }

    public static HashMap<String, String> getNameAndExecution (@NotNull String donationAmount) {
        String nameOfExecution;
        for (String p : donationAmountsHashMap.keySet()) {
            if (donationAmountsHashMap.get(p).containsKey(donationAmount)) {
                HashMap<String, String> temp = new HashMap<String, String>();
                nameOfExecution = donationAmountsHashMap.get(p).get(donationAmount);
                temp.put("name", p);
                temp.put("execution", nameOfExecution);
                return temp;
            }
        }

        return null;
    }


}
