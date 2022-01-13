package com.leon.htools.internal.repos;

import com.google.inject.Singleton;
import com.leon.htools.api.repos.RecruitRepository;
import com.leon.htools.database.JpaRepository;
import com.leon.htools.internal.entities.RecruitmentImpl;
import io.smallrye.mutiny.Uni;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
public class RecruitRepositoryImpl extends JpaRepository<RecruitmentImpl, Long> implements RecruitRepository {

    public RecruitRepositoryImpl() {
        super(RecruitmentImpl.class);
    }

    @Override
    public Uni<RecruitmentImpl> readByUserId(long userId) {
        return getSessionFactory().withTransaction((ss, tr) -> {
            final CriteriaBuilder builder = getSessionFactory().getCriteriaBuilder();
            final CriteriaQuery<RecruitmentImpl> query = builder.createQuery(getTarget());
            final Root<RecruitmentImpl> root = query.from(getTarget());
            query.select(root).where(
                    builder.equal(root.get("applicantId"), userId),
                    builder.equal(root.get("finalized"), false)
            );
            return ss.createQuery(query).getSingleResultOrNull();
        });
    }

    @Override
    public Uni<RecruitmentImpl> readByNickname(String nickname) {
        return getSessionFactory().withTransaction((ss, tr) -> {
            final CriteriaBuilder builder = getSessionFactory().getCriteriaBuilder();
            final CriteriaQuery<RecruitmentImpl> query = builder.createQuery(getTarget());
            final Root<RecruitmentImpl> root = query.from(getTarget());
            query.select(root).where(
                    builder.equal(root.get("nickname"), nickname),
                    builder.equal(root.get("finalized"), false)
            );
            return ss.createQuery(query).getSingleResultOrNull();
        });
    }
}
