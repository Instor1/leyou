package com.leyou.auth.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "ly.user")
public class UserProperties {
    String exchange;
    String routingKey;
    Integer code_expire_time;
}
