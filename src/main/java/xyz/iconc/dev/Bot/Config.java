package xyz.iconc.dev.Bot;

import com.google.gson.Gson;
import xyz.iconc.dev.ConfigObjects.ConfigObject;

import java.io.*;
import java.util.Scanner;

public class Config {
    public static final String configFileTemplateName = "ConfigTemplate.json";
    public static final String configFilePath = "./config.json";


    public static ConfigObject LoadConfig() throws IOException {
        if (!new File(configFilePath).exists()) {
            //File resourceFile = new File(Config.class.getResource(configFileTemplateName).getFile());
            //FileUtils.copyFile(resourceFile, new File(configFilePath));
            new File(configFilePath).createNewFile();
            throw new FileNotFoundException("File not found: " + configFilePath + "\nPlease add correct details into the file and try again.");
        }
        StringBuilder sb = new StringBuilder();
        Scanner myReader = new Scanner(new File(configFilePath));
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            sb.append(data);
        }
        myReader.close();
        Gson gson = new Gson();
        ConfigObject co = gson.fromJson(sb.toString(), ConfigObject.class);
        System.out.println();
        return co;
    }

    public static void WriteConfig(ConfigObject configObject) throws IOException {
        if (!new File(configFilePath).exists()) {
            new File(configFilePath).createNewFile();
        }
        Gson gson = new Gson();

        FileWriter myWriter = new FileWriter(configFilePath);
        myWriter.write(gson.toJson(configObject));
        myWriter.close();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        Long[] l = new Long[3];
        l[0] = 123L;
        l[1] = 321L;
        l[2] = 456L;
        System.out.println(gson.toJson(l));
    }

}
