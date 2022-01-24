package igorlink.donationexecutor.executionsstaff;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.Executor;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListOfStreamerPlayers {
    private final List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<StreamerPlayer>();

    //Таймер будет выполнять донаты из очередей игроков каждые 2 сек, если они живы и онлайн - выполняем донат и убираем его из очереди
    public ListOfStreamerPlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!DonationExecutor.isRunningStatus) {
                    this.cancel();
                    return;
                }

                for (StreamerPlayer sp : listOfStreamerPlayers) {
                    if ( !(Bukkit.getPlayer(sp.getName()) == null) ) {
                        if (!(Objects.requireNonNull(Bukkit.getPlayer(sp.getName())).isDead())) {
                            Donation donation = sp.takeDonationFromQueue();
                            if (donation==null) {
                                continue;
                            }
                            Utils.logToConsole("Отправлен на выполнение донат §b" + donation.getExecutionName() + "§f для стримера §b" + sp.getName() + "§f от донатера §b" + donation.getUsername());
                            Executor.DoExecute(donation.getSender(), sp.getName(), donation.getUsername(), donation.getAmount(), donation.getMessage(), donation.getExecutionName());
                        }
                    }

                }
            }
        }.runTaskTimer(DonationExecutor.getInstance(), 0, 40);
    }

    public int getNumberOfStreamers() {
        return listOfStreamerPlayers.size();
    }

    //Добавляем стримера в список
    public void addStreamerPlayer(@NotNull String streamerPlayerName) {
        listOfStreamerPlayers.add(new StreamerPlayer(streamerPlayerName));
    }

    public StreamerPlayer getStreamerPlayer(@NotNull String name) {
        for (StreamerPlayer p : listOfStreamerPlayers) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    //Очищаем список стримеров
    public void clear() {
        listOfStreamerPlayers.clear();
    }

    //Добавление доната в очередь
    public void addToDonationsQueue(Donation donation) {
        String execution;
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            execution = sp.checkExecution(Utils.cutOffKopeykis(donation.getAmount()));
            if (!(execution == null)) {
                donation.setExecutionName(execution);
                sp.putDonationToQueue(donation);
                Utils.logToConsole("Донат от §b" + donation.getUsername() + "§f в размере §b" + donation.getAmount() + " руб.§f был обработан и отправлен в очередь на выполнение.");
                return;
            }
        }
    }

}
