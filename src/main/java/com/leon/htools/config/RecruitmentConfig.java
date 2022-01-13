package com.leon.htools.config;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentConfig {

    private long timeToAliveInvite = 10080;
    private long intervieweeRoleId;
    private long channelInviteId;
    private long teamChannelId;
    private long logInviteChannelId;
    private long studiosChannelId;
    private long logChannelId;
    private long acceptedRoleId;
    private long avaliationRoleId;
    private long suppoterChannelId;
    private Set<Gamemode> gamemodes = Sets.newHashSet();

}
