package xyz.iconc.dev.Tools;

import net.dv8tion.jda.api.entities.User;
import xyz.iconc.dev.Bot.Bot;

public class General {
    public static String GetIdentifierType(String identification) {
        return "";
    }
    public static int GetDecimalLength(long num) {
        return (int) (Math.floor(Math.log10(Math.abs(num))) + 1);
    }

    public static int GetDecimalLength(int num) {
        return GetDecimalLength((long) num);
    }

    public static <T> boolean Contains(T[] o1, Object o2) {
        for (int i=0; i<o1.length; i++) {
            if (o1[i].equals(o2)) return true;
        }
        return false;
    }

    // Returns long from string. If string doesn't contain a viable long, returns -1
    public static long GetLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static int GetInt(String input) {
        return (int) GetLong(input);
    }
    // Returns int from string. If string doesn't contain a viable int, returns -1


    public static void main(String[] args) {
        Integer[] i1 = new Integer[]{1,2,3};
        int t = 2;
        System.out.println(Contains(i1,t));
    }
}
