package com.leon.htools.api;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

public interface ButtonPattern {

    String SEPARATOR = "XxxX";
    String CONFIRM = "confirm";
    String CANCEL = "cancel";
    String ACCEPT = "accept";
    String REFUSE = "refuse";
    @NotNull
    String getId();
    Component getComponent(String code);
    void action(ButtonClickEvent event);

}
