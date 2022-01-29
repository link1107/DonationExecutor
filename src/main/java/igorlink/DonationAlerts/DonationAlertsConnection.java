package igorlink.donationalerts;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.executionsstaff.Donation;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static igorlink.service.Utils.logToConsole;

public class DonationAlertsConnection {
    private Emitter.Listener connectListener;
    private Emitter.Listener disconectListener;
    private Emitter.Listener donationListener;
    private Emitter.Listener errorListener;
    private static final String DASERVER = "https://socket.donationalerts.ru:443";
    private URI url;
    private Socket socket;
    private DonationAlertsToken donationAlertsToken;


    public DonationAlertsConnection(DonationAlertsToken donationAlertsToken) throws URISyntaxException {
        this.donationAlertsToken = donationAlertsToken;
        url = new URI(DASERVER);
        socket = IO.socket(url);

        connectListener = new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                logToConsole("Произведено успешное подключение для токена §b" + donationAlertsToken.getToken());
            }
        };

        disconectListener = new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                logToConsole("Произведено отключение для токена §b" + donationAlertsToken.getToken());
            }
        };

        donationListener = new Emitter.Listener() {
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

                        DonationAlertsConnection.this.donationAlertsToken.addToDonationsQueue(new Donation(Bukkit.getConsoleSender(),
                                        json.getString("username"),
                                        json.getString("amount_formatted"),
                                        json.getString("message")));

                    }
                }.runTask(DonationExecutor.getInstance());

            }
        };

        errorListener = new Emitter.Listener() {
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


    public void connect() throws JSONException {
        socket.connect();
        socket.emit("add-user", new JSONObject()
                .put("token", donationAlertsToken.getToken())
                .put("type", "minor"));
    }

    public void disconnect() throws JSONException {
        socket.disconnect();
    }

    public boolean getConnected() {
        return socket.connected();
    }


}