package util;

public class Substring {
    public static void main(String... args) {
        String command = "hello world asdf";
        int startIndex = command.indexOf(" ") + 1;
        int endIndex = (command.substring(command.indexOf(" ") + 1) + " ").indexOf(" ") + startIndex;

        System.out.println(command.substring(startIndex, endIndex));
    }
}
