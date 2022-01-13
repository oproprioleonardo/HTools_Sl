package com.leon.htools.internal.buttons.recr;

import com.google.inject.Inject;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.utils.TemplateMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class ConfirmRefuseButton implements ButtonPattern {

    @Inject
    private LogManager logManager;
    @Inject
    private RecruitService service;
    @Inject
    private JDA jda;

    @Override
    public @NotNull String getId() {
        return CONFIRM + SEPARATOR + "team" + SEPARATOR + "false" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Confirmar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final long id = Long.parseLong(strings[3]);
        final Guild guild = Objects.requireNonNull(event.getGuild());
        event.getInteraction().deferEdit().queue();
        Objects.requireNonNull(guild.getGuildChannelById(event.getChannel().getId())).delete().queue();
        service.read(id).call((r) -> {
            jda.retrieveUserById(r.getApplicantId())
               .queue(user -> user.openPrivateChannel()
                                  .queue(privateChannel -> privateChannel.sendMessage(
                                          TemplateMessages.refused(user)
                                  ).allowedMentions(Collections.singleton(Message.MentionType.USER))
                                                                         .queue(message1 -> guild.kick(r.getApplicantId()
                                                                                                        .toString())
                                                                                                 .queue())));
            r.setStage(RecruitStatus.FORCED_REFUSED);
            r.setFinalized(true);
            return service.update(r);
        }).subscribe().with(value -> System.out.println("success: " + value));
    }
}
