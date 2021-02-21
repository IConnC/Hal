package xyz.iconc.dev.Bot;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.Bot.Commands.Command;
import xyz.iconc.dev.Tools.General;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<Command> commands;
    @Getter private static final Logger logger = LogManager.getLogger(CommandManager.class);

    public CommandManager(){
        commands = new ArrayList<Command>();
    }
    public void RegisterCommand(Command command) {
        commands.add(command);
        logger.info("Registering Command: " + command.getKey());
    }

    public void RemoveCommand(Command command) {
        commands.remove(command);
        logger.info("Removing Command: " + command.getKey());
    }

    public void HandleEvent(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;
        String[] args = event.getMessage().getContentRaw().toLowerCase().split("\\s+");
        if (args.length < 1) return;
        String key = args[0].substring(Bot.PREFIX.length());

        Command command = null;
        for (Command _command : commands) {
            if (_command.getKey().equals(key) || General.Contains(_command.getAliases(), key)) command = _command;
        }
        if (command == null) return;
        if (!command.IsMemberAllowed(event.getMember())) return;

        if (command.getArgCount() >= 0) {
            if (args.length-1 < command.getArgCount()) {
                event.getChannel().sendMessage("Invalid Arguments!\nThis command requires " +
                        command.getArgCount() + " argument(s).").queue();
                return;
            }
        }
        command.Handle(event.getMember(), event, args);
    }
}
