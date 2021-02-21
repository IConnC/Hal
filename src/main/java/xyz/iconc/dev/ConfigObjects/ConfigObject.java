package xyz.iconc.dev.ConfigObjects;

import lombok.Getter;

public class ConfigObject {
    @Getter private final String BotToken;
    @Getter private final long[] SuperUsers;


    public ConfigObject(String botToken, long[] superUsers) {
        BotToken = botToken;
        SuperUsers = superUsers;
    }

}
