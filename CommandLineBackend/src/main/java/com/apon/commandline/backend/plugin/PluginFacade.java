package com.apon.commandline.backend.plugin;

import com.apon.commandline.backend.plugin.interfaces.IPlugin;
import com.apon.commandline.backend.spring.database.Plugin;
import com.apon.commandline.backend.spring.database.PluginRepository;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginFacade {
    private Logger logger = LogManager.getLogger(PluginFacade.class);
    private PluginRepository pluginRepository;

    public PluginFacade(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    /**
     * Execute a plugin, and return the response.
     * @param pluginIdentifier The identifier of the plugin.
     */
    public String executePluginWithIdentifier(String pluginIdentifier, String fullCommand) throws PluginException {
        // Search the plugin.
        Plugin plugin = pluginRepository.findByPluginIdentifier(pluginIdentifier).get(0);

        // Store the plugin (temporary).
        File jar;
        try {
            jar = writeJarToDisk(plugin.getJarContent(), pluginIdentifier);
        } catch (IOException e) {
            logger.error("Failed to write jar to disk.", e);
            throw new PluginException("Failed to write jar to disk.");
        }

        // Execute the plugin (assume we only have one class that implements the interface).
        String output;
        try {
            output = executeFirstPluginYouCanFind(jar, fullCommand);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Failed to execute plugin.", e);
            throw new PluginException("Failed to execute plugin.");
        }

        // Remove the jar.
        removeJarFromDisk(jar);

        // Return the output.
        return output;
    }

    /**
     * Write the jar to disk and returns the file object of the jar.
     * @param fileContent The content of the file.
     * @param pluginIdentifier The identifier of the plugin.
     */
    private File writeJarToDisk(Byte[] fileContent, String pluginIdentifier) throws IOException, PluginException {
        // Create a file with a unique name.
        File file = new File("target/" + pluginIdentifier + System.nanoTime() + ".jar");

        // Make sure the file does not exist.
        if (file.exists()) {
            throw new PluginException("Could not create jar on disk.");
        }

        // Write the file to disk.
        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(ArrayUtils.toPrimitive(fileContent));
        }

        return file;
    }

    /**
     * Execute the implementation of the IPlugin.
     * @param jar The jar in which the plugin resides.
     */
    private String executeFirstPluginYouCanFind(File jar, String fullCommand) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, PluginException {
        // Create the class loader.
        URL[] urls = { new URL("jar:file:" + jar.getAbsolutePath() + "!/") };
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);

        // Find the plugin.
        Reflections reflections = new Reflections(classLoader);
        Class<? extends IPlugin> iPluginClass = reflections.getSubTypesOf(IPlugin.class).iterator().next();

        // Instantiate the plugin.
        Constructor<? extends IPlugin> constructor = iPluginClass.getConstructor();
        IPlugin plugin = constructor.newInstance();

        // Execute the plugin.
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(plugin.getOptions(), fullCommand.split(" "));
        } catch (ParseException e) {
            logger.error("Failed to parse command.", e);
            throw new PluginException("Failed to parse command.");
        }
        String output = plugin.execute(commandLine);

        // Close the class loader (otherwise the jar will stay open).
        classLoader.close();

        return output;
    }

    /**
     * Remove the jar from disk.
     * @param jar The jar.
     */
    private void removeJarFromDisk(File jar) throws PluginException {
        if (!jar.delete()) {
            throw new PluginException("Could not remove jar " + jar.getAbsolutePath());
        }
    }
}
