package xyz.iconc.dev.Bot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.iconc.dev.Bot.Controller;

public class Test extends Command {
    public Test() {
        super("test", new String[]{}, 0, new Role[]{},null, Controller.getConfig().getSuperUsers(), true);
    }


    @Override
    public void Handle(Member member, GuildMessageReceivedEvent event, String[] args) {

    }
}
