package com.apon.plugin.implementation;

import com.apon.commandline.backend.plugin.interfaces.IPlugin;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class HelloWorldPlugin implements IPlugin {
    @Override
    public String execute(CommandLine commandLine) {
        if (commandLine.hasOption("v")) {
            return "Your version 1.0";
        }

        return "Hello world plugin";
    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder("v")
                .longOpt("version")
                .desc("The version of the plugin")
                .build());

        return options;
    }
}
