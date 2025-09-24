package com.github.zzf.dd.common;


import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public interface Migrate {

    default <T> T migrate(
            Supplier<T> v1Func,
            Supplier<T> v2Func,
            boolean useV2,
            boolean check,
            Executor executor) {
        if (!useV2) {
            T v1Data = v1Func.get();
            if (check) {
                doCheck(v1Data, null, null, v2Func, executor);
            }
            return v1Data;
        } else {
            T v2Data = v2Func.get();
            if (check) {
                doCheck(null, v2Data, v1Func, null, executor);
            }
            return v2Data;
        }
    }

    default <T> void doCheck(
            T v1,
            T v2,
            Supplier<T> v1Func,
            Supplier<T> v2Func,
            Executor executor) {
        Runnable task = () -> {
            T v1Data = ofNullable(v1Func).map(Supplier::get).orElse(v1);
            T v2Data = ofNullable(v2Func).map(Supplier::get).orElse(v2);
            if (!com.github.zzf.dd.utils.Objects.equals(v1Data, v2Data, Set.of())) {
                // todo logEvent
            }
        };
        if (executor == null) {
            task.run();
        } else {
            executor.execute(task);
        }
    }

    default <T> Stream<T> migrate(
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
