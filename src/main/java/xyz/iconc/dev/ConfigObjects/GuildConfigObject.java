package xyz.iconc.dev.ConfigObjects;

import lombok.Getter;
import lombok.Setter;

public class GuildConfigObject {
    @Getter private final long GuildId;
    @Getter @Setter private long WelcomeChannel;
    @Getter @Setter private long RulesChannel;

    public GuildConfigObject(long guildId, long welcomeChannel, long rulesChannel) {

        GuildId = guildId;
        WelcomeChannel = welcomeChannel;
        RulesChannel = rulesChannel;
    }
}
