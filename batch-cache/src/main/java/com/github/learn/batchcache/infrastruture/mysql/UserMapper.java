package com.github.learn.batchcache.infrastruture.mysql;

import static java.util.stream.Collectors.toList;

import com.github.learn.batchcache.domain.model.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
@Component
public class UserMapper {

  private final ConcurrentMap<Long, User> db = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(3);

  {
    User u1 = new User().setId(1L).setName("1");
    User u2 = new User().setId(2L).setName("2");
    db.putIfAbsent(1L, u1);
    db.putIfAbsent(2L, u2);
  }

  public void insert(User user) {
    user.setId(idGenerator.getAndIncrement());
    db.putIfAbsent(user.getId(), user);
  }

  public List<User> getByIds(Set<Long> ids) {
    return db.entrySet().stream()
        .filter(e -> ids.contains(e.getKey())).map(Map.Entry::getValue)
        .collect(toList());
  }

  public void deleteByIds(Set<Long> ids) {
    for (Long id : ids) {
      db.remove(id);
    }
  }

  public void batchInsert(Set<User> users) {
    for (User user : users) {
      insert(user);
    }
  }

  public void updateById(User user) {

  }
}
