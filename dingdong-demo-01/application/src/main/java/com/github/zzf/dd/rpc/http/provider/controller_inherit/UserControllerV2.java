package com.github.zzf.dd.rpc.http.provider.controller_inherit;

import com.github.zzf.dd.config.config_center.GrayConfigService;
import com.github.zzf.dd.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.zzf.dd.config.spring.async.SpringAsyncConfig.ASYNC_THREAD;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/base")
public class UserControllerV2 {
    final UserController userController;
    final GrayConfigService grayConfigService;

    @GetMapping("/users")
    public List<User> userList() {
        // 灰度控制
        if (!grayConfigService.hitUserListGray()) {
            return userController.userList();
        }
        // 新逻辑
        List<User> ret = new ArrayList<>();
        // 异步比对
        self.asyncCheckUserList(ret);
        return ret;
    }

    @Async(ASYNC_THREAD)
    void asyncCheckUserList(List<User> newData) {
        List<User> oldData = userController.userList();
        // 比对 newData / oldData
        if (!Objects.equals(oldData, newData)) {
            // 打点，告警
        }
    }

    private UserControllerV2 self;

    @Autowired
    @Lazy
    public void setSelf(UserControllerV2 self) {
        this.self = self;
    }

}
