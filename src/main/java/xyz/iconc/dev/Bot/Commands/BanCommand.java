package xyz.iconc.dev.Bot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.iconc.dev.Bot.Controller;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", new String[] {}, 1, new Role[]{}, null, Controller.getConfig().getSuperUsers(),true);
    }

    @Override
    public void Handle(Member member, GuildMessageReceivedEvent event, String[] args) {

    }
}
