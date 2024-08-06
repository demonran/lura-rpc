package org.lura.rpc.protocol;

import java.net.Inet4Address;

public interface Protocol {
    /**
     * 绑定服务
     * @param port 绑定端口
     */
    void bind(int port);

    /**
     * 连接服务
     * @param address 服务地址
     * @param port 服务端口
     */
    void connect(Inet4Address address, int port);
}
