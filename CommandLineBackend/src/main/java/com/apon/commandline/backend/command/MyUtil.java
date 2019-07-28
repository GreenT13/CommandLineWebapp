package com.apon.commandline.backend.command;

import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.terminal.TerminalException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class MyUtil {
    /**
     * Determine whether a file is a JAR File.
     */
    public static boolean isJarFile(File file) throws IOException {
        if (!isZipFile(file)) {
            return false;
        }
        ZipFile zip = new ZipFile(file);
        boolean manifest = zip.getEntry("META-INF/MANIFEST.MF") != null;
        zip.close();
        return manifest;
    }
    /**
     * Determine whether a file is a ZIP File.
     */
    private static boolean isZipFile(File file) throws IOException {
        if(file.isDirectory()) {
            return false;
        }
        if(!file.canRead()) {
            throw new IOException("Cannot read file "+file.getAbsolutePath());
        }
        if(file.length() < 4) {
            return false;
        }
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int test = in.readInt();
        in.close();
        return test == 0x504b0304;
    }

    /**
     * Return the full path of the command inside the jar, appending the extension.
     *
     * Throws TerminalException when the file could not be found.
     */
    public static Path getCommandFile(Class<? extends ICommand> commandClass, String extension) {
        return Paths.get(commandClass.getName().replaceAll("\\.", "/") + extension);
    }

    public static String getContentOfCommandFile(Path path) throws URISyntaxException, IOException, TerminalException {
        URL url = MyUtil.class.getClassLoader().getResource(path.toString());
        if (url == null) {
            throw new TerminalException("File could not be found: " + path.toString());
        }

        return Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);
    }

}
