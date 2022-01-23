package igorlink.donationexecutor;

import igorlink.command.DonationExecutorCommand;
import igorlink.donationexecutor.executionsstaff.GiantMobManager;
import igorlink.donationexecutor.executionsstaff.ListOfStreamerPlayers;
import igorlink.service.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import igorlink.DonationAlerts.*;
import java.net.URISyntaxException;
import static igorlink.service.Utils.*;


public final class DonationExecutor extends JavaPlugin {

    public static final String DASERVER = "https://socket.donationalerts.ru:443";
    private static DonationExecutor instance;
    public static DonationAlerts da;
    public static GiantMobManager giantMobManager;
    public static Boolean isRunning = true;
    public ListOfStreamerPlayers listOfStreamerPlayers;


    @Override
    public void onEnable() {
        instance = this;
        listOfStreamerPlayers = new ListOfStreamerPlayers();
        MainConfig.loadMainConfig();
        giantMobManager = new GiantMobManager(this);

        if (CheckNameAndToken()) {
            try {
                da = new DonationAlerts(DASERVER);
                da.connect(MainConfig.token);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            new DonationExecutorCommand();
        }


        Bukkit.getPluginManager().registerEvents(new EventListener(),this);

    }

    @Override
    public void onDisable() {
        try {
            isRunning = false;
            da.disconnect();
            Thread.sleep(1000);
            da = null;
        } catch (Exception e) {
            logToConsole("Какая-то ебаная ошибка, похуй на нее вообще");
        }
    }


    public static DonationExecutor getInstance() {
        return instance;
    }


}
