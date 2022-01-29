package igorlink.donationalerts;
import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.Executor;
import igorlink.donationexecutor.executionsstaff.Donation;
import igorlink.donationexecutor.executionsstaff.StreamerPlayer;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import io.socket.emitter.Emitter.Listener;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static igorlink.service.Utils.logToConsole;


public class DonationAlertsToken {
    private static DonationAlertsConnection donationAlertsConnection;
    private List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<StreamerPlayer>();
    private String token;

    public DonationAlertsToken(String token) {
        this.token = token;
        try {
            donationAlertsConnection = new DonationAlertsConnection(this);
            donationAlertsConnection.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (String spName : MainConfig.getConfig().getConfigurationSection("DonationAmounts." + token).getKeys(false)) {
            listOfStreamerPlayers.add(new StreamerPlayer(spName, this));
        }
    }

    public String getToken() {
        return token;
    }

    public void executeDonationsInQueues() {
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            if ( !(Bukkit.getPlayer(sp.getName()) == null) ) {
                if (!(Bukkit.getPlayer(sp.getName()).isDead())) {
                    Donation donation = sp.takeDonationFromQueue();
                    if (donation==null) {
                        continue;
                    }
                    Utils.logToConsole("Отправлен на выполнение донат §b" + donation.getexecutionName() + "§f для стримера §b" + sp.getName() + "§f от донатера §b" + donation.getName());
                    Executor.DoExecute(donation.getSender(), sp.getName(), donation.getName(), donation.getAmount(), donation.getMessage(), donation.getexecutionName());
                }
            }

        }
    }

    public void addStreamerPlayer(@NotNull String streamerPlayerName) {
        listOfStreamerPlayers.add(new StreamerPlayer(streamerPlayerName, this));
    }

    public StreamerPlayer getStreamerPlayer(@NotNull String name) {
        for (StreamerPlayer p : listOfStreamerPlayers) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    //Добавление доната в очередь
    public void addToDonationsQueue(Donation donation) {
        String execution;
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            execution = sp.checkExecution(Utils.cutOffKopeykis(donation.getAmount()));
            if (!(execution == null)) {
                donation.setexecutionName(execution);
                sp.putDonationToQueue(donation);
                Utils.logToConsole("Донат от §b" + donation.getName() + "§f в размере §b" + donation.getAmount() + " руб.§f был обработан и отправлен в очередь на выполнение.");
                return;
            }
        }
    }

    public void disconnect() {
        donationAlertsConnection.disconnect();
    }

    public int getNumberOfStreamerPlayers() {
        return listOfStreamerPlayers.size();
    }
}