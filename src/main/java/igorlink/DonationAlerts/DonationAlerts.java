package igorlink.DonationAlerts;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.executionsstaff.Donation;
import io.socket.emitter.Emitter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import io.socket.emitter.Emitter.Listener;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.checkerframework.framework.qual.Unused;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static igorlink.service.Utils.logToConsole;


public class DonationAlerts {

    private final Socket socket;

    public DonationAlerts(String server) throws URISyntaxException {

        URI url = new URI(server);
        socket = IO.socket(url);

        Listener connectListener = arg0 -> logToConsole("Произведено успешное подключение!");

        Listener disconectListener = arg0 -> logToConsole("Соединение разорвано!");

        Listener donationListener = arg0 -> {

            JSONObject json = new JSONObject((String) arg0[0]);
            json.toString();
            new BukkitRunnable() {
                @Override
                public void run() {

                    if ((json.isNull("username")) || (json.isNull("amount_formatted"))) {
                        return;
                    }

                    if ((json.getString("amount_formatted")).length() <= 1) {
                        return;
                    }

                    DonationExecutor.getInstance().listOfStreamerPlayers
                            .addToDonationsQueue(new Donation(Bukkit.getConsoleSender(),
                                    json.getString("username"),
                                    json.getString("amount_formatted"),
                                    json.getString("message")));

                }
            }.runTask(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("DonationExecutor")));

        };

        Listener errorListener = arg0 -> logToConsole("Произошла ошибка подключения к Donation Alerts!");

        socket.on(Socket.EVENT_CONNECT, connectListener)
                .on(Socket.EVENT_DISCONNECT, disconectListener)
                .on(Socket.EVENT_CONNECT_ERROR, errorListener)
                .on("donation", donationListener);
    }

    public void Connect (String token) throws JSONException {
        socket.connect();
        socket.emit("add-user", new JSONObject()
                .put("token", token)
                .put("type", "minor"));
    }

    public void Disconnect() throws JSONException {
        socket.disconnect();
    }

    @Deprecated
    public boolean getConnected() {
        return socket.connected();
    }
}