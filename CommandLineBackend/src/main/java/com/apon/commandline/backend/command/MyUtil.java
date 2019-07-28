package com.apon.commandline.backend.command;

import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.terminal.Terminal;
import com.apon.commandline.backend.terminal.TerminalException;
import org.apache.commons.io.IOUtils;

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
     * Return the input stream of the file inside the jar.
     *
     * Throws TerminalException when the file could not be found.
     */
    public static InputStream getCommandFile(Class<? extends ICommand> commandClass, String extension) throws TerminalException {
        // We could also do this with paths, however Heroku throws FileSystemNotFoundException. So I do it this way.
        InputStream inputStream = commandClass.getClassLoader().getResourceAsStream(
                commandClass.getName().replaceAll("\\.", "/") + extension);

        if (inputStream == null) {
            throw new TerminalException("Input stream is null.");
        }

        return inputStream;
    }

    /**
     * Return content as string from an input stream.
     * @param inputStream The stream
     */
    public static String getContentOfFile(InputStream inputStream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        return writer.toString();
    }

}
