package com.apon.commandline.backend.command.implementation.uploadplugin;

import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadPluginCommand implements ICommand {
    private TerminalCommandHelper terminalCommandHelper;
    private Logger logger = LogManager.getLogger(UploadPluginCommand.class);

    @Override
    public void run(CommandInput commandInput) {
        terminalCommandHelper.sendMessage("Received plugin.");
        logger.debug("File: {}", commandInput.fileBase64);
    }

    @Override
    public String getCommandIdentifier() {
        return "upload-plugin";
    }

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {
        this.terminalCommandHelper = terminalCommandHelper;
    }
}
