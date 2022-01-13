package com.leon.htools.internal.buttons.invite;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.utils.TemplateMessage;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ConfirmRefuseButton implements ButtonPattern {

    @Inject
    private LogManager logManager;
    @Inject
    private RecruitService service;

    @Override
    public @NotNull String getId() {
        return CONFIRM + SEPARATOR + "invitation" + SEPARATOR + "process" + SEPARATOR + "false" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Confirmar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final long id = Long.parseLong(strings[4]);
        event.getInteraction().deferEdit().queue();
        event.getMessage().delete().queue();
        event.getChannel().sendMessageEmbeds(TemplateMessage.REFUSE_INVITATION.embed()).queue();
        service.read(id).call((r) -> {
            r.setStage(RecruitStatus.INVITATION_REFUSED);
            logManager.logInviteRefused(r, event.getUser());
            r.setFinalized(true);
            return service.update(r);
        }).subscribe().with(value -> System.out.println("success: " + value));
    }
}
