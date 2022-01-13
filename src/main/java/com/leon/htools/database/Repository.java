package com.leon.htools.database;

import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<O, T> {

    Uni<Void> commit(O obj);

    Uni<O> read(T id);

    Uni<O> update(O obj);

    Uni<Void> delete(O obj);

    Uni<O> deleteById(T id);

    Uni<List<O>> findAll();

    Uni<List<O>> findAll(Predicate<O> predicate);

    Uni<Boolean> exists(T id);

    Class<O> getTarget();

}
