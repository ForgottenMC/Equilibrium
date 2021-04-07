package net.forgottenmc.equilibrium.objects;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.forgottenmc.equilibrium.Prefix;
import net.forgottenmc.equilibrium.database.DatabaseManager;

import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final String prefix;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
        this.prefix = Prefix.PREFIXES.computeIfAbsent(event.getGuild().getIdLong(), DatabaseManager.INSTANCE::getPrefix);
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
