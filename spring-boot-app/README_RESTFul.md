# RESTFUL

## 202503

### 查询 resources 的全部 id

1. 使用专用的 Endpoint `GET /resources/_ids`
   1. 清晰明了，容易理解。
   1. 不会影响现有资源的查询逻辑。
1. 使用过滤器（Filter）`GET /resources?select=id` / `GET /resources?fields=id`
   1. 灵活性高，可以与其他查询参数结合使用

决策点:

1. 性能。ids 查询与 `/resources` 查询性能一致，优先选择 Filter；ids 服务端单独实现（为了性能）优先使用 Endpoint。
1. 灵活性。需要 resource 中多个字段时，优先使用 Filter。

#### 结合 page 查询

如果数据量较大，建议结合分页和排序功能，以避免一次性返回过多数据。

`GET /resources/_ids?page=1&size=100`

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

#### search_after 全量遍历 / 增量遍历

1. DB 全量遍历，不要走缓存，否则可能造成严重缓存污染
1. 限流做好，防止 DB 被打挂
1. 默认只提供全量(按 id asc) / 增量(按 updatedAt 选择数据集， id asc) 2中遍历方法

- `POST {{host}}/api/v1/stations/_searchAfter`
  > 去掉 updatedAt 可拉取全量数据

  ```json
  {
    "id": 6,
    "size": 2,
    "updatedAt": {
      "gte": "2025-02-12T18:34:49"
    }
  }
  ```

  ```sql
  -- watch out: id > ? order by id limit ? 
  -- 3个条件缺一不可
  select * from tb_resource 
  where id > ? and updated_at >= ? and is_deleted = 0 
  order by id 
  limit ?
  ```

- `GET /resources/_searchAfter?id[gt]=-1`  
  > 增量遍历: `GET /resources/_searchAfter?updatedAt[gt]=20240312T08:00:00Z&id[gt]=-1`

#### url 参数传递

- 简单参数 `GET /users/_search?name=John&age=30&page=1,size=10`
- 数组 `GET /users/_batch?ids=1,2,3`
- 对象 `GET /users/_search?age[gt]=18&age[lt]=65&status=active`
  > 对象 `GET /users/_search?age[gt]=18&age[lt]=65&status[eq]=active`

  ```json
  { 
    "age": { "gt": 18, "lt": 65 },
    "status": "active"
  }
  ```

##### 对象的 LHS Brackets 表示法

> Java Spring-Web 后端暂未探索出可用方案

LHS Brackets。 对运算符进行编码的一种方法是在键名上使用方括号 [] 。例如，`GET /items?price[gte]=10&price[lte]=100` 意思是查找价格大于或等于 10 但小于或等于 100 的所有项目。

可以根据需要使用任意数量的运算符，例如 [lte]、[gte]、[exists]、[regex]、[before] 和  [after]。

LHS Brackets 客户端的过滤器值提供了更大的灵活性，无需以不同方式处理特殊字符。

- `npm install qs`

LHS Brackets 服务端的解析框架

- Spring 框架提供了 MultiValueMap 和 UriComponentsBuilder 等工具来处理查询字符串
  > 暂未探索出可行方案

#### Sort

`GET /resources?sortBy=-lastModified,+email`

关注点：

- url 中 `sortBy=-lastModified,+email` 使用驼峰命名法
- controller 入口层转 Sort 对象
- MySQL repo 层驼峰转下划线 (mybatis)
