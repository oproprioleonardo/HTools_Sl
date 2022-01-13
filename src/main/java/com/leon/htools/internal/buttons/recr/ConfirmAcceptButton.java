package com.leon.htools.internal.buttons.recr;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.internal.buttons.AlrightButton;
import com.leon.htools.utils.TemplateMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.managers.channel.attribute.IPermissionContainerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Singleton
public class ConfirmAcceptButton implements ButtonPattern {

    @Inject
    private LogManager logManager;
    @Inject
    private RecruitService service;
    @Inject
    private JDA jda;
    @Inject
    private Config config;
    @Inject
    private AlrightButton alrightButton;

    @Override
    public @NotNull String getId() {
        return CONFIRM + SEPARATOR + "team" + SEPARATOR + "true" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Confirmar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final long id = Long.parseLong(strings[3]);
        final MessageChannel channel = event.getChannel();
        final Guild guild = Objects.requireNonNull(event.getGuild());
        event.getInteraction().deferEdit().queue();
        event.getMessage().delete().queue();
        service.read(id).call((r) -> {
            r.setStage(RecruitStatus.ACCEPTED);
            jda.retrieveUserById(r.getApplicantId()).queue(user -> {
                final IPermissionContainerManager<?, ?> permissionContainer =
                        Objects.requireNonNull(guild.getGuildChannelById(channel.getId()))
                               .getPermissionContainer().getManager();
                guild.removeRoleFromMember(
                        r.getApplicantId(),
                        Objects.requireNonNull(guild.getRoleById(config.getRecruitmentSystem().getIntervieweeRoleId()))
                )
                     .queue(unused -> guild.addRoleToMember(
                             r.getApplicantId(),
                             Objects.requireNonNull(guild.getRoleById(config.getRecruitmentSystem().getAcceptedRoleId()))
                     ).queue());
                guild.retrieveMember(user)
                     .queue(member -> permissionContainer.putPermissionOverride(member, Lists.newArrayList(
                             Permission.VIEW_CHANNEL,
                             Permission.MESSAGE_HISTORY,
                             Permission.MESSAGE_SEND
                     ), Lists.newArrayList()).queue(unused -> channel.sendMessage(
                             config.getGamemode(r.getGamemode()).isStudios() ?
                             TemplateMessages.acceptedStudios(user)
                                                                             :
                             TemplateMessages.accepted(user)
                     ).setActionRow(alrightButton.getComponent(""+r.getId())).queue()));
            });
            return service.update(r);
        }).subscribe().with(value -> System.out.println("success: " + value));
    }
}
