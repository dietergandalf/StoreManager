package com.dietergandalf.store_manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@Profile("test")
public class TestJpaConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource,
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties) {
        
        Map<String, Object> properties = new HashMap<>(hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings()));
        
        // Override problematic properties for H2 testing
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        // Disable multi-table strategies that cause issues with H2
        properties.put("hibernate.query.sqm.mutation_strategy", "inline");
        
        return builder
                .dataSource(dataSource)
                .packages("com.dietergandalf.store_manager.model")
                .persistenceUnit("default")
                .properties(properties)
                .build();
    }
}
