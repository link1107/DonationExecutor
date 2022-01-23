package igorlink.donationalerts;
import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.executionsstaff.Donation;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import io.socket.emitter.Emitter.Listener;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import static igorlink.service.Utils.logToConsole;


public class DonationAlerts {
    private Listener connectListener;
    private Listener disconectListener;
    private Listener donationListener;
    private Listener errorListener;

    private URI url;
    private Socket socket;


    public DonationAlerts(String server) throws URISyntaxException {

        url = new URI(server);
        socket = IO.socket(url);

        connectListener = new Listener() {
            @Override
            public void call(Object... arg0) {
                logToConsole("Произведено успешное подключение!");
          }
        };

        disconectListener = new Listener() {
            @Override
            public void call(Object... arg0) {
                logToConsole("Соединение разорвано!");
            }
        };

        donationListener = new Listener() {
            @Override
            public void call(Object... arg0) {

                JSONObject json = new JSONObject((String) arg0[0]);
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if ( (json.isNull("username")) || (json.isNull("amount_formatted"))) {
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
                }.runTask(Bukkit.getPluginManager().getPlugin("DonationExecutor"));

            }
        };

        errorListener = new Listener() {
            @Override
            public void call(Object... arg0) {
                logToConsole("Произошла ошибка подключения к Donation Alerts!");
            }
        };

        socket.on(Socket.EVENT_CONNECT, connectListener)
                .on(Socket.EVENT_DISCONNECT, disconectListener)
                .on(Socket.EVENT_ERROR, errorListener)
                .on("donation", donationListener);
    }

    public void connect(String token) throws JSONException {
        socket.connect();
        socket.emit("add-user", new JSONObject()
                .put("token", token)
                .put("type", "minor"));
    }

    public void disconnect() throws JSONException {
        socket.disconnect();
    }

    public boolean getConnected() {
        return socket.connected();
    }
}