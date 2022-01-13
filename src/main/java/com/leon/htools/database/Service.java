package com.leon.htools.database;

import io.smallrye.mutiny.Uni;

public interface Service<O, I> {

    Repository<O, I> getRepository();

    default Uni<Void> create(O object) {
        return this.getRepository().commit(object);
    }

    default Uni<O> read(I id) {
        return this.getRepository().read(id);
    }

    default Uni<O> update(O object) {
        return this.getRepository().update(object);
    }

    default Uni<Void> delete(O object) {
        return this.getRepository().delete(object);
    }

    default Uni<O> deleteById(I id) {
        return this.getRepository().deleteById(id);
    }

    default Uni<Boolean> exists(I id) {
        return this.getRepository().exists(id);
    }

}
