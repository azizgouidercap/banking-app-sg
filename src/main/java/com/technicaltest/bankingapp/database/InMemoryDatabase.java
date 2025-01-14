package com.technicaltest.bankingapp.database;

import com.technicaltest.bankingapp.model.Entity;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class InMemoryDatabase {

    private static final Map<Class<?>, Map<Long, ?>> database = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> Map<Long, T> getCollection(Class<T> entityType) {
        return (Map<Long, T>) database.computeIfAbsent(entityType, k -> new HashMap<>());
    }
}
