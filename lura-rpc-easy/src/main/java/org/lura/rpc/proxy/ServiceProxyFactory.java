package org.lura.rpc.proxy;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {

    public static <T> T create(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new ServiceProxy());
    }
}
