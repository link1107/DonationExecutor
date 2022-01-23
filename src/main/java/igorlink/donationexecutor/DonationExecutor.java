package igorlink.donationexecutor;
import igorlink.command.DonationExecutorCommand;
import igorlink.donationalerts.DonationAlerts;
import igorlink.donationexecutor.executionsstaff.GiantMobManager;
import igorlink.donationexecutor.executionsstaff.ListOfStreamerPlayers;
import igorlink.service.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.net.URISyntaxException;
import static igorlink.service.Utils.*;


public final class DonationExecutor extends JavaPlugin {

    public static final String DASERVER = "https://socket.donationalerts.ru:443";
    private static DonationExecutor instance;
    public static igorlink.donationalerts.DonationAlerts da;
    public static GiantMobManager giantMobManager;
    private static Boolean isRunningStatus = true;
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
            isRunningStatus = false;
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


    public static Boolean isRunning() {
        return isRunningStatus;
    }


}
