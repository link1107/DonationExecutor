package igorlink.donationexecutor.playersmanagement;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import igorlink.donationexecutor.playersmanagement.donationalerts.DonationAlertsToken;
import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.Executor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class StreamerPlayer {
    private final String streamerPlayerName;
    private final List<Item> listOfDeathDropItems = new ArrayList<>();
    private final Queue<Donation> listOfQueuedDonations = new LinkedList<>();
    private final HashMap<String, String> listOfAmounts = new HashMap<>();


    //Инициализация нового объекта стримера-игрока
    public StreamerPlayer(@NotNull String _streamerPlayerName, DonationAlertsToken donationAlertsToken) {
        FileConfiguration config = MainConfig.getConfig();
        streamerPlayerName = _streamerPlayerName;
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), DonationExecutor.getInstance());


        //Заполняем список сумм для донатов
        String amount;
        for (String execName : Executor.executionsNamesList) {
            amount = config.getString("donation-amounts." + donationAlertsToken.getToken() + "." + streamerPlayerName + "." + execName);
            if (!(amount==null)) {
                listOfAmounts.put(amount, execName);
            } else {
                Utils.logToConsole("Сумма доната, необходимая для " + execName + " для стримера " + streamerPlayerName + " не найдена. Проверьте правильность заполнения файла конфигурации DonationExecutor.yml в папке с именем плагина.");
            }
        }
    }

    public String checkExecution(@NotNull String amount) {
        return listOfAmounts.get(amount);
    }

    public String getName(){
        return streamerPlayerName;
    }

    //Работа со списком выпавших при смерти вещей
    public void setDeathDrop(List<Item> listOfItems) {
        listOfDeathDropItems.clear();
        listOfDeathDropItems.addAll(listOfItems);
    }


    //Работа с очередью донатов
    //Поставить донат в очередь на выполнение донатов для этого игрока
    public void putDonationToQueue(@NotNull Donation donation) {
        listOfQueuedDonations.add(donation);
    }

    //Взять донат из очереди и убрать его из нее
    public Donation takeDonationFromQueue() {
        return listOfQueuedDonations.poll();
    }


    //Удалить дроп игрока после смерти
    public boolean removeDeathDrop() {
        boolean wasAnythingDeleted = false;
        for (Item i : listOfDeathDropItems) {
            if (i.isDead()) {
                continue;
            }
            i.remove();
            wasAnythingDeleted = true;
        }
        return wasAnythingDeleted;
    }

    //Замена дездропа при смерти игрока (через Listener)
    private static class PlayerDeathListener implements Listener{
        StreamerPlayer thisStreamerPlayer;

        //Передача родительского объекта в слушателя
        private PlayerDeathListener(StreamerPlayer _thisStreamerPlayer) {
            thisStreamerPlayer = _thisStreamerPlayer;
        }

        //Если данный игрок умер - запоминаем его посмертный дроп
        @EventHandler
        private void onPlayerDeath(PlayerDeathEvent event) {
            List<Item> deathDrop = new ArrayList<>();
            if (event.getPlayer().getName().equals(thisStreamerPlayer.getName())) {
                for (ItemStack i : event.getDrops()) {
                    deathDrop.add(event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), i));
                }
            }
            event.getDrops().clear();
            thisStreamerPlayer.setDeathDrop(deathDrop);
        }

    }



}
