package com.leon.htools.api.entities;

import com.leon.htools.RecruitStatus;

import java.io.Serializable;
import java.util.Calendar;

public interface Recruitment extends Serializable {

    Long getId();

    void setId(Long id);

    Long getOperatorId();

    void setOperatorId(Long id);

    Long getApplicantId();

    void setApplicantId(Long id);

    String getNickname();

    void setNickname(String nickname);

    RecruitStatus getStage();

    void setStage(RecruitStatus stage);

    String getGamemode();

    void setGamemode(String id);

    Calendar getDate();

    void setDate(Calendar time);

    default String getApplicantAsMention() {
        return "<@" + getApplicantId() + ">";
    }

    String getInvitationUrl();

    void setInvitationUrl(String url);

    boolean isFinalized();

    void setFinalized(boolean value);


}
