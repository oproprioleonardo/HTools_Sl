package com.leon.htools.internal.buttons.recr;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.ButtonPattern;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public class RefuseToTeamButton implements ButtonPattern {

    @Inject
    private ConfirmRefuseButton confirmRefuseButton;
    @Inject
    private CancelToTeamButton cancelToTeamButton;

    @Override
    public @NotNull String getId() {
        return REFUSE + SEPARATOR + "team" + SEPARATOR;
    }

    @Override
    public Component getComponent(String code) {
        return Button.danger(getId() + code, "Reprovar");
    }

    @Override
    public void action(ButtonClickEvent event) {
        final String[] strings = event.getButton().getId().split(SEPARATOR);
        final String code = strings[2];
        event.getInteraction().deferEdit().setActionRow(
                confirmRefuseButton.getComponent(code),
                cancelToTeamButton.getComponent(code)
        ).queue();
    }
}
