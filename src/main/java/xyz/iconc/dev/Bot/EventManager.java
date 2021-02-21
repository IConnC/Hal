package xyz.iconc.dev.Bot;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.Bot.Commands.ConfigCommand;
import xyz.iconc.dev.Bot.Commands.Test;
import xyz.iconc.dev.Tools.SQLiteConnector;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EventManager implements IEventManager {

    private List<Object> listeners;
    private CommandManager commandManager;
    @Getter private Logger logger = LogManager.getLogger(EventManager.class);


    public EventManager(CommandManager commandManager){
        listeners = new ArrayList<Object>();
        this.commandManager = commandManager;
    }

    @Override
    public void register(@Nonnull Object listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(@Nonnull Object listener) {
        listeners.remove(listener);
    }

    @Override
    public void handle(@Nonnull GenericEvent _event) {
        if (_event instanceof GuildMessageReceivedEvent) {
            commandManager.HandleEvent((GuildMessageReceivedEvent) _event);
        }
        else if (_event instanceof GuildMemberJoinEvent) {
            CMemberJoinEvent((GuildMemberJoinEvent) _event);
        }
        else if (_event instanceof GuildMemberRemoveEvent) {
            CMemberRemoveEvent((GuildMemberRemoveEvent) _event);
        }
        else if (_event instanceof GuildJoinEvent) {
            GuildJoinEvent event = (GuildJoinEvent) _event;
            SQLiteConnector.getSqLiteConnector().ValidateGuild(event.getGuild().getIdLong());
        }
        else if (_event instanceof ReadyEvent){
            List<Guild> guilds = _event.getJDA().getGuilds();
            for (Guild guild : guilds) {
                SQLiteConnector.getSqLiteConnector().ValidateGuild(guild.getIdLong());
                Bot.getBot().AddGuildConfig(SQLiteConnector.getSqLiteConnector().GetGuildConfig(guild.getIdLong()));
            }
            logger.info("----- **Begin initialization** -----");
            logger.info("Registering Commands...");
            commandManager.RegisterCommand(new ConfigCommand());
            commandManager.RegisterCommand(new Test());
            logger.info("Successfully Registered Commands");
            logger.info("----- **Fully initialized** -----");
        }

    }

    private void CMemberJoinEvent(GuildMemberJoinEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Welcome to the **Sacrifice Gaming** " + event.getUser().getAsTag());
        //builder.setDescription("We're delighted that you decided to join us " + event.getMember().getAsMention() + " but please be aware there are some rule you need to follow.\n Please check out our rules over at " + Controller.getConfig().GetRulesChannel().getAsMention() + ".");
        builder.setThumbnail(event.getMember().getUser().getAvatarUrl());
        builder.setFooter("I am the H.A.L 9000. You may call me Hal.", Bot.getApi().getSelfUser().getAvatarUrl());
        builder.setColor(Color.BLUE);
        //Controller.getConfig().GetWelcomeChannel().sendMessage(builder.build()).queue();
    }

    private void CMemberRemoveEvent(GuildMemberRemoveEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("You will be missed " + event.getUser().getAsTag());
        builder.setDescription(event.getUser().getName() + " has left the server.");
        builder.setThumbnail(event.getUser().getAvatarUrl());
        builder.setFooter("I am the H.A.L 9000. You may call me Hal.", Bot.getApi().getSelfUser().getAvatarUrl());
        builder.setColor(Color.RED);
        //Controller.getConfig().GetWelcomeChannel().sendMessage(builder.build()).queue();
    }

    @Nonnull
    @Override
    public List<Object> getRegisteredListeners() {
        return listeners;
    }
}
