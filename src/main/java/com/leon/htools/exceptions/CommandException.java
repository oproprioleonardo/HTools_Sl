package com.leon.htools.exceptions;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CommandException extends Exception {

    private MessageEmbed messageEmbed;

    public CommandException(MessageEmbed messageEmbed) {
        this.messageEmbed = messageEmbed;
    }

    public CommandException() {
    }

    public void throwMessage(MessageChannel channel) {
        channel.sendMessageEmbeds(this.messageEmbed).complete().delete()
               .queueAfter(15, TimeUnit.SECONDS);
    }
}
