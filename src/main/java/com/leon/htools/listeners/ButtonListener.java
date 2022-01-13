package com.leon.htools.listeners;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.ButtonPattern;
import com.leon.htools.internal.buttons.AlrightButton;
import com.leon.htools.internal.buttons.CancelRecruitButton;
import com.leon.htools.internal.buttons.ConfirmRecruitButton;
import com.leon.htools.internal.buttons.NextStepButton;
import com.leon.htools.internal.buttons.invite.*;
import com.leon.htools.internal.buttons.recr.AcceptToTeamButton;
import com.leon.htools.internal.buttons.recr.CancelToTeamButton;
import com.leon.htools.internal.buttons.recr.RefuseToTeamButton;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;

@Singleton
public class ButtonListener extends ListenerAdapter {

    private final HashSet<ButtonPattern> set = Sets.newHashSet();

    @Inject
    public ButtonListener(
            CancelRecruitButton cancelRecruitButton,
            ConfirmRecruitButton confirmRecruitButton,
            AcceptButton acceptButton,
            CancelProcessButton cancelProcessButton,
            ConfirmAcceptButton confirmAcceptButton,
            ConfirmRefuseButton confirmRefuseButton,
            RefuseButton refuseButton,
            NextStepButton nextStepButton,
            AcceptToTeamButton acceptToTeamButton,
            RefuseToTeamButton refuseToTeamButton,
            AlrightButton alrightButton,
            CancelToTeamButton cancelToTeamButton,
            com.leon.htools.internal.buttons.recr.ConfirmAcceptButton confirmAcceptButton2,
            com.leon.htools.internal.buttons.recr.ConfirmRefuseButton confirmRefuseButton2) {
        this.set.add(cancelRecruitButton);
        this.set.add(confirmRecruitButton);
        this.set.add(acceptButton);
        this.set.add(cancelProcessButton);
        this.set.add(confirmAcceptButton);
        this.set.add(confirmRefuseButton);
        this.set.add(refuseButton);
        this.set.add(nextStepButton);
        this.set.add(acceptToTeamButton);
        this.set.add(refuseToTeamButton);
        this.set.add(alrightButton);
        this.set.add(cancelToTeamButton);
        this.set.add(confirmAcceptButton2);
        this.set.add(confirmRefuseButton2);

    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        final Button button = event.getButton();
        if (button == null) return;

        this.set.stream().filter(bp -> Objects.requireNonNull(button.getId()).startsWith(bp.getId()))
                .findFirst().ifPresent(bp -> bp.action(event));

    }
}
