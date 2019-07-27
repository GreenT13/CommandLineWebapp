package util;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateBase64String {
    public static void main(String... args) throws IOException {
        File file = new File("C:\\Users\\Gebruiker\\IdeaProjects\\CommandLineWebapp\\out\\artifacts\\PluginImplementation_jar\\PluginImplementation.jar");
        byte[] data = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        System.out.println(new String(data, StandardCharsets.UTF_8));
    }
}
