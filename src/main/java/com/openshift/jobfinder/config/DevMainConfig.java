package com.openshift.jobfinder.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.Mongo;
import com.openshift.jobfinder.mongodb.repository.JobRepository;

@Configuration
@ComponentScan(basePackages = "com.openshift.jobfinder", excludeFilters = { @Filter(Configuration.class) })
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableMongoRepositories(basePackageClasses = JobRepository.class)
@Profile("dev")
public class DevMainConfig {

	@Bean(destroyMethod = "shutdown")
	public DataSource dataSource() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseName("jobfinder");
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		factory.setDatabasePopulator(databasePopulator());
		return factory.getDatabase();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// internal helpers

	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource());
		dataSourceInitializer.setEnabled(true);
		dataSourceInitializer.setDatabasePopulator(databasePopulator());
		return dataSourceInitializer;
	}

	private DatabasePopulator databasePopulator() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("drop-tables.sql"));
		populator.addScript(new ClassPathResource("create-tables.sql"));
		populator.addScript(new ClassPathResource(
				"JdbcUsersConnectionRepository.sql",
				JdbcUsersConnectionRepository.class));
		return populator;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		Mongo mongo = new Mongo("localhost", 27017);
		String databaseName = "jobfinder";
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo,
				databaseName);
		return mongoDbFactory;
	}
}
