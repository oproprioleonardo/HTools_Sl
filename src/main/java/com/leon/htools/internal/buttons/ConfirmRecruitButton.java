package com.leon.htools.internal.buttons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.cache.RecruitCacheProcess;
import com.leon.htools.config.Config;
import com.leon.htools.config.RecruitmentConfig;
import com.leon.htools.internal.buttons.invite.AcceptButton;
import com.leon.htools.internal.buttons.invite.RefuseButton;
import com.leon.htools.internal.entities.RecruitmentImpl;
import com.leon.htools.utils.TemplateMessage;
import com.leon.htools.utils.TemplateMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class ConfirmRecruitButton implements ButtonPattern {

    @Inject
    private RecruitCacheProcess cache;
    @Inject
    private RecruitService service;
    @Inject
    private LogManager logManager;
    @Inject
    private Config config;
    @Inject
    private JDA jda;
    @Inject
    private AcceptButton acceptButton;
    @Inject
    private RefuseButton refuseButton;

    @Override
    public @NotNull String getId() {
        return CONFIRM + SEPARATOR + "recruitment" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Enviar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final ButtonInteraction inter = event.getInteraction();
        final Guild guild = Objects.requireNonNull(event.getGuild());
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final UUID uuid = UUID.fromString(strings[2]);
        final MessageChannel channel = event.getChannel();
        inter.deferEdit().queue();
        guild.retrieveMember(event.getUser()).queue(member -> {
            if (member.getRoles().stream().anyMatch(role -> config.getAdminRole() == role.getIdLong())) {
                final RecruitmentImpl r = ((RecruitmentImpl) cache.get(uuid));
                event.getMessage().delete().queue();
                final Guild refGuild = Objects.requireNonNull(jda.getGuildById(config.getReferenceGuildId()));
                jda.retrieveUserById(r.getApplicantId())
                   .queue(
                           user -> refGuild.retrieveMember(user).queue(target -> {
                               if (target.getRoles()
                                         .stream()
                                         .anyMatch(role -> role.getIdLong() == config.getVerifiedRoleId())) {
                                   this.sendInvitation(user, r, channel);
                               } else logManager.logFailInvite(r, channel, true);
                           }, throwable -> logManager.logFailInvite(r, channel, false)),
                           throwable -> logManager.logFailInvite(r, channel, false)
                   );
                this.cache.remove(uuid);
            }
        });
    }

    public void sendInvitation(
            User user, RecruitmentImpl rec,
            MessageChannel originalCallChannel) {
        final RecruitmentConfig rCfg = config.getRecruitmentSystem();
        user.openPrivateChannel()
            .queue(channel -> Objects
                    .requireNonNull(jda.getTextChannelById(rCfg.getChannelInviteId()))
                    .createInvite()
                    .setUnique(true)
                    .setMaxUses(1)
                    .setMaxAge(rCfg.getTimeToAliveInvite(), TimeUnit.MINUTES)
                    .queue(invite -> {
                        rec.setInvitationUrl(invite.getUrl());
                        channel.sendMessage(TemplateMessages.invitationReceived(rec, config))
                               .queue(
                                       message -> this.service.create(rec)
                                                              .onFailure()
                                                              .invoke(() -> {
                                                                  invite.delete().queue();
                                                                  channel.sendMessageEmbeds(
                                                                          TemplateMessage.INTERNAL_ERROR.embed())
                                                                         .queue();
                                                              })
                                                              .onItem()
                                                              .invoke(() -> message
                                                                      .editMessage(TemplateMessages.invitationReceived(
                                                                              rec,
                                                                              config
                                                                      ))
                                                                      .setActionRow(
                                                                              acceptButton.getComponent("" + rec.getId()),
                                                                              refuseButton.getComponent("" + rec.getId())
                                                                      )
                                                                      .queue(
                                                                              (ignored) -> logManager
                                                                                      .logSucessInvite(
                                                                                              rec,
                                                                                              originalCallChannel
                                                                                      )
                                                                      ))
                                                              .subscribe()
                                                              .with(value -> System.out.println("success: " + value)),
                                       throwable -> logManager.logFailInvite(rec, originalCallChannel, false)
                               );
                    }));
    }


}
