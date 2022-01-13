package com.leon.htools;

import lombok.Getter;

@Getter
public enum RecruitStatus {

    REQUIRED_PRESENCE("Presença requerida"),
    EXPIRED("Tempo expirado"),
    INVITATION_REFUSED("Convite recusado"),
    WAIT_CHECKING("Aguardando verificação..."),
    CHECKED("Verificado"),
    ABANDONED("Abandonou o recrutamento"),
    INTERVIEW_DONE("Entrevistado"),
    ACCEPTED("Aprovado pela equipe"),
    FORCED_REFUSED("Recusado pela equipe"),
    READY_TO_WORK("Pronto para trabalhar");

    private final String label;

    RecruitStatus(String label) {
        this.label = label;
    }

}
