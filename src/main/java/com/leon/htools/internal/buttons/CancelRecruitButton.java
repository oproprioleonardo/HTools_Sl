package com.leon.htools.internal.buttons;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.cache.RecruitCacheProcess;
import com.leon.htools.config.Config;
import com.leon.htools.utils.TemplateMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class CancelRecruitButton implements ButtonPattern {

    @Inject
    private RecruitCacheProcess cache;
    @Inject
    private Config config;

    @Override
    public @NotNull String getId() {
        return CANCEL + SEPARATOR + "recruitment" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.danger(getId() + code, "Cancelar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final ButtonInteraction inter = event.getInteraction();
        final Guild guild = Objects.requireNonNull(event.getGuild());
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final UUID uuid = UUID.fromString(strings[2]);
        inter.deferEdit().queue();
        guild.retrieveMember(event.getUser()).queue(member -> {
            if (member.getRoles().stream().anyMatch(role -> config.getAdminRole() == role.getIdLong())) {
                cache.remove(uuid);
                event.getMessage().delete().queue();
                event.getChannel().sendMessageEmbeds(TemplateMessage.INFO_CANCELED.embed())
                     .allowedMentions(Lists.newArrayList(Message.MentionType.EMOTE))
                     .queue((rs) -> rs.delete().queueAfter(30, TimeUnit.SECONDS));
            }
        });
    }
}
