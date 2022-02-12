package igorlink.donationexecutor.playersmanagement;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Donation {
    private final CommandSender sender;
    private final String username;
    private final String amount;

    @Nullable
    private ExecutionType executionType = null;

    public Donation(@NotNull String _username, @NotNull String _amount) {
        this(Bukkit.getConsoleSender(), _username, _amount);
    }

    public Donation(@NotNull CommandSender _sender, @NotNull String _username, @NotNull String _amount) {
        sender = _sender;
        if (_username.length() == 0) {
            username = "Аноним";
        } else {
            username = _username;
        }
        amount = _amount;
    }

    @NotNull
    public CommandSender getSender() {
        return sender;
    }

    @NotNull
    public String getName() {
        return username;
    }

    @NotNull
    public String getAmount() {
        return amount;
    }

    @Nullable
    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(@Nullable ExecutionType type) {
        executionType = type;
    }
}
