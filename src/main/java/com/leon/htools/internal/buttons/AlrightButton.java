package com.leon.htools.internal.buttons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.config.Gamemode;
import com.leon.htools.config.RecruitmentConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.IInviteContainer;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Singleton
public class AlrightButton implements ButtonPattern {

    @Inject
    private LogManager logManager;
    @Inject
    private RecruitService service;
    @Inject
    private JDA jda;
    @Inject
    private Config config;

    @Override
    public @NotNull String getId() {
        return "alright" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.primary(getId() + code, "Terminar recrutamento");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final long id = Long.parseLong(strings[1]);
        final Guild guild = Objects.requireNonNull(event.getGuild());
        final RecruitmentConfig recConfig = config.getRecruitmentSystem();
        final MessageChannel channel = event.getChannel();
        event.getInteraction().deferEdit().queue();
        guild.retrieveMember(event.getUser()).queue(member -> {
            if (member.getRoles().stream().anyMatch(role -> config.getAdminRole() == role.getIdLong())) {
                Objects.requireNonNull(guild.getGuildChannelById(channel.getId())).delete().queue();
                service.read(id)
                       .call((r) -> {
                           jda.retrieveUserById(r.getApplicantId())
                              .queue(user -> {
                                  final Gamemode gamemode = config.getGamemode(r.getGamemode());
                                  final long channelId =
                                          gamemode.isStudios() ? recConfig.getStudiosChannelId() :
                                          gamemode.isSupporter() ? recConfig.getSuppoterChannelId() :
                                          recConfig.getTeamChannelId();
                                  final GuildChannel
                                          guildChannel = Objects.requireNonNull(jda.getGuildChannelById(channelId));
                                  ((IInviteContainer) guildChannel).createInvite()
                                                                   .setUnique(true)
                                                                   .setMaxUses(1)
                                                                   .setMaxAge(recConfig.getTimeToAliveInvite(), TimeUnit.MINUTES)
                                                                   .queue(invite -> user.openPrivateChannel()
                                                                                        .queue(privateChannel ->
                                                                                                       privateChannel
                                                                                                               .sendMessage(
                                                                                                                       "|| " +
                                                                                                                       invite.getUrl()
                                                                                                                             .replace(
                                                                                                                                     "https://",
                                                                                                                                     ""
                                                                                                                             ) + " ||")
                                                                                                               .queue()
                                                                                        ));
                              });

                           r.setStage(RecruitStatus.READY_TO_WORK);
                           return service.update(r);
                       })
                       .invoke((r) -> guild.kick(r.getApplicantId().toString()).queue())
                       .subscribe()
                       .with(value -> System.out.println("success: " + value));
            }
        });
    }
}
