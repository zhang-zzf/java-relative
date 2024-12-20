package com.github.zzf.dd.redis_multi_get;

import static com.github.zzf.dd.redis_multi_get.SomeMultiGetServiceImpl.BEAN;

import com.github.zzf.dd.redis_multi_get.repo.SomeRepo;
import com.github.zzf.dd.user.model.User;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Service(BEAN)
@RequiredArgsConstructor
@Validated
public class SomeMultiGetServiceImpl implements SomeMultiGetService {

    public static final String BEAN = "someMultiGetServiceImpl";

    final SomeRepo someRepo;

    public List<User> batchGetBy(@NotEmpty String area, @NotEmpty @Size(max = 200) List<@NotEmpty String> userNoList) {
        return someRepo.getBy(area, userNoList);
    }

    public User getBy(@NotEmpty String area, @NotEmpty String userNo) {
        return someRepo.getBy(area, userNo);
    }

}
