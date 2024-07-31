package org.lura.rpc.registry;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.lura.rpc.loadbalance.LoadBalancer;
import org.lura.rpc.loadbalance.RandomLoadBalancer;
import org.lura.rpc.model.RpcConfig;
import org.lura.rpc.model.ServiceMetaInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LocalRegisty
 *
 * @author Liu Ran
 */

@Slf4j
public class LocalFileRegistry implements Registry {

    private Map<String, List<ServiceMetaInfo>> map;
    private final LoadBalancer lb = new RandomLoadBalancer();
    private FileOutputStream fos;

    @Override
    public void init(RpcConfig config) {
        Path rpcPath = Paths.get(config.getRpcFilePath(), "lura.rpc");
        File rpcFile = rpcPath.toFile();
        try {
            if (!rpcFile.exists()) {
                rpcFile.createNewFile();
            }

            fos = new FileOutputStream(rpcFile, true);
            FileInputStream fis = new FileInputStream(rpcFile);
            map = JSONUtil.toBean(new String(fis.readAllBytes()), new TypeReference<Map<String, List<ServiceMetaInfo>>>() {
            }, true);
        } catch (Exception e) {
            log.warn("LocalRegistry init error", e);
            map = new HashMap<>();
        }

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {

        List<ServiceMetaInfo> serviceMetaInfos = map.getOrDefault(serviceMetaInfo.getServiceName(), new ArrayList<>());
        serviceMetaInfos.add(serviceMetaInfo);
        map.put(serviceMetaInfo.getServiceName(), serviceMetaInfos);
        fos.write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
        fos.flush();
    }

    @Override
    public ServiceMetaInfo lookup(String serviceName) {
        List<ServiceMetaInfo> serviceMetaInfos = map.get(serviceName);
        return lb.select(serviceMetaInfos);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        List<ServiceMetaInfo> serviceMetaInfos = map.getOrDefault(serviceMetaInfo.getServiceName(), new ArrayList<>());
        serviceMetaInfos.remove(serviceMetaInfo);
    }

    @Override
    public void destroy() {

    }
}
