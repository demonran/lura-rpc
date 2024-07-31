package org.lura.rpc.consumer;

import org.lura.rpc.RpcApplication;
import org.lura.rpc.common.model.User;
import org.lura.rpc.common.service.UserService;
import org.lura.rpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void main(String[] args) {

//        RpcApplication.init();

        UserService userService = ServiceProxyFactory.create(UserService.class);
        User user = new User();
        user.setName("Lura");

        User newUser = userService.getUser(user);

        System.out.println(newUser);
    }
}
