package xyz.iconc.dev.Tools;


import com.sun.rowset.CachedRowSetImpl;
import lombok.Getter;
import net.dv8tion.jda.api.events.UpdateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.iconc.dev.Bot.Controller;
import xyz.iconc.dev.ConfigObjects.GuildConfigObject;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLiteConnector {
    @Getter private static SQLiteConnector sqLiteConnector;
    @Getter private static final Logger logger = LogManager.getLogger(SQLiteConnector.class);

    public SQLiteConnector() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection con=DriverManager.getConnection("jdbc:sqlite:guilds.db");
            Statement stmt=con.createStatement();

            //ResultSet rs=stmt.executeQuery("select * from PlayerDB");
            //while(rs.next())
             //   System.out.println(rs.getLong(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getInt(4));
            //con.close();
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        sqLiteConnector = this;
    }

    private CachedRowSet ExecuteQuery(String query) {
        try (Connection con = GetConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            CachedRowSet rowSet = new CachedRowSetImpl();
            rowSet.populate(rs);
            rs.close();
            st.close();
            con.close();
            logger.info("Query: `" + query + "` has been executed");
            return rowSet;
        }
        catch (SQLException e) {
            logger.error(e.toString());
        }
        return null;
    }
    private int ExecuteUpdate(String query) {
        try (Connection con = GetConnection()) {
            Statement st = con.createStatement();
            int res = st.executeUpdate(query);
            logger.info("Query: `" + query + "` has been executed resulting in " + res + " row(s) being updated.");
            return res;
        }
        catch (SQLException | NullPointerException e) {
            logger.error(e.toString());
        }
        return -1;
    }

    public void ValidateGuild(long guildId) {
        //System.out.println(guildId);
        ExecuteUpdate("INSERT OR IGNORE INTO settings (guildId) VALUES (" + guildId + ")");
    }

    public GuildConfigObject GetGuildConfig(long guildId) {
        CachedRowSet rs = ExecuteQuery("SELECT * FROM settings WHERE guildId = " + guildId);
        GuildConfigObject gco = null;
        try {
            rs.next();
            gco = new GuildConfigObject(rs.getLong(1),rs.getLong(2),rs.getLong(3));
        } catch (Exception e) {
            logger.error(e);
        }
        return gco;
    }

    public void UpdateConfig(GuildConfigObject configObject) {
        ExecuteUpdate("UPDATE settings SET welcomeChannel = " + configObject.getWelcomeChannel() + ", rulesChannel = " +
                configObject.getRulesChannel() + " WHERE guildId = " + configObject.getGuildId());
    }

    public int GetMemberBalance(long discordId) {
        CachedRowSet row = ExecuteQuery("SELECT * FROM economy WHERE discordId = " + discordId);
        if (row.size() != 1) return 0;
        try {
            row.next();
            return row.getInt(2);
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    public void UpdateMemberBalance(long discordId, int amount) {
        ExecuteUpdate("INSERT INTO economy (discordId, balance, dailyRewardLastEpoch) VALUES (" + discordId +
                ", " + amount + ", "  + "0) ON CONFLICT (discordId) DO UPDATE SET balance=" + amount+ ";");
    }




    private Connection GetConnection() throws SQLException {
        if (sqLiteConnector == null) return null;
        return DriverManager.getConnection("jdbc:sqlite:guilds.db");
    }
    public static void main(String[] args) {
        SQLiteConnector t = new SQLiteConnector();
        t.GetMemberBalance(1);
        t.UpdateMemberBalance(1, 5);
    }
}
