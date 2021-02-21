package xyz.iconc.dev.Bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotTools {
    private static Logger logger = LogManager.getLogger(BotTools.class);

    public static void GetMessages(TextChannel channel, int number, Consumer<List<Message>> callback) {
        if (number > 1000) return;
        List<Message> messages = new ArrayList<>(1000);
        channel.getIterableHistory().cache(false).forEachAsync((message) ->
        {
            messages.add(message);
            return messages.size() < number;
        }).thenRun(() -> callback.accept(messages));

    }

    public static void PurgeMessages(TextChannel channel, int number) {
        GetMessages(channel, number % 1000, channel::purgeMessages);
        number -= number % 1000;
        while (number > 0) {
            int finalNumber = number;
            GetMessages(channel, 1000, (messages) -> {
                logger.info(messages.size());
                if (messages.size() < finalNumber) {
                    channel.purgeMessages(messages);
                    return;
                }
                channel.purgeMessages(messages);
            });
            number -= 1000;
        }
    }
}
