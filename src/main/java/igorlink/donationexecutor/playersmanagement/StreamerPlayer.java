package igorlink.donationexecutor.playersmanagement;

import igorlink.donationexecutor.playersmanagement.donationalerts.DonationAlertsToken;
import igorlink.donationexecutor.DonationExecutor;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StreamerPlayer {
    private final String streamerPlayerName;
    private final List<Item> listOfDeathDropItems = new ArrayList<>();
    private final Queue<Donation> listOfQueuedDonations = new LinkedList<>();
    private final HashMap<String, ExecutionType> listOfAmounts = new HashMap<>();

    //Инициализация нового объекта стримера-игрока
    public StreamerPlayer(@NotNull String _streamerPlayerName, @NotNull DonationAlertsToken donationAlertsToken) {
        FileConfiguration config = MainConfig.getConfig();
        streamerPlayerName = _streamerPlayerName;
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), DonationExecutor.getInstance());

        //Заполняем список сумм для донатов
        String daToken = donationAlertsToken.getToken();
        for (ExecutionType execType : ExecutionType.values()) {
            String amount = config.getString("donation-amounts." + daToken + "." + streamerPlayerName + "." + execType);

            if (amount != null) {
                listOfAmounts.put(amount, execType);
            } else {
                Utils.logToConsole("Сумма доната, необходимая для " + execType + " для стримера " + streamerPlayerName + " не найдена. Проверьте правильность заполнения файла конфигурации DonationExecutor.yml в папке с именем плагина.");
            }
        }
    }

    @Nullable
    public ExecutionType getExecutionTypeForAmount(@NotNull String amount) {
        return listOfAmounts.get(amount);
    }

    @NotNull
    public String getName(){
        return streamerPlayerName;
    }

    //Работа со списком выпавших при смерти вещей
    public void setDeathDrop(@NotNull List<Item> listOfItems) {
        listOfDeathDropItems.clear();
        listOfDeathDropItems.addAll(listOfItems);
    }


    //Работа с очередью донатов
    //Поставить донат в очередь на выполнение донатов для этого игрока
    public void putDonationToQueue(@NotNull Donation donation) {
        listOfQueuedDonations.add(donation);
    }

    //Взять донат из очереди и убрать его из нее
    @Nullable
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
    private static final class PlayerDeathListener implements Listener{
        private final StreamerPlayer thisStreamerPlayer;

        //Передача родительского объекта в слушателя
        private PlayerDeathListener(@NotNull StreamerPlayer _thisStreamerPlayer) {
            thisStreamerPlayer = _thisStreamerPlayer;
        }

        //Если данный игрок умер - запоминаем его посмертный дроп
        @EventHandler
        private void onPlayerDeath(@NotNull PlayerDeathEvent event) {
            List<Item> deathDrop = new ArrayList<>();
            Player player = event.getPlayer();
            World world = player.getWorld();
            List<ItemStack> drops = event.getDrops();

            if (player.getName().equals(thisStreamerPlayer.getName())) {
                for (ItemStack i : event.getDrops()) {
                    deathDrop.add(world.dropItemNaturally(player.getLocation(), i));
                }
            }

            drops.clear();
            thisStreamerPlayer.setDeathDrop(deathDrop);
        }
    }
}
