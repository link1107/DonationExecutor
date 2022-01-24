package igorlink.donationexecutor;

import igorlink.command.DonationExecutorCommand;
import igorlink.donationexecutor.executionsstaff.GiantMobManager;
import igorlink.donationexecutor.executionsstaff.ListOfStreamerPlayers;
import igorlink.service.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import igorlink.DonationAlerts.*;
import org.json.JSONException;

import java.net.URISyntaxException;
import static igorlink.service.Utils.*;


public final class DonationExecutor extends JavaPlugin {

    public static final String apiLink = "https://socket.donationalerts.ru:443";
    private static DonationExecutor instance;
    public static DonationAlerts da;
    public static GiantMobManager giantMobManager;
    public static Boolean isRunningStatus = true;
    public ListOfStreamerPlayers listOfStreamerPlayers;


    @Override
    public void onEnable() {
        instance = this;
        listOfStreamerPlayers = new ListOfStreamerPlayers();
        MainConfig.loadMainConfig();
        giantMobManager = new GiantMobManager(this);

        if (CheckNameAndToken()) {
            try {
                da = new DonationAlerts(apiLink);
                da.connect(MainConfig.token);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            new DonationExecutorCommand();
        }
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        try {
            isRunningStatus = false;
            da.disconnect();
            Thread.sleep(1000);
        } catch (JSONException e) {
            logToConsole("Какая-то ебаная ошибка, похуй на нее вообще");
        } catch (Exception e){
            logToConsole("Произошла неустановленная ошибка, как такое могло произойти?");
        }  finally {
            da = null;
        }
    }

    public static DonationExecutor getInstance() {
        return instance;
    }
}
