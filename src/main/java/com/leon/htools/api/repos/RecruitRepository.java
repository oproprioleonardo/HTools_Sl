package com.leon.htools.api.repos;

import com.leon.htools.database.Repository;
import com.leon.htools.internal.entities.RecruitmentImpl;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface RecruitRepository extends Repository<RecruitmentImpl, Long> {

    Uni<RecruitmentImpl> readByUserId(long id);

    Uni<RecruitmentImpl> readByNickname(String nickname);
}
