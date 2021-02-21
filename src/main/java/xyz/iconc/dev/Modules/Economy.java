package xyz.iconc.dev.Modules;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.Tools.SQLiteConnector;

public class Economy {
    @Getter private static final Logger logger = LogManager.getLogger(Economy.class);


    public static int GetBalance(long discordId) {
        return SQLiteConnector.getSqLiteConnector().GetMemberBalance(discordId);
    }

    public static void AddToBalance(long discordId, int amount) {
        SQLiteConnector.getSqLiteConnector().UpdateMemberBalance(discordId,GetBalance(discordId) + amount);
    }

    public static void RemoveFromBalance(long discordId, int amount) {
        AddToBalance(discordId, amount * -1);
    }

    public static void ResetBalance(long discordId) {
        SQLiteConnector.getSqLiteConnector().UpdateMemberBalance(discordId, 0);
    }

}
