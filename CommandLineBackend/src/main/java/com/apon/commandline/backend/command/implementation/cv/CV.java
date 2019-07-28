package com.apon.commandline.backend.command.implementation.cv;

import com.apon.commandline.backend.command.MyUtil;
import com.apon.commandline.backend.command.framework.ICommand;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

/**
 * Basic command to test functionality.
 */
public class CV implements ICommand {
    private Logger logger = LogManager.getLogger(CV.class);

    @Override
    public String run(String command) {
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(createOptions(), command.split(" "));
        } catch (ParseException e) {
            e.printStackTrace();
            return "Wrong argument.";
        }

        try {
            InputStream inputStream;
            if (commandLine.hasOption("i")) {
                inputStream = MyUtil.getCommandFile(this.getClass(), "_picture.txt");
            } else {
                inputStream = MyUtil.getCommandFile(this.getClass(), "_content.txt");
            }

            return MyUtil.getContentOfFile(inputStream);
        } catch (Exception e) {
            logger.error("Could not read CV file.", e);
            return "CV not found. Contact the administrator.";
        }
    }

    @Override
    public String getCommandIdentifier() {
        return "cv";
    }

    private Options createOptions() {
        Options options = new Options();

        Option option = Option.builder("i")
                .longOpt("image")
                .desc("Get a picture of me!")
                .build();

        options.addOption(option);

        return options;
    }
}
