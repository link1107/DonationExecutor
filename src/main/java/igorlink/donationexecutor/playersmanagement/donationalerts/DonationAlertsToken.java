package igorlink.donationexecutor.playersmanagement.donationalerts;

import igorlink.donationexecutor.Executor;
import igorlink.donationexecutor.playersmanagement.Donation;
import igorlink.donationexecutor.playersmanagement.StreamerPlayer;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonationAlertsToken {
    private DonationAlertsConnection donationAlertsConnection;
    private final List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<>();
    private final String token;

    public DonationAlertsToken(String token) {
        this.token = token;
        try {
            donationAlertsConnection = new DonationAlertsConnection(this);
            donationAlertsConnection.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (String spName : Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts." + token)).getKeys(false)) {
            listOfStreamerPlayers.add(new StreamerPlayer(spName, this));
        }
    }

    public String getToken() {
        return token;
    }

    public void executeDonationsInQueues() {
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            if ( (Bukkit.getPlayerExact(sp.getName()) != null) && (!(Objects.requireNonNull(Bukkit.getPlayerExact(sp.getName())).isDead())) ) {
                    Donation donation = sp.takeDonationFromQueue();
                    if (donation==null) {
                        continue;
                    }
                    Utils.logToConsole("Отправлен на выполнение донат " + ChatColor.AQUA + donation.getExecutionName() + ChatColor.WHITE + " для стримера " + ChatColor.AQUA + sp.getName() + ChatColor.WHITE + " от донатера " + ChatColor.AQUA + donation.getName());
                    Executor.DoExecute(sp.getName(), donation.getName(), donation.getAmount(), donation.getExecutionName());
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
                donation.setExecutionName(execution);
                sp.putDonationToQueue(donation);
                Utils.logToConsole("Донат от " + ChatColor.AQUA + donation.getName() + ChatColor.WHITE + " в размере " + ChatColor.AQUA + donation.getAmount() + " руб." + ChatColor.WHITE + " был обработан и отправлен в очередь на выполнение.");
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