package com.apon.commandline.backend.plugin.interfaces;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface IPlugin {
    String execute(CommandLine commandLine);

    Options getOptions();
}
