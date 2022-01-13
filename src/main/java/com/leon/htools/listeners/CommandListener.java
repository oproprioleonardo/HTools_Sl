package com.leon.htools.listeners;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.SlashCommand;
import com.leon.htools.commands.RecruitSCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Singleton
public class CommandListener extends ListenerAdapter {

    private final Map<String, SlashCommand> commandMap = Maps.newHashMap();

    @Inject
    public CommandListener(RecruitSCommand recruitSCommand) {
        this.commandMap.put("recrutar", recruitSCommand);
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        this.commandMap.keySet().stream()
                       .filter(s -> s.equalsIgnoreCase(event.getCommandString()))
                       .findFirst()
                       .ifPresent(s -> this.commandMap.get(s)
                                                      .performCommand(event,
                                                                      event.getMember(),
                                                                      event.getTextChannel()
                                                      ));
    }
}
