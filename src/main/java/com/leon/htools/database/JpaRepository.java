package com.leon.htools.database;

import com.google.inject.Inject;
import io.smallrye.mutiny.Uni;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class JpaRepository<O, T extends Serializable> implements Repository<O, T> {

    @Getter
    private final Class<O> target;
    @Inject
    @Setter(AccessLevel.PRIVATE)
    @Getter
    private Mutiny.SessionFactory sessionFactory;

    public JpaRepository(Class<O> target) {
        this.target = target;
    }

    public Uni<Void> commit(O obj) {
        return sessionFactory.withTransaction((session, transaction) -> session.persist(obj));
    }

    public Uni<O> read(T id) {
        return sessionFactory.withTransaction((session, transaction) -> session.find(getTarget(), id));
    }

    public Uni<O> update(O obj) {
        return sessionFactory.withTransaction((session, transaction) -> session.merge(obj));
    }

    public Uni<Void> delete(O obj) {
        return sessionFactory.withTransaction((session, transaction) -> session.remove(obj));
    }

    public Uni<O> deleteById(T id) {
        return read(id)
                .call(report -> sessionFactory.withTransaction((session, transaction) -> session.remove(report)));
    }

    public Uni<List<O>> findAll() {
        return sessionFactory.withTransaction(
                (session, transaction) -> session.createQuery("FROM " + target.getName(), target).getResultList());
    }

    public Uni<List<O>> findAll(Predicate<O> predicate) {
        return findAll().map(list -> list.stream().filter(predicate).collect(Collectors.toList()));
    }

    public Uni<Boolean> exists(T id) {
        return sessionFactory.withTransaction((session, transaction) -> {
            final CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
            final CriteriaQuery<O> query = builder.createQuery(getTarget());
            final Root<O> root = query.from(getTarget());
            query.select(root).where(builder.equal(root.get("id"), id));
            return session.createQuery(query).getResultList().map(List::isEmpty);
        });
    }

}
