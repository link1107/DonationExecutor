package igorlink.donationexecutor.playersmanagement;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Donation {
    private CommandSender sender;
    private String username;
    private String amount;

    private String executionName = null;

    public Donation(String _username, String _amount) {
        new Donation(Bukkit.getConsoleSender(), _username, _amount);
    }

    public Donation(CommandSender _sender, String _username, String _amount) {
        sender = _sender;
        if (_username.equals("")) {
            username = "Аноним";
        } else {
            username = _username;
        }
        amount = _amount;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getName() {
        return username;
    }

    public String getAmount() {
        return amount;
    }

    public String getexecutionName() {
        return executionName;
    }

    public void setexecutionName(String _executionName) {
        executionName = _executionName;
    }


}
