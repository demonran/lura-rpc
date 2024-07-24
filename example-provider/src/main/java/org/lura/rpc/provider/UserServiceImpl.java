package org.lura.rpc.provider;

import org.lura.rpc.common.model.User;
import org.lura.rpc.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
