package com.github.zzf.dd.rpc.http.provider.controller_inherit;

import com.github.zzf.dd.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/base")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    // @GetMapping("/users")
    public List<User> userList() {
        log.info("UserController -> userList");
        return List.of();
    }

}
