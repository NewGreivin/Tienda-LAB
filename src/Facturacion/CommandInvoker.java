package Facturacion;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private final List<Command> commandHistory;

    public CommandInvoker() {
        this.commandHistory = new ArrayList<>();
    }

    public void executeCommand(Command command) {
        commandHistory.add(command);
        command.ejecutar();
    }

    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
}