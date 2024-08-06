package org.lura.rpc.common.service;

import org.lura.rpc.common.model.User;

public interface UserService {

    /**
     * 获取用户
     * @param user 查询对象
     * @return 查询结果
     */
    User getUser(User user);
}
