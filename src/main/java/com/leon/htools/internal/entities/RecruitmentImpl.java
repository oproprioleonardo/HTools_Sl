package com.leon.htools.internal.entities;

import com.leon.htools.RecruitStatus;
import com.leon.htools.api.entities.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity(name = "Recruitment")
@Table(name = "recruitments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentImpl implements Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long operatorId;
    private Long applicantId;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private RecruitStatus stage = RecruitStatus.REQUIRED_PRESENCE;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar date;
    private String gamemode;
    private String invitationUrl;
    private boolean finalized = false;

}
