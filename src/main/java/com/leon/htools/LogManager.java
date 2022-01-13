package com.leon.htools;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.config.Config;
import com.leon.htools.internal.entities.RecruitmentImpl;
import com.leon.htools.utils.TemplateMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Singleton
public class LogManager {

    @Inject
    private Config config;
    @Inject
    private JDA jda;

    public void logFailInvite(RecruitmentImpl rec, MessageChannel channel, boolean hasOpenPrivateChat) {
        if (!hasOpenPrivateChat) {
            channel.sendMessageEmbeds(TemplateMessage.INVITATION_ERROR.embed())
                   .queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
        } else {
            final EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Algo de errado não está certo...")
                   .setColor(new Color(212, 105, 105))
                   .setThumbnail(
                           "https://media.discordapp.net/attachments/807229304156061716/883389245455237161/729059998562254881.png")
                   .appendDescription(
                           "O candidato `" + rec.getNickname() + "` não se encontra vinculado.\n")
                   .appendDescription("É necessário que o candidato se vincule para o recrutamento.");
            channel.sendMessageEmbeds(builder.build())
                   .queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
        }
    }

    public void logSucessInvite(RecruitmentImpl rec, MessageChannel channel) {
        final EmbedBuilder builder = new EmbedBuilder();
        builder
                .setColor(new Color(100, 236, 113))
                .appendDescription("<:aprovado:882018256657985587> **Processo iniciado com sucesso!**\n")
                .appendDescription("\n")
                .appendDescription(
                        "O convite do servidor será enviado para o usuário `" + rec.getNickname() +
                        "`.\n")
                .appendDescription("Fique atento às atualizações decorrentes do processo.");
        channel.sendMessageEmbeds(builder.build()).allowedMentions(Lists.newArrayList(Message.MentionType.EMOTE))
               .queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
    }

    public void logInviteExpired(RecruitmentImpl rec, User user) {
        final TextChannel channel =
                Objects.requireNonNull(jda.getTextChannelById(config.getRecruitmentSystem().getLogInviteChannelId()));

        final EmbedBuilder builder = new EmbedBuilder();
        builder
                .setColor(new Color(47, 49, 54))
                .appendDescription("**Logs - Recrutamento**\n")
                .appendDescription("\n")
                .appendDescription("> Informações: `" + user.getAsTag() + "` - " + rec.getNickname() + ".\n")
                .appendDescription("> **Status**: Convite expirado.");

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    public void logInviteRefused(RecruitmentImpl rec, User user) {
        final TextChannel channel =
                Objects.requireNonNull(jda.getTextChannelById(config.getRecruitmentSystem().getLogInviteChannelId()));

        final EmbedBuilder builder = new EmbedBuilder();
        builder
                .setColor(new Color(47, 49, 54))
                .appendDescription("**Logs - Recrutamento**\n")
                .appendDescription("\n")
                .appendDescription("> Informações: `" + user.getAsTag() + "` - " + rec.getNickname() + ".\n")
                .appendDescription("> **Status**: Convite negado.");

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    public void logJoinEvent(User user) {
        final TextChannel channel =
                Objects.requireNonNull(jda.getTextChannelById(config.getRecruitmentSystem().getLogChannelId()));

        final EmbedBuilder builder = new EmbedBuilder();
        builder
                .setColor(new Color(47, 49, 54))
                .appendDescription("**Logs - Recrutamento**\n")
                .appendDescription("\n")
                .appendDescription("> Informações: " + user.getAsMention() + ".\n")
                .appendDescription("> **Status**: Entrou no servidor.");

        channel.sendMessageEmbeds(builder.build())
               .allowedMentions(Collections.singleton(Message.MentionType.USER))
               .queue();
    }

    public void logLeaveEvent(User user) {
        final TextChannel channel =
                Objects.requireNonNull(jda.getTextChannelById(config.getRecruitmentSystem().getLogChannelId()));

        final EmbedBuilder builder = new EmbedBuilder();
        builder
                .setColor(new Color(47, 49, 54))
                .appendDescription("**Logs - Recrutamento**\n")
                .appendDescription("\n")
                .appendDescription("> Informações: `" + user.getAsTag() + "`.\n")
                .appendDescription("> **Status**: Saiu do servidor.");

        channel.sendMessageEmbeds(builder.build()).queue();
    }

}
