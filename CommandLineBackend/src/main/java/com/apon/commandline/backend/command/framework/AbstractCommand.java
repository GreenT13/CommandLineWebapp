package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.terminal.TerminalCommandHelper;

/**
 * For more information, see {@link ICommand}.
 */
public abstract class AbstractCommand implements ICommand {
    protected TerminalCommandHelper terminalCommandHelper;

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {
        this.terminalCommandHelper = terminalCommandHelper;
    }
}
