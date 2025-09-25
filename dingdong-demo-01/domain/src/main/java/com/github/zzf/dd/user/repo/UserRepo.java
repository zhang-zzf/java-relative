package com.github.zzf.dd.user.repo;

import com.github.zzf.dd.user.model.User;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <pre>
 * 最佳实践：
 *  1. 按 ID 查询的接口返回完整模型
 *  1. 按非 ID 查询的接口返回 ID
 * </pre>
 */
public interface UserRepo {

    /**
     * 批量查询
     */
    List<User> queryUserByUserNoList(@NotNull List<String> userNoList);

    User queryUserByUserNo(@NotNull String userNo);

    long tryCreateUser(@NotNull User user);

    /**
     * 多字段联合查询
     * <pre>页面检索使用，其他场景慎用</pre>
     */
    List<String> queryByMultiCondition();

    @RequiredArgsConstructor
    class Wrapper implements UserRepo {
        protected final UserRepo delegate;

        @Override
        public User queryUserByUserNo(String userNo) {
            return delegate.queryUserByUserNo(userNo);
        }

        @Override
        public long tryCreateUser(User user) {
            return delegate.tryCreateUser(user);
        }

        @Override
        public List<User> queryUserByUserNoList(List<String> userNoList) {
            return delegate.queryUserByUserNoList(userNoList);
        }

        @Override
        public List<String> queryByMultiCondition() {
            return delegate.queryByMultiCondition();
        }
    }

}
