package com.leon.htools.utils.validators;

import com.leon.htools.config.Config;
import com.leon.htools.exceptions.CommandException;
import com.leon.htools.utils.TemplateMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ArgsValidator {

    public void throwIfNicknameIsWrong(String value) throws CommandException {
        if (value.length() <= 16 && value.length() >= 3) {
            return;
        }
        throw new CommandException(TemplateMessage.NICKNAME_LENGTH_NOT_SUPPORTED.embed());
    }

    public void throwIfGamemodeIsNull(String value, Config config) throws CommandException {
        if (config.getRecruitmentSystem().getGamemodes().stream()
                  .anyMatch(gamemode -> gamemode.getName().equals(value))) {
            return;
        }
        throw new CommandException(TemplateMessage.SERVER_NOT_FOUND.embed());
    }


}
