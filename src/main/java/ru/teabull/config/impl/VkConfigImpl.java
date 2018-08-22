package ru.teabull.config.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.teabull.config.interfaces.VkConfig;

@Component
@PropertySource("classpath:vk.properties")
public class VkConfigImpl implements VkConfig {

    private String token;

    private String version;

    private String targetVkGroup;

    private static Logger logger = LoggerFactory.getLogger(VkConfigImpl.class);

    @Autowired
    public VkConfigImpl(Environment env) {
        try {
            token = env.getRequiredProperty("vk.token");
            version = env.getRequiredProperty("vk.version");
            targetVkGroup = env.getRequiredProperty("youtube.target.vkgroup.id");
            if (token.isEmpty() || version.isEmpty() || targetVkGroup.isEmpty()) {
                throw new NullPointerException();
            }
        } catch (IllegalStateException | NullPointerException e) {
            logger.error("VK configs have not initialized. Check vk.properties file");
            System.exit(-1);
        }
    }

    public String getToken() {
        return token;
    }

    public String getVersion() {
        return version;
    }

    public String getTargetVkGroup() {
        return targetVkGroup;
    }
}
