package util;

import org.apache.commons.cli.*;

public class CommandParsing {
    public static void main(String... args) {
        String command = "cv --imag";

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(createOptions(), command.split(" "));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Why is this true?
        System.out.println(commandLine.hasOption("i"));

    }

    private static Options createOptions() {
        Options options = new Options();

        Option option = Option.builder("i")
                .longOpt("image")
                .desc("Get a picture of me!")
                .build();

        options.addOption(option);

        return options;
    }

}
