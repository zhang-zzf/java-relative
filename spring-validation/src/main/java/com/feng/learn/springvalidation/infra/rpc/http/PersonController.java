package com.feng.learn.springvalidation.infra.rpc.http;

import com.feng.learn.springvalidation.domain.model.Person;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = {"/api/persons", "/web/persons"})
@Slf4j
// @Validated
public class PersonController {

    @PostMapping("/_search-by-ids")
    @NotNull /* 需要配合 @Validated 生效 */
    public List<Person> searchBy(
        @RequestBody(required = false /* 当 body 不存在时， idSet 为 null */) Set<Long> idSet) {
        return null;
    }

    @PostMapping("/_search-by-ids-2")
    public List<Person> searchBy2(
        @RequestBody(required = false /* 当 body 不存在时， idSet 为 null */)
        @NotNull /* 需要配合 @Validated 生效 */ Set<Long> idSet) {
        return null;
    }

    @PostMapping("/_search-by-ids-3")
    public List<Person> searchBy3(
        @RequestBody(required = false /* 当 body 不存在时， idSet 为 null */)
        @NotEmpty /* 需要配合 @Validated 生效 */ Set<@NotNull/* 需要配合 @Validated 生效 */ Long> idSet) {
        return null;
    }

    @PostMapping("/_search-by-ids-4")
    public List<Person> searchBy4(
        @RequestBody
        @Size(min = 2) /* 需要配合 @Validated 生效 */ Set<@NotNull/* 需要配合 @Validated 生效 */ Long> idSet) {
        return null;
    }

    @PostMapping("/_search-by-ids-5")
    public List<Person> searchBy5(
        @RequestBody
        @Valid /* 需要配合 @Validated 生效。若无 @Validate 无法校验 Set 对象；有 @Validated 时不需要 @Valid 也可校验，实际上没有任何意义*/
        @Size(min = 2) /* 需要配合 @Validated 生效 */ Set<@NotNull/* 需要配合 @Validated 生效 */ Long> idSet) {
        return null;
    }

    @PostMapping("/c1")
    public List<Person> createPerson(@RequestBody/* 可以保证 person 不能为空，但是不校验 person 内的字段*/ Person person) {
        return null;
    }

    @PostMapping("/c2")
    public List<Person> createPerson2(@RequestBody
    @Valid /*不需要 @Validate , spring-web 可以识别 @Valid 进行校验*/ Person person) {
        return null;
    }
}
