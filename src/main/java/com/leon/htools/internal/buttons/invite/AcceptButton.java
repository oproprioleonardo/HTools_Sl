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
public class AcceptButton implements ButtonPattern {

    @Inject
    private ConfirmAcceptButton confirmAcceptButton;
    @Inject
    private CancelProcessButton cancelProcessButton;

    @Override
    public @NotNull String getId() {
        return ACCEPT + SEPARATOR + "invitation" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.success(getId() + code, "Quero prosseguir");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final ButtonInteraction inter = event.getInteraction();
        final String[] strings = event.getButton().getId().split(SEPARATOR);

        final String code = strings[2];

        inter.deferEdit().setActionRow(
                confirmAcceptButton.getComponent(code),
                cancelProcessButton.getComponent(code)
        ).queue();

    }
}
