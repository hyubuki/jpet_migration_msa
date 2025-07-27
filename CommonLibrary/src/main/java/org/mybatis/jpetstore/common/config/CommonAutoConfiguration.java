package org.mybatis.jpetstore.common.config;

import org.mybatis.jpetstore.common.grpc.CatalogGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfig.class)
public class CommonAutoConfiguration {

    @Bean
    public CatalogGrpcClient catalogGrpcClient() {
        return new CatalogGrpcClient();
    }
}
