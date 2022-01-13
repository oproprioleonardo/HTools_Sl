package com.leon.htools.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommand {

    void performCommand(SlashCommandEvent event, Member member, TextChannel channel);

}
