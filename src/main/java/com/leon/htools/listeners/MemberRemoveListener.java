package com.leon.htools.listeners;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.LogManager;
import com.leon.htools.RecruitStatus;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.cache.RecruitCacheProcess;
import com.leon.htools.config.Config;
import com.leon.htools.config.Gamemode;
import com.leon.htools.internal.entities.RecruitmentImpl;
import io.smallrye.mutiny.Uni;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Singleton
public class MemberRemoveListener extends ListenerAdapter {

    @Inject
    private RecruitService service;
    @Inject
    private Config config;
    @Inject
    private LogManager logManager;
    @Inject
    private JDA jda;

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        final User user = event.getUser();
        final Guild guild = event.getGuild();
        if (guild.getIdLong() != config.getRecruitmentGuildId()) return;
        logManager.logLeaveEvent(user);
        this.service
                .readByUserId(user.getIdLong())
                .onItem().ifNotNull()
                .call(r -> {
                    final Gamemode gamemode = config.getGamemode(r.getGamemode());
                    final Category category = Objects.requireNonNull(guild.getCategoryById(gamemode.getCategoryId()));
                    if (!r.isFinalized() && r.getStage() != RecruitStatus.READY_TO_WORK) {
                        category.getTextChannels()
                                .stream()
                                .filter(channel -> channel.getName()
                                                          .equalsIgnoreCase(r.getNickname() + "-" + r.getId()))
                                .findFirst()
                                .ifPresent(channel -> channel.delete().queue());
                        r.setStage(RecruitStatus.ABANDONED);
                        r.setFinalized(true);
                    }
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
