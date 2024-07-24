package org.lura.rpc.utls;

import cn.hutool.setting.yaml.YamlUtil;

import java.io.InputStream;

public class ConfigUtils {

    public static <T> T loadConfig(Class<T> clazz) {
        InputStream is = ConfigUtils.class.getClassLoader().getResourceAsStream("application.yaml");
        return YamlUtil.load(is, clazz);
    }
}
