package org.lura.rpc.registry;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import dev.failsafe.Execution;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.lura.rpc.loadbalance.LoadBalancer;
import org.lura.rpc.loadbalance.RandomLoadBalancer;
import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class EtcdRegistry implements Registry {

    private static final String SERVICE_PATH_PREFIX = "/services/";

    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    private KV kvClient;

    private Client client;

    private LoadBalancer loadBalancer;

    @Override
    public void init(RpcConfig config) {
        loadBalancer = new RandomLoadBalancer();

        client = Client.builder()
                .endpoints(config.getEndpoints())
                .build();

        kvClient = client.getKVClient();
        // 注册时只设置30s租约，通过心跳来续期， 避免服务crash后注册信息不过期问题。
        heartBeat();


    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 设置30s租约
        long leaseId = client.getLeaseClient().grant(30).get().getID();

        ByteSequence key = ByteSequence.from(SERVICE_PATH_PREFIX + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption);
        localRegisterNodeKeySet.add(serviceMetaInfo.getServiceNodeKey());
    }

    @Override
    public ServiceMetaInfo lookup(String serviceName) {
        ByteSequence prefixKey = ByteSequence.from(SERVICE_PATH_PREFIX + serviceName, StandardCharsets.UTF_8);
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        List<ServiceMetaInfo> serviceMetaInfoList = new ArrayList<>();

        try {

            kvClient.get(prefixKey, getOption).get().getKvs().forEach(kv -> {
                ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(kv.getValue().toString(), ServiceMetaInfo.class);
                serviceMetaInfoList.add(serviceMetaInfo);
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return loadBalancer.select(serviceMetaInfoList);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        ByteSequence key = ByteSequence.from(SERVICE_PATH_PREFIX + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8);
        kvClient.delete(key);
        localRegisterNodeKeySet.remove(serviceMetaInfo.getServiceNodeKey());
    }

    @Override
    public void destroy() {
        localRegisterNodeKeySet.forEach(serviceNodeKey ->
                kvClient.delete(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8)));

        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    private void watch(String serviceNodeKey) {
        ByteSequence key = ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8);
        Watch watchClient = client.getWatchClient();

        watchClient.watch(key, watchResponse -> {
            watchResponse.getEvents().forEach(watchEvent -> {
                if (Objects.requireNonNull(watchEvent.getEventType()) == WatchEvent.EventType.DELETE) {
//                    kvClient.delete(watchEvent.getKeyValue().getKey());
                }
            });
        });
    }

    private void heartBeat() {
        CronUtil.schedule("*/10 * * * *", (Task) () -> {
            localRegisterNodeKeySet.forEach(serviceNodeKey -> {
                try {
                    List<KeyValue> keyValues = kvClient.get(ByteSequence.from(SERVICE_PATH_PREFIX + serviceNodeKey, StandardCharsets.UTF_8))
                            .get()
                            .getKvs();

                    KeyValue keyValue = keyValues.get(0);

                    String value = keyValue.getValue().toString();
                    register(JSONUtil.toBean(value, ServiceMetaInfo.class));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
