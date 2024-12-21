package com.github.zzf.dd.redis_multi_get;

import com.github.zzf.dd.user.model.User;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
public interface SomeMultiGetService {

    List<User> batchGetBy(@NotEmpty String area,
        @NotEmpty @Size(max = 200) List<@NotEmpty String> userNoList);

    User getBy(@NotEmpty String area, @NotEmpty String userNo);

}
