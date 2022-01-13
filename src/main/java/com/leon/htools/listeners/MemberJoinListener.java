package com.leon.htools.listeners;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.config.Config;
import com.leon.htools.config.Gamemode;
import com.leon.htools.internal.buttons.NextStepButton;
import com.leon.htools.internal.entities.RecruitmentImpl;
import com.leon.htools.utils.TemplateMessages;
import io.smallrye.mutiny.Uni;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Singleton
public class MemberJoinListener extends ListenerAdapter {

    @Inject
    private RecruitService service;
    @Inject
    private Config config;
    @Inject
    private LogManager logManager;
    @Inject
    private JDA jda;
    @Inject
    private NextStepButton nextStepButton;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final User user = event.getUser();
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        if (guild.getIdLong() == config.getRecruitmentGuildId()) {
            logManager.logJoinEvent(user);
            this.service
                    .readByUserId(user.getIdLong())
                    .onItem().ifNotNull()
                    .call(r -> {
                        final Gamemode gamemode = config.getGamemode(r.getGamemode());
                        final Category category =
                                Objects.requireNonNull(guild.getCategoryById(gamemode.getCategoryId()));
                        guild.addRoleToMember(member, Objects.requireNonNull(jda.getRoleById(gamemode.getRoleId())))
                             .queueAfter(3, TimeUnit.MINUTES, unused -> {
                             }, throwable -> {
                             });
                        member.modifyNickname(r.getNickname()).queue();
                        category
                                .createTextChannel(r.getNickname() + "-" + r.getId())
                                .queue(channel -> channel
                                        .sendMessage(TemplateMessages.controlPanel(r, config))
                                        .setActionRow(nextStepButton.getComponent("" + r.getId()))
                                        .queue()
                                );
                        r.setStage(RecruitStatus.CHECKED);
                        return Uni.createFrom().item(Lists.newArrayList(r));
                    })
                    .onFailure().recoverWithNull()
                    .chain(r -> {
                        if (r != null) return service.update(r);
                        else return Uni.createFrom().voidItem();
                    })
                    .subscribe().with(value -> System.out.println("success: " + value));
        } else if (guild.getIdLong() == config.getTeamGuildId()) {
            this.service
                    .readByUserId(user.getIdLong())
                    .onItem().ifNotNull()
                    .call(r -> {
                        guild.addRoleToMember(
                                member,
                                Objects.requireNonNull(jda.getRoleById(config.getRecruitmentSystem()
                                                                             .getAvaliationRoleId()))
                        ).queueAfter(5, TimeUnit.SECONDS, unused -> member.modifyNickname(r.getNickname()).queue());
                        r.setFinalized(true);
                        return Uni.createFrom().item(Lists.newArrayList(r));
                    })
                    .onFailure().recoverWithNull()
                    .chain(r -> {
                        if (r != null) return service.update(r);
                        else return Uni.createFrom().voidItem();
                    })
                    .subscribe().with(value -> System.out.println("success: " + value));
        }
    }
}
