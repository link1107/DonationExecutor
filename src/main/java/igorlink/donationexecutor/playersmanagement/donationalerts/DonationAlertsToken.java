package igorlink.donationexecutor.playersmanagement.donationalerts;

import igorlink.donationexecutor.Executor;
import igorlink.donationexecutor.playersmanagement.Donation;
import igorlink.donationexecutor.playersmanagement.ExecutionType;
import igorlink.donationexecutor.playersmanagement.StreamerPlayer;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonationAlertsToken {
    private DonationAlertsConnection donationAlertsConnection;
    private final List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<>();
    private final String token;

    public DonationAlertsToken(@NotNull String token) {
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

    @NotNull
    public String getToken() {
        return token;
    }

    public void executeDonationsInQueues() {
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            Player player = Bukkit.getPlayerExact(sp.getName());

            if (player != null && !player.isDead()) {
                Donation donation = sp.takeDonationFromQueue();
                if (donation == null) {
                    continue;
                }

                Utils.logToConsole("Отправлен на выполнение донат §b" + donation.getExecutionType() + "§f для стримера §b" + sp.getName() + "§f от донатера §b" + donation.getName());

                ExecutionType execType = donation.getExecutionType();
                if (execType != null) {
                    Executor.doExecute(sp.getName(), donation.getName(), donation.getAmount(), execType);
                }
            }
        }
    }

    public void addStreamerPlayer(@NotNull String streamerPlayerName) {
        listOfStreamerPlayers.add(new StreamerPlayer(streamerPlayerName, this));
    }

    @Nullable
    public StreamerPlayer getStreamerPlayer(@NotNull String name) {
        for (StreamerPlayer p : listOfStreamerPlayers) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    //Добавление доната в очередь
    public void addToDonationsQueue(@NotNull Donation donation) {
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            ExecutionType execution = sp.getExecutionTypeForAmount(Utils.cutOffKopeykis(donation.getAmount()));

            if (execution != null) {
                donation.setExecutionType(execution);
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