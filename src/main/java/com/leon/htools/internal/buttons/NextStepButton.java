package com.leon.htools.internal.buttons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.config.Gamemode;
import com.leon.htools.internal.buttons.recr.AcceptToTeamButton;
import com.leon.htools.internal.buttons.recr.RefuseToTeamButton;
import com.leon.htools.utils.TemplateMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Singleton
public class NextStepButton implements ButtonPattern {

    @Inject
    private RecruitService service;
    @Inject
    private Config config;
    @Inject
    private AcceptToTeamButton acceptToTeamButton;
    @Inject
    private RefuseToTeamButton refuseToTeamButton;

    @Override
    public @NotNull String getId() {
        return "next" + SEPARATOR + "step" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.primary(getId() + code, "Entrevistado");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final Guild guild = Objects.requireNonNull(event.getGuild());
        final long id = Long.parseLong(strings[2]);
        event.getInteraction().deferEdit().queue();
        guild.retrieveMember(event.getUser()).queue(member -> {
            if (member.getRoles().stream().anyMatch(role -> config.getAdminRole() == role.getIdLong())) {
                service.read(id).call((r) -> {
                    r.setStage(RecruitStatus.INTERVIEW_DONE);
                    final Gamemode gamemode = config.getGamemode(r.getGamemode());
                    guild.removeRoleFromMember(
                            r.getApplicantId(),
                            Objects.requireNonNull(guild.getRoleById(gamemode.getRoleId()))
                    )
                         .queue(unused -> guild.addRoleToMember(
                                 r.getApplicantId(),
                                 Objects.requireNonNull(guild.getRoleById(config.getRecruitmentSystem()
                                                                                .getIntervieweeRoleId()))
                         )
                                               .queue());

                    event.getHook().editOriginal(TemplateMessages.controlPanel(r, config))
                         .setActionRow(
                                 acceptToTeamButton.getComponent("" + id),
                                 refuseToTeamButton.getComponent("" + id)
                         ).queue();
                    return service.update(r);
                }).subscribe().with(value -> System.out.println("success: " + value));
            }
        });

    }
}
