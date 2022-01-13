package com.leon.htools.internal.buttons.invite;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.ButtonPattern;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CancelProcessButton implements ButtonPattern {

    @Inject
    private AcceptButton acceptButton;
    @Inject
    private RefuseButton refuseButton;

    @Override
    public @NotNull String getId() {
        return CANCEL + SEPARATOR + "invitation" + SEPARATOR + "process" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.danger(getId() + code, "Voltar atrás");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final ButtonInteraction inter = event.getInteraction();
        final String[] strings = event.getButton().getId().split(SEPARATOR);

        final String code = strings[3];

        inter.deferEdit().setActionRow(
                acceptButton.getComponent(code),
                refuseButton.getComponent(code)
        ).queue();
    }
}
