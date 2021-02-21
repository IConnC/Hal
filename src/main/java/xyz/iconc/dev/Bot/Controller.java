package xyz.iconc.dev.Bot;

import lombok.Getter;
import lombok.Setter;
import xyz.iconc.dev.ConfigObjects.ConfigObject;
import xyz.iconc.dev.ConfigObjects.GuildConfigObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @Getter private static Bot BotInstance;
    @Getter private static ConfigObject Config;

    public static void main(String[] args) throws IOException {
        Config = xyz.iconc.dev.Bot.Config.LoadConfig();
        CreateNewBot(Config.getBotToken());

        //CreateNewBot("NzAxMTg1MDcyNjE0NzM2MDEy.XreX7Q.ebxkNTlEWJhxPtew9L4nfq-95z8");
    }

    public static void CreateNewBot(String token){
        if (BotInstance != null) BotInstance.Shutdown();
        try{
        BotInstance = new Bot(token);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public static void OnBotShutdown(Bot instance){
    }
}
