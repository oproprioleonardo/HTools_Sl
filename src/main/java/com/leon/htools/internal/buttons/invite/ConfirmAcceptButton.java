package com.leon.htools.internal.buttons.invite;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.utils.TemplateMessage;
import com.leon.htools.utils.TemplateMessages;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ConfirmAcceptButton implements ButtonPattern {

    @Inject
    private LogManager logManager;
    @Inject
    private RecruitService service;
    @Inject
    private Config config;

    @Override
    public @NotNull String getId() {
        return CONFIRM + SEPARATOR + "invitation" + SEPARATOR + "confirmation" + SEPARATOR + "true" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Confirmar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final long id = Long.parseLong(strings[4]);
        final MessageChannel channel = event.getChannel();
        event.getInteraction().deferEdit().queue();
        event.getMessage().delete().queue();
        channel.sendMessageEmbeds(TemplateMessage.GENERATING_LINK.embed())
               .queue(oldMsg -> {
                   oldMsg.delete().queue();
                   service.read(id)
                          .call((r) -> {
                              if (System.currentTimeMillis() >
                                  r.getDate().getTime().getTime() +
                                  config.getRecruitmentSystem().getTimeToAliveInvite() * 60 * 1000) {
                                  r.setStage(RecruitStatus.EXPIRED);
                                  channel.sendMessageEmbeds(TemplateMessage.INVITATION_EXPIRE.embed()).queue();
                                  logManager.logInviteExpired(r, event.getUser());
                                  r.setFinalized(true);
                              } else {
                                  channel.sendMessage("|| " + r.getInvitationUrl().replace("https://", "") + " ||")
                                         .queue();
                                  channel.sendMessage(TemplateMessages.generatedLink(r.getDate())).queue();
                                  r.setStage(RecruitStatus.WAIT_CHECKING);
                              }
                              return service.update(r);
                          }).subscribe().with(value -> System.out.println("success: " + value));
               });
    }
}
