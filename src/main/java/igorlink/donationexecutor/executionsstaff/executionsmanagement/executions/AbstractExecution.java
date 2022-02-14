package igorlink.donationexecutor.executionsstaff.executionsmanagement.executions;

import igorlink.donationexecutor.executionsstaff.executionsmanagement.Executor;
import org.bukkit.entity.Player;

public abstract class AbstractExecution {
    private final String execName;

    public AbstractExecution() {
        String className = this.getClass().getName();
        String packageName = this.getClass().getPackageName() + ".";
        className = className.replace(packageName, "");
        execName = className;
        Executor.AddToList(this);
    }

    public abstract Boolean execute(String donationUsername, Player player, String donationAmount);

    public String getName() {
        return execName;
    }

}
