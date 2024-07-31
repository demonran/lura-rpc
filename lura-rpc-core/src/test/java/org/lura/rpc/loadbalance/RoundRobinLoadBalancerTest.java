package org.lura.rpc.loadbalance;

import org.junit.jupiter.api.Test;
import org.lura.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RoundRobinLoadBalancerTest
 *
 * @author Liu Ran
 */
class RoundRobinLoadBalancerTest {

    private RoundRobinLoadBalancer loadBalancer;

    @Test
    void select() {
        loadBalancer = new RoundRobinLoadBalancer(new Random().nextInt(1000));
        List<ServiceMetaInfo> serviceMetaInfos = List.of(
                new ServiceMetaInfo("userService", "127.0.0.1", 8080),
                new ServiceMetaInfo("userService", "127.0.0.1", 8081),
                new ServiceMetaInfo("userService", "127.0.0.1", 8082)
        );
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(serviceMetaInfos);

        System.out.println(serviceMetaInfo);
    }
}
