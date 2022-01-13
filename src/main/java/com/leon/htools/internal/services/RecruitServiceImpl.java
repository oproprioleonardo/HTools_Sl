package com.leon.htools.internal.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leon.htools.api.repos.RecruitRepository;
import com.leon.htools.api.services.RecruitService;
import com.leon.htools.internal.entities.RecruitmentImpl;
import io.smallrye.mutiny.Uni;
import lombok.Getter;

import java.util.List;

@Singleton
@Getter
public class RecruitServiceImpl implements RecruitService {

    @Inject
    private RecruitRepository repository;

    @Override
    public Uni<RecruitmentImpl> readByUserId(long id) {
        return this.repository.readByUserId(id);
    }

    @Override
    public Uni<RecruitmentImpl> readByNickname(String nickname) {
        return this.repository.readByNickname(nickname);
    }
}
