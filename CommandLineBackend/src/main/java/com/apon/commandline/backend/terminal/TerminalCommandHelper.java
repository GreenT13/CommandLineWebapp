package com.apon.commandline.backend.terminal;

/**
 * Facade class for commands to help execute code.
 */
public class TerminalCommandHelper {
    private TerminalSocketIO terminalSocketIO;

    TerminalCommandHelper(TerminalSocketIO terminalSocketIO) {
        this.terminalSocketIO = terminalSocketIO;
    }

    public void sendMessage(String message) {
        terminalSocketIO.sendMessage(message);
    }
}
