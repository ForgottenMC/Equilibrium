package net.forgottenmc.equilibrium;


import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.forgottenmc.equilibrium.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {


    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

    }



    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        Guild guild = event.getGuild();
        String prefix = Prefix.PREFIXES.computeIfAbsent(guild.getIdLong(), DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && event.getMember().getRoles().contains(event.getGuild().getRolesByName("Leader", true).get(0))) {
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}