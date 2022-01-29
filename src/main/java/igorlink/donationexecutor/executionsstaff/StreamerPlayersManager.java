package igorlink.donationexecutor.executionsstaff;

import igorlink.donationalerts.DonationAlertsToken;
import igorlink.donationexecutor.DonationExecutor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StreamerPlayersManager {
    private List<DonationAlertsToken> listOfDonationAlertsTokens = new ArrayList<DonationAlertsToken>();

    //Таймер будет выполнять донаты из очередей игроков каждые 2 сек, если они живы и онлайн - выполняем донат и убираем его из очереди
    public StreamerPlayersManager() {
        getTokensFromConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!DonationExecutor.isRunning()) {
                    this.cancel();
                    return;
                }

                for (DonationAlertsToken token : listOfDonationAlertsTokens) {
                    token.executeDonationsInQueues();
                }
            }
        }.runTaskTimer(DonationExecutor.getInstance(), 0, 40);
    }

    private void getTokensFromConfig() {
        Set<String> tokensStringList = MainConfig.getConfig().getConfigurationSection("DonationAmounts").getKeys(false);
        for (String token : tokensStringList) {
            this.addTokenToList(token);
        }

        int numOfStreamerPlayers = 0;
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            numOfStreamerPlayers += token.getNumberOfStreamerPlayers();
        }

        Utils.logToConsole("Было добавлено §b" + listOfDonationAlertsTokens.size() + " §fтокенов, с которыми связано §b" + numOfStreamerPlayers + " §fигроков.");
    }

    public StreamerPlayer getStreamerPlayer(@NotNull String name) {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            if (token.getStreamerPlayer(name) != null) {
                return token.getStreamerPlayer(name);
            }
        }
        return null;
    }

    public void reload() throws InterruptedException {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.disconnect();
        }
        Thread.sleep(1000);
        listOfDonationAlertsTokens.clear();
        getTokensFromConfig();
    }

    public void stop() throws InterruptedException {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.disconnect();
        }
        Thread.sleep(1000);
        listOfDonationAlertsTokens.clear();
    }

    public void addToDonationsQueue(Donation donation) {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.addToDonationsQueue(donation);
        }
    }

    private void addTokenToList(String token) {
        listOfDonationAlertsTokens.add(new DonationAlertsToken(token));
    }

    private int getNumberOfTokens() {
        return listOfDonationAlertsTokens.size();
    }




}
