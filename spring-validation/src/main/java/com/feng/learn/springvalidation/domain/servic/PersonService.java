package com.feng.learn.springvalidation.domain.servic;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@Service
@RequiredArgsConstructor
@Validated
public class PersonService {

    final PersonRepo personRepo;

    public @NotNull Person getById(@NotNull @Range Long id) {
        // just for test
        if (id < 10L) {
            return null;
        }
        return personRepo.getById(id);
    }

    /**
     * <pre>
     *    `@NotNull` 校验 person 不能为 null
     *    `@Valid` 级联校验，校验 Person 内的注解
     * </pre>
     */
    public @Range(min = 0L) int updatePerson(@NotNull @Valid Person person) {
        return 0;
    }

    public @Range(min = 0L) int updatePerson2(@NotNull @Size(min = 1) List<@NotNull Person> personList) {
        return 0;
    }

    /**
     * <pre>
     *  `@NotNull` / @Size 校验 personList 参数
     *  `NotNull` 校验 personList 中的对象
     *  `@Valid` 级联校验 Person 对象中的属性
     * </pre>
     */
    public @Range(min = 0L) int updatePerson3(@NotNull @Size(min = 1) List<@NotNull @Valid Person> personList) {
        return 0;
    }

    public @Range(min = 0L) int updatePerson4(@NotNull @Size(min = 1) @Valid List<@NotNull Person> personList) {
        return 0;
    }

    /**
     * 无法校验 idList 中的 null 对象
     */
    public @NotNull List<Person> batchGetById(@NotNull @Size(min = 1) List<Long> idList) {
        return Collections.emptyList();
    }

    /**
     * List 对象不需要加 @Valid 可以检验 List 对象中的 null 元素
     */
    public @NotNull List<Person> batchGetById2(@NotNull @Size(min = 1) List<@NotNull Long> idList) {
        return Collections.emptyList();
    }


}
