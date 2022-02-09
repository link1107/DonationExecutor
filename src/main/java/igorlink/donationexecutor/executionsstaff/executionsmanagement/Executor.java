package igorlink.donationexecutor.executionsstaff.executionsmanagement;

import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.AbstractExecution;
import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.inventory.ShitToInventory;

import java.util.ArrayList;
import java.util.List;

public class Executor {
    private static List<AbstractExecution> listOfExecutions = new ArrayList<AbstractExecution>();

    public Executor() {
        new ShitToInventory();
    }

    public static void AddToList(AbstractExecution execution) {
        listOfExecutions.add(execution);
    }




}
