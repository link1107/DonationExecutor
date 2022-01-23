package igorlink.donationexecutor.executionsstaff;

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
    private String streamerPlayerName;
    private List<Item> listOfDeathDropItems = new ArrayList<Item>();
    private Queue<Donation> listOfQueuedDonations = new LinkedList<Donation>();
    private HashMap<Integer, String> listOfAmounts = new HashMap<Integer, String>();


    //Инициализация нового объекта стримера-игрока
    public StreamerPlayer(@NotNull String _streamerPlayerName) {
        FileConfiguration config = MainConfig.getConfig();
        streamerPlayerName = _streamerPlayerName;

        //Заполняем список сумм для донатов
        for (String execName : Executor.executionsList) {
            int amount = config.getInt("DonationAmounts." + streamerPlayerName + "." + execName);
            if (amount != 0) {
                listOfAmounts.put(amount, execName);
            } else {
                Utils.logToConsole("Сумма доната, необходимая для " + execName + " для стримера " + streamerPlayerName + " не найдена. Проверьте правильность заполнения файла конфигурации DonationExecutor.yml в папке с именем плагина.");
            }
        }
    }

    public String checkExecution(@NotNull String amount) {
        int a;
        try {
            a = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            a = 0;
        }
        return listOfAmounts.get(a);
    }

    public String getName() {
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

    /***
     * PlayerDeathListener переехал в EventListener >_<
     * Регистрировать целый слушатель на каждого пользователя в данном контексте не обязательно.
     */

}
