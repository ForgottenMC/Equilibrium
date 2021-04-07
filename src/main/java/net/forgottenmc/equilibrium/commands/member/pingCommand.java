package net.forgottenmc.equilibrium.commands.member;

import net.forgottenmc.equilibrium.objects.Categories;
import net.forgottenmc.equilibrium.objects.CommandContext;
import net.forgottenmc.equilibrium.objects.ICommand;

public class pingCommand implements ICommand {
    @Override
    public void handle(CommandContext event) {
        event.getChannel().sendMessage("Pong!").queue(m -> {
            m.editMessage("Pong! the ping is " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp(CommandContext event) {
        return "Pings discord";
    }

    @Override
    public String getCat() {
        return Categories.member;
    }
}
