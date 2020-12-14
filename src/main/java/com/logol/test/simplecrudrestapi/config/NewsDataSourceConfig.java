package com.logol.test.simplecrudrestapi.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.logol.test.simplecrudrestapi.repository",
        entityManagerFactoryRef = "newsEntityManagerFactory",
        transactionManagerRef = "newsTransactionManager")
public class NewsDataSourceConfig {
	
	@Autowired
    private Environment env;

	@Primary
    @Bean
    @ConfigurationProperties(prefix="datasource")
    public DataSourceProperties newsDataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Primary
	@Bean
    public DataSource newsDataSource() {
    	return DataSourceBuilder.create()
            .driverClassName(env.getProperty("spring.datasource.driver-class-name"))
            .url(env.getProperty("spring.datasource.url"))
            .username(env.getProperty("spring.datasource.username"))
            .password(env.getProperty("spring.datasource.password"))
            .build();
    }
 
	@Primary
    @Bean
    public PlatformTransactionManager newsTransactionManager() {
        EntityManagerFactory factory = newsEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }
    
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean newsEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(newsDataSource());
        factory.setPackagesToScan(new String[]{"com.logol.test.simplecrudrestapi.model"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
     
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        factory.setJpaProperties(jpaProperties);
     
        return factory;
    }
    
    @Primary
    @Bean
    public DataSourceInitializer newsDataSourceInitializer() {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(newsDataSource());
        return dataSourceInitializer;
    }  
}	
