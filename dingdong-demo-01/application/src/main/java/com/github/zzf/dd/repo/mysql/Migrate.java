package com.github.zzf.dd.repo.mysql;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public interface Migrate<T> {

    default Stream<T> migrate(
            Supplier<T> v1Func,
            Supplier<T> v2Func,
            boolean useV2,
            boolean check,
            Executor executor,
            Function<T, String> identifier
    ) {
        if (!useV2) {
            T v1Data = v1Func.get();
            if (check) {
                doCheck(new ArrayList<>(v1Data), null, v1Func, v2Func, executor, identifier);
            }
            return v1Data.stream();
        } else {
            List<T> v2Data = v2Func.get();
            if (check) {
                doCheck(null, new ArrayList<>(v2Data), v1Func, v2Func, executor, identifier);
            }
            return v2Data.stream();
        }
    }

    default <T> Stream<T> migrateList(
            Supplier<List<T>> v1Func,
            Supplier<List<T>> v2Func,
            boolean useV2,
            boolean check,
            Executor executor,
            Function<T, String> identifier
    ) {
        if (!useV2) {
            List<T> v1Data = v1Func.get();
            if (check) {
                doCheck(new ArrayList<>(v1Data), null, v1Func, v2Func, executor, identifier);
            }
            return v1Data.stream();
        } else {
            List<T> v2Data = v2Func.get();
            if (check) {
                doCheck(null, new ArrayList<>(v2Data), v1Func, v2Func, executor, identifier);
            }
            return v2Data.stream();
        }
    }

    private <T> void doCheck(
            List<T> v1,
            List<T> v2,
            Supplier<List<T>> v1Func,
            Supplier<List<T>> v2Func,
            Executor executor,
            Function<T, String> identifier) {
        Runnable task = () -> {
            List<T> v1Data = ofNullable(v1).orElseGet(v1Func);
            List<T> v2Data = ofNullable(v2).orElseGet(v2Func);
            if (v1Data.size() != v2Data.size()) {
                // todo logEvent
            }
            Map<String, T> v2Map = v2Data.stream().collect(toMap(identifier, d -> d));
            for (T v1Datum : v1Data) {
                T v2Datum = v2Map.get(identifier.apply(v1Datum));
                if (!Objects.equals(v1Datum, v2Datum)) {
                    // todo logEvent
                }
            }
        };
        if (executor == null) {
            task.run();
        } else {
            executor.execute(task);
        }
    }

}
