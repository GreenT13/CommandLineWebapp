package com.apon.commandline.backend.command.implementation.cv;

import com.apon.commandline.backend.command.MyUtil;
import com.apon.commandline.backend.command.framework.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Basic command to test functionality.
 */
public class CV implements ICommand {
    private Logger logger = LogManager.getLogger(CV.class);

    @Override
    public String run(String command) {
        try {
            InputStream inputStream = MyUtil.getCommandFile(this.getClass(), "_content.txt");

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
}
