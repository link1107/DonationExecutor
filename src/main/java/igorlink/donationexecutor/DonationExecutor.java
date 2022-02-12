package igorlink.donationexecutor;

import igorlink.command.DonationExecutorCommand;
import igorlink.donationexecutor.executionsstaff.giantmobs.GiantMobManager;
import igorlink.donationexecutor.playersmanagement.StreamerPlayersManager;
import igorlink.service.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static igorlink.service.Utils.*;

public final class DonationExecutor extends JavaPlugin {
    private static DonationExecutor instance;
    public static GiantMobManager giantMobManager;
    private static boolean isRunningStatus = true;
    public StreamerPlayersManager streamerPlayersManager;


    @Override
    public void onEnable() {
        instance = this;
        try {
            MainConfig.loadMainConfig();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (checkNameAndToken()) {
            streamerPlayersManager = new StreamerPlayersManager();
            giantMobManager = new GiantMobManager(this);
            new DonationExecutorCommand();
        }

        Bukkit.getPluginManager().registerEvents(new GeneralEventListener(),this);
    }

    @Override
    public void onDisable() {
        try {
            isRunningStatus = false;
            if (checkNameAndToken()) {
                streamerPlayersManager.stop();
            }
        } catch (InterruptedException e) {
            logToConsole("Какая-то ебаная ошибка, похуй на нее вообще");
        }
    }

    @NotNull
    public static DonationExecutor getInstance() {
        return instance;
    }

    public static boolean isRunning() {
        return isRunningStatus;
    }
}
