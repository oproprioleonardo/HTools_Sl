package com.leon.htools.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Singleton;
import com.leon.htools.api.entities.Recruitment;

import java.util.UUID;

@Singleton
public class RecruitCacheProcess {

    private final Cache<UUID, Recruitment> cache =
            Caffeine.newBuilder()
                    .maximumSize(200)
                    .build();

    public UUID add(Recruitment recruitment) {
        final UUID uuid = UUID.randomUUID();
        this.cache.put(uuid, recruitment);
        return uuid;
    }

    public void remove(UUID uuid) {
        this.cache.invalidate(uuid);
    }

    public Recruitment get(UUID uuid) {
        return this.cache.getIfPresent(uuid);
    }

}
