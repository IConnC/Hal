package xyz.iconc.dev.Bot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.Bot.Bot;
import xyz.iconc.dev.Bot.Controller;
import xyz.iconc.dev.ConfigObjects.GuildConfigObject;
import xyz.iconc.dev.Tools.General;
import xyz.iconc.dev.Tools.SQLiteConnector;

import java.util.concurrent.TimeUnit;

public class ConfigCommand extends Command {
    private static Logger logger = LogManager.getLogger(ConfigCommand.class);

    public ConfigCommand() {
        super("config", new String[] {"cfg"}, 1, new Role[]{}, null, Controller.getConfig().getSuperUsers(),true);
}

    @Override
    public void Handle(Member member, GuildMessageReceivedEvent event, String[] args) {
        event.getMessage().delete().queue();
        if (args.length != 3) {
            InvalidArgs(member, args, event);
            return;
        }
        long value = General.GetLong(args[2]);
        if (value == -1) {
            InvalidArgs(member, args, event);
            return;
        }

        GuildConfigObject cfg = Bot.getBot().GetGuildConfig(event.getGuild().getIdLong());
        switch (args[1].toLowerCase()) {
            case "welcomechannel":
                cfg.setWelcomeChannel(value);
                Bot.getBot().AddGuildConfig(cfg);
                SQLiteConnector.getSqLiteConnector().UpdateConfig(cfg);
                break;
            case "ruleschannel":
                cfg.setRulesChannel(value);
                Bot.getBot().AddGuildConfig(cfg);
                SQLiteConnector.getSqLiteConnector().UpdateConfig(cfg);
                break;
            default:
                break;
        }

        event.getChannel().sendMessage("Successfully set key `" + args[1] + "` to `" + value + "`!").queue((result) -> {
            result.delete().queueAfter(10, TimeUnit.MINUTES);
        });
    }

    private void InvalidArgs(Member member, String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Invalid command format, read the documentation for help.").queue((result) -> {
            result.delete().queueAfter(30, TimeUnit.SECONDS);
        });
        logger.info("Member: " + member.getEffectiveName()
                + " attempted to run the config command with incorrect arguments!\n" + args.toString());
    }
}
