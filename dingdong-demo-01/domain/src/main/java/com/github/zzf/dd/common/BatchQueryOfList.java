package com.github.zzf.dd.common;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public interface BatchQueryOfList {

    int BATCH_SIZE = 50;

    @SuppressWarnings("unchecked")
    private <E, ID> Function<E, ID> getIdFunc() {
        return (e) -> {
            try {
                return (ID) e.getClass().getDeclaredMethod("getId").invoke(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * <pre>
     *   search after 模式分页遍历
     *
     *   1. 最大遍历 queryFunc 10000 次
     *   1. queryFunc 注意 order by
     * </pre>
     */
    default <ID, E> Stream<E> searchAfter(ID firstID,
                                          Function<ID, List<E>> queryFunc) {
        return searchAfter(firstID, queryFunc, getIdFunc());
    }

    /**
     * <pre>
     *      search after 模式分页遍历
     *      最大遍历 queryFunc 10000 次
     * </pre>
     */
    default <ID, E> Stream<E> searchAfter(ID firstID,
                                          Function<ID, List<E>> queryFunc,
                                          Function<E, ID> idFunc) {
        ID startId = firstID;
        Stream<E> ret = Stream.empty();
        for (int i = 0; i < 10000; i++) {
            List<E> list = queryFunc.apply(startId);
            if (list.isEmpty()) break;
            ret = Stream.concat(ret, list.stream());
            E last = list.get(list.size() - 1);
            startId = idFunc.apply(last);
        }
        return ret;
    }

    default <ID, E> Stream<E> batchQuery(
        @NotNull Collection<ID> idList,
        @NotNull Function<List<ID>, Stream<E>> query) {
        return batchQuery(idList, query, BATCH_SIZE, null);
    }

    default <ID, E> Stream<E> batchQuery(
        @NotNull Collection<ID> idList,
        @NotNull Function<List<ID>, Stream<E>> query,
        @NotNull Executor executor) {
        return batchQuery(idList, query, BATCH_SIZE, executor);
    }

    default <ID, E> Stream<E> batchQuery(
        @NotNull Collection<ID> idList,
        @NotNull Function<List<ID>, Stream<E>> query,
        @Min(1) @Max(8192) int batchSize) {
        return batchQuery(idList, query, batchSize, null);
    }

    default <ID, E> Stream<E> batchQuery(
        @NotNull @NotEmpty Collection<ID> idList,
        @NotNull Function<List<ID>, Stream<E>> query,
        @Min(1) @Max(8192) int batchSize,
        @Nullable Executor executor) {
        if (idList.isEmpty()) {
            return Stream.empty();
        }
        List<ID> list;
        if (List.class.isAssignableFrom(idList.getClass())) {
            list = (List<ID>) idList;
        }
        else {
            list = new ArrayList<>(idList);
        }
        if (list.size() <= batchSize) {/* 少于1批，直接使用当前线程，减少 context switch */
            return query.apply(list);
        }
        if (executor == null) {
            return Lists.partition(list, batchSize).stream()
                .map(query)
                .reduce(Stream.empty(), Stream::concat);
        }
        else {
            return Lists.partition(list, batchSize).stream()
                .map(batch -> supplyAsync(() -> query.apply(batch), executor))
                // combine all future
                .reduce(completedFuture(Stream.empty()), (cf1, cf2) -> cf1.thenCombine(cf2, Stream::concat))
                // wait for all future complete
                .join()
                ;
        }
    }

}
