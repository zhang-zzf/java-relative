package com.github.zzf.dd.redis_multi_get.repo;

import com.github.zzf.dd.user.model.User;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
public interface SomeRepo {

    User getBy(@NotEmpty String area, String userNo);

    List<User> getBy(@NotEmpty String area, @NotEmpty @Size(max = 200) List<String> userNoList);

}
