package com.feng.learn.springvalidation.domain.repo;

import com.feng.learn.springvalidation.domain.model.Person;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
public interface PersonRepo {

    /**
     * get by id
     *
     * @param id id
     * @return data
     */
    @NotNull
    Person getById(@NotNull Long id);

    /**
     * update person
     *
     * @param person data
     * @return updated number
     */
    @Range
    int updatePerson(@NotNull @Valid Person person);

    /**
     * <pre>
     *     idListSet0 可以为 null
     *     idListSet 不可以为 null, emptySet, 可以包含 null
     *     idListSet2 不可以为 null / emptySet, 不可以包含 null
     *     idListMap 不可以为 null / emptySet, key 不可以包含 null, value 可以包含 null
     *     idListMap2 不可以为 null / emptySet, key 不可以包含 null, value 不可以包含 null
     * </pre>
     */
    @NotNull
    List<Person> batchGetById(
            @NotNull @Size(min = 1, max = 2) List<Long> idList,
            Set<Long> idListSet0,
            @NotEmpty Set<Long> idListSet,
            @NotEmpty Set<@NotNull Long> idListSet2,
            @NotEmpty Map<@NotNull Long, String> idListMap,
            @NotEmpty Map<@NotNull Long, @NotEmpty String> idListMap2
    );

}
