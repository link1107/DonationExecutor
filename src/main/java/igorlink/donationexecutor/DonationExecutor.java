package igorlink.donationexecutor;

import igorlink.command.DonationExecutorCommand;
import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.inventory.ShitToInventory;
import igorlink.donationexecutor.executionsstaff.giantmobs.GiantMobManager;
import igorlink.donationexecutor.playersmanagement.StreamerPlayersManager;
import igorlink.service.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static igorlink.service.Utils.*;

public final class DonationExecutor extends JavaPlugin {
    private static DonationExecutor instance;
    public static GiantMobManager giantMobManager;
    private static Boolean isRunningStatus = true;
    public StreamerPlayersManager streamerPlayersManager;


    @Override
    public void onEnable() {
        instance = this;
        try {
            MainConfig.loadMainConfig();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CheckNameAndToken()) {
            streamerPlayersManager = new StreamerPlayersManager();
            giantMobManager = new GiantMobManager(this);
            new DonationExecutorCommand();
        }

        ShitToInventory shitCommand = new ShitToInventory();
        logToConsole(shitCommand.getName());
        Bukkit.getPluginManager().registerEvents(new GeneralEventListener(),this);

    }

    @Override
    public void onDisable() {
        try {
            isRunningStatus = false;
            streamerPlayersManager.stop();
        } catch (InterruptedException e) {
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
