package com.leon.htools.internal.buttons.recr;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.ButtonPattern;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CancelToTeamButton implements ButtonPattern {

    @Inject
    private AcceptToTeamButton acceptToTeamButton;
    @Inject
    private RefuseToTeamButton refuseToTeamButton;

    @Override
    public @NotNull String getId() {
        return CANCEL + SEPARATOR + "team" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.danger(getId() + code, "Voltar atrás");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final String code = strings[2];
        event.getInteraction().deferEdit().setActionRow(
                acceptToTeamButton.getComponent(code),
                refuseToTeamButton.getComponent(code)
        ).queue();
    }
}
