# RESTFUL

## 202503

### ids 查询

1. 使用专用的 Endpoint `GET /resources/ids`
   1. 清晰明了，容易理解。
   1. 不会影响现有资源的查询逻辑。
1. 使用过滤器（Filter）`GET /resources?select=id` / `GET /resources?fields=id`
   1. 灵活性高，可以与其他查询参数结合使用

决策点:

1. 性能。ids 查询与 `/resources` 查询性能一致，优先选择 Filter；ids 服务端单独实现（为了性能）优先使用 Endpoint。
1. 灵活性。需要 resource 中多个字段时，优先使用 Filter。

#### 结合 page 查询

如果数据量较大，建议结合分页和排序功能，以避免一次性返回过多数据。

`GET /resources/ids?page=1&size=100`

```json
{
  "ids": [1, 2, 3, 4, 5],
  "page": 1,
  "size": 100,
  "totalPages": 5,
  "totalElements": 500
}
```

### 通用 page / sort 设计

page 实现方案

1. `Page` (`page,size`/`from,size`/`OFFSET,LIMIT`)
2. `search_after`

| 方案                | 适用场景                     | 优点                           | 缺点                           |
|---------------------|------------------------------|--------------------------------|--------------------------------|
| `from` + `size`      | 小规模数据集                 | 随机翻页，实现简单                       | 深度分页性能差，内存消耗大     |
| `search_after`       | 大规模数据集，顺序翻页       | 高效，适合深度分页             | 不能随机翻页，实现复杂     |

#### spring Page 模型

- `org.springframework.data.domain.Pageable`
  - `org.springframework.data.domain.PageRequest`
    > zero-based page number
  - `org.springframework.data.domain.Sort`
- `org.springframework.data.domain.Page`
  - `org.springframework.data.domain.PageImpl`

#### Page 方案

参考: [StationController#queryPage](.)

`GET /resources/_search?page=1&size=10`

关注点：

1. 查询参数以 Map 接受，直接透传到 repo，方便后续扩展

```java
    @GetMapping("/_search")
    public Page<StationDto> queryPage(
        @RequestParam Map<String, String> parameters,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Station> pageData = stationService.queryPageBy(parameters, pageable);
        List<StationDto> stationDtoList = pageData.get().map(mapper::toDto).toList();
        return new PageImpl<>(stationDtoList, pageable, pageData.getTotalElements());
    }
```

#### Sort

`GET /users?sortBy=-lastModified,+email`

关注点：

- url 中 `sortBy=-lastModified,+email` 使用驼峰命名法
- controller 入口层转 Sort 对象
- MySQL repo 层驼峰转下划线 (mybatis)
