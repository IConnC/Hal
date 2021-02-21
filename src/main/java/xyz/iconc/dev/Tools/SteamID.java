package xyz.iconc.dev.Tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SteamID {
    private static final Logger logger = LogManager.getLogger(SteamID.class);

    public static String toSteamID(long steam64) {
        int universe = (int) ((steam64 >> 56L) & ((1L << 8L) - 1L));
        int accountId = (int) (steam64 & ((1L << 32L) - 1L));
        int lowestBit = accountId & 1;
        int highest32 = (accountId >> 1) & ((1 << 31) - 1);
        return "STEAM_" + universe + ":" + lowestBit + ":" + highest32;
    }
    public static long toSteam64(String steamID) {
        String[] components = steamID.substring(6).split(":");
        int lowestBit = Integer.parseInt(components[1]);
        String accountID = EnsureBinaryLength(Integer.toBinaryString((Integer.parseInt(components[2]) << 1) + lowestBit),32);
        String universe = EnsureBinaryLength("1",1);
        String accountType = EnsureBinaryLength("1",4);
        String instance = EnsureBinaryLength("1",20);
        return Long.parseLong(universe + accountType + instance + accountID, 2);
    }

    public static boolean ValidSteamIdentifier(String steamId) {
        if (!steamId.startsWith("STEAM_")) return false;
        return steamId.substring(6).split(":").length == 3;
    }
    public static boolean ValidSteamIdentifier(long steam64) {
        return General.GetDecimalLength(steam64) == 17;
    }
    public static long GetSteam64FromString(String input) {
        try {
            long out = Long.parseLong(input);
            if (ValidSteamIdentifier(out)) return out;
        } catch (NumberFormatException ignored) {}

        if (ValidSteamIdentifier(input)) return toSteam64(input);

        return -1L;
    }

    public static String EnsureBinaryLength(String binary, int length) {
        StringBuilder out = new StringBuilder(binary);
        while (out.length() < length) {
            out.insert(0, "0");
        }
        return out.toString();
    }

    public static void main(String[] args) {
        // STEAM_1:0:138784590
        // steamID3: [U:1:277569180]
        // 76561198237834908
        String steamID = "STEAM_1:0:138784590";
        long steam64 = 76561198237834908L;
        System.out.println(toSteam64(steamID));
        System.out.println(toSteam64("STEAM_0:1:1545775"));
        System.out.println(toSteam64("STEAM_1:0:14314400"));
        System.out.println(toSteamID(76561197963357279L));
        System.out.println(ValidSteamIdentifier(steam64));
        System.out.println(ValidSteamIdentifier(steamID));
    }
}
