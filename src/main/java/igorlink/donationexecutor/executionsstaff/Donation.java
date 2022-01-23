package igorlink.donationexecutor.executionsstaff;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Donation {
    private CommandSender sender;
    private String username;
    private String amount;
    private String message;
    private String executionName = null;

    public Donation(String _username, String _amount, String _message) {
        new Donation(Bukkit.getConsoleSender(), _username, _amount, _message);
    }

    public Donation(CommandSender _sender, String _username, String _amount, String _message) {
        sender = _sender;
        if (_username == null || _username.equals("")) {
            username = "Аноним";
        } else {
            username = _username;
        }
        amount = _amount;
        message = _message;
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

    public String getMessage() {
        return message;
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String _executionName) {
        executionName = _executionName;
    }


}
