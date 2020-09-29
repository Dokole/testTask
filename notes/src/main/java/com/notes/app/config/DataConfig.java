package com.notes.app.config;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Collections;

@ComponentScan("com.notes.app")
@Configuration
@EnableMongoAuditing
public class DataConfig extends AbstractMongoClientConfiguration {

    @Value("${data.mongo.username}")
    private String userName;
    @Value("${data.mongo.password}")
    private String password;
    @Value("${data.mongo.databaseName}")
    private String databaseName;
    @Value("${data.mongo.serverAddress}")
    private String serverAddress;
    @Value("${data.mongo.port}")
    private int port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder
                .credential(MongoCredential.createCredential(userName, getDatabaseName(),password.toCharArray()))
                .applyToClusterSettings(settings -> {
                    settings.hosts(Collections.singletonList(new ServerAddress(serverAddress, port)));
                });
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.notes.app.domain");
    }
}
