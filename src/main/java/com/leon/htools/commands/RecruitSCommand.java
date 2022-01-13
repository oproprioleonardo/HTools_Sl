package com.leon.htools.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.SlashCommand;
import com.leon.htools.cache.RecruitCacheProcess;
import com.leon.htools.config.Config;
import com.leon.htools.exceptions.CommandException;
import com.leon.htools.internal.buttons.CancelRecruitButton;
import com.leon.htools.internal.buttons.ConfirmRecruitButton;
import com.leon.htools.internal.entities.RecruitmentImpl;

import com.leon.htools.utils.TemplateMessage;
import com.leon.htools.utils.TemplateMessages;
import com.leon.htools.utils.validators.ArgsValidator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;
import java.util.UUID;

@Singleton
public class RecruitSCommand implements SlashCommand {

    @Inject
    private Config config;
    @Inject
    private RecruitCacheProcess cache;
    @Inject
    private ConfirmRecruitButton confirmRecruitButton;
    @Inject
    private CancelRecruitButton cancelRecruitButton;

    @Override
    public void performCommand(SlashCommandEvent event, Member member, TextChannel channel) {
        event.deferReply().queue();
        final Guild guild = Objects.requireNonNull(event.getGuild());
        if (guild.getIdLong() != config.getRecruitmentGuildId() || member == null) return;
        if (member.getRoles().stream().anyMatch(role -> config.getAdminRole() == role.getIdLong())) {
            final String id = Objects.requireNonNull(event.getOption("id")).getAsString();
            final String nickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();
            final String gamemode = Objects.requireNonNull(event.getOption("gamemode")).getAsString();

            try {
                ArgsValidator.throwIfNicknameIsWrong(nickname);
                ArgsValidator.throwIfGamemodeIsNull(gamemode, config);
            } catch (CommandException exception) {
                event.getHook().editOriginal("").setEmbeds(exception.getMessageEmbed()).queue();
                return;
            }
            final User sender = event.getUser();
            event.getJDA().retrieveUserById(id).onErrorMap(throwable -> {
                event.getHook().editOriginal("").setEmbeds(TemplateMessage.USER_NOT_FOUND.embed()).queue();
                return sender;
            }).queue(target -> {
                if (target.getId().equalsIgnoreCase(sender.getId())) return;
                final RecruitmentImpl r = new RecruitmentImpl();
                r.setApplicantId(target.getIdLong());
                r.setOperatorId(sender.getIdLong());
                r.setGamemode(gamemode);
                r.setNickname(nickname);
                final UUID uuid = cache.add(r);
                event.getHook().editOriginal(TemplateMessages.infoAttached(r, config))
                 .setActionRow(
                         confirmRecruitButton.getComponent(uuid.toString()),
                         cancelRecruitButton.getComponent(uuid.toString())
                 ).queue();
            });
        } else event.getHook().deleteOriginal().queue();
    }
}
