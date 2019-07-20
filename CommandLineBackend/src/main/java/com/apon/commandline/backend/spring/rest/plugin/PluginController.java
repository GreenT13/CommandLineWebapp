package com.apon.commandline.backend.spring.rest.plugin;

import com.apon.commandline.backend.plugin.PluginException;
import com.apon.commandline.backend.plugin.PluginFacade;
import com.apon.commandline.backend.spring.database.Plugin;
import com.apon.commandline.backend.spring.database.PluginRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController()
public class PluginController {
    private Logger logger = LogManager.getLogger(PluginController.class);
    private PluginRepository pluginRepository;

    public PluginController(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    @GetMapping("plugin/execute")
    public PluginResponseValueObject executePlugin(@RequestParam() String pluginIdentifier) {
        PluginFacade pluginFacade = new PluginFacade(pluginRepository);
        String output;
        try {
            output = pluginFacade.executePluginWithIdentifier(pluginIdentifier);
        } catch (PluginException e) {
            logger.error("Failed to execute plugin with identifier '{}'", pluginIdentifier, e);
            return new PluginResponseValueObject("Failed!");
        }

        return new PluginResponseValueObject(output);
    }

    @PostMapping("plugin/upload")
    public PluginResponseValueObject uploadPlugin(@RequestBody PluginInputValueObject pluginInputValueObject) throws IOException {
        Plugin plugin = new Plugin("hello-world", getBytesFromBase64File(pluginInputValueObject.fileContent));
        pluginRepository.save(plugin);

        return new PluginResponseValueObject("Succes!");
    }


    private Byte[] getBytesFromBase64File(String fileContent) throws IOException {
        String PREFIX = "data:application/octet-stream;base64,";
        if (!fileContent.startsWith(PREFIX)) {
            throw new IOException("Could not parse file content.");
        }
        fileContent = fileContent.substring(PREFIX.length());
        byte[] data = Base64.decodeBase64(fileContent);
        return ArrayUtils.toObject(data);
    }

}
