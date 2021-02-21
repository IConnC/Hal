package xyz.iconc.dev.Bot.Commands;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.iconc.dev.Tools.General;

public abstract class Command {
    @Getter protected final String Key;
    @Getter protected final String[] Aliases;
    @Getter protected final int ArgCount;
    @Getter protected Role[] RolesRequired;
    @Getter protected Permission PermissionRequired;
    @Getter protected long[] AllowedUsers;
    @Getter protected boolean Restricted;

    public Command(String key, String[] aliases, int argCount, Role[] rolesRequired, Permission permissionRequired, long[] allowedUsers, boolean restricted){
        Key = key;
        Aliases = aliases;
        ArgCount = argCount;
        RolesRequired = rolesRequired;
        PermissionRequired = permissionRequired;
        AllowedUsers = allowedUsers;
        Restricted = restricted;
    }

    public abstract void Handle(Member member, GuildMessageReceivedEvent event, String[] args);

    public boolean IsMemberAllowed(Member member){
        if (Restricted) {
            if (AllowedUsers != null) {
                for (long id : AllowedUsers){
                    if (id == member.getIdLong()) return true;
                }
            }
            if (PermissionRequired != null && member.hasPermission(PermissionRequired)) return true;
            if (RolesRequired.length > 0) {
                for (Role role : member.getRoles()) {
                    if (General.Contains(RolesRequired, role)) return true;
                }
            }
        }
        return !Restricted;
    }
}
