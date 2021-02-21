package xyz.iconc.dev.Bot;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.ConfigObjects.GuildConfigObject;
import xyz.iconc.dev.Tools.SQLiteConnector;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class Bot {
    public static final String PREFIX = "!";
    @Getter private static JDA api;
    @Getter private static Bot bot;
    private Logger logger = LogManager.getLogger(Bot.class);
    @Getter private CommandManager commandManager;
    private List<GuildConfigObject> guildCachedConfigs;

    public Bot(String token) throws LoginException {
        guildCachedConfigs = new ArrayList<>();

        Collection<GatewayIntent> gatewayIntents = Arrays.asList(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING
        );

        commandManager = new CommandManager();
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setEventManager(new EventManager(commandManager));
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.enableIntents(gatewayIntents);
        api = builder.build();
        api.getPresence().setStatus(OnlineStatus.ONLINE);
        api.getPresence().setActivity(Activity.playing("Killing all humans"));
        bot = this;
        new SQLiteConnector();
    }


    public GuildConfigObject GetGuildConfig(long guildId) {
        for (GuildConfigObject cfg : guildCachedConfigs){
            if (cfg.getGuildId() == guildId) return cfg;
        }
        return null;
    }

    public void AddGuildConfig(GuildConfigObject cfg) {
        for (int i=0; i<guildCachedConfigs.size(); i++) {
            if (cfg.getGuildId() == guildCachedConfigs.get(i).getGuildId()) {
                guildCachedConfigs.remove(i);
            }
        }
        guildCachedConfigs.add(cfg);
    }

    public void RemoveGuildConfig(long guildId) {
        for (int i=0; i<guildCachedConfigs.size(); i++) {
            if (guildCachedConfigs.get(i).getGuildId() == guildId) {
                guildCachedConfigs.remove(i);
                return;
            }
        }
    }

    public void ClearCachedGuildConfigs() {
        guildCachedConfigs.clear();
    }


    public void Shutdown(){
        Controller.OnBotShutdown(this);
    }

    public static boolean IsValidDiscordId(long discordId) {
        return api.getUserById(discordId) != null;
    }

    @Deprecated
    public static void WaitForOperationToComplete(Message message, Consumer<Message> callback) {
    }
}
