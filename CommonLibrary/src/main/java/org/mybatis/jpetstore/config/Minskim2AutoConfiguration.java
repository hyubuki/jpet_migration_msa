package org.mybatis.jpetstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfig.class)
public class Minskim2AutoConfiguration {
}
