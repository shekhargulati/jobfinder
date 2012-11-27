package com.openshift.jobfinder.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.Mongo;
import com.openshift.jobfinder.mongodb.repository.JobRepository;

@Configuration
@ComponentScan(basePackages = "com.openshift.jobfinder", excludeFilters = { @Filter(Configuration.class) })
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackageClasses = JobRepository.class)
@EnableTransactionManagement
@Profile("openshift")
public class OpenShiftMainConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPersistenceUnitName("jobfinder-postgresql");
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setShowSql(true);
		entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		return entityManagerFactory;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		String username = System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME");
		String password = System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD");
		String host = System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST");
		String port = System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT");
        String databaseName = System.getenv("OPENSHIFT_APP_NAME");
		String url = "jdbc:postgresql://" + host + ":" + port + "/"+databaseName;
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		dataSource.setTestWhileIdle(true);
		dataSource.setTimeBetweenEvictionRunsMillis(1800000);
		dataSource.setNumTestsPerEvictionRun(3);
		dataSource.setMinEvictableIdleTimeMillis(1800000);
		dataSource.setValidationQuery("SELECT version()");

		return dataSource;
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
	
//	@Bean
	public DataSourceInitializer dataSourceInitializer(){
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
		populator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class));
		return populator;
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(openshiftMongoDBFactoryConfig());
		return mongoTemplate;
	}
	
	@Bean
	public MongoDbFactory openshiftMongoDBFactoryConfig() throws Exception {
		String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
		int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
		String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
		String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
		Mongo mongo = new Mongo(openshiftMongoDbHost, openshiftMongoDbPort);
		UserCredentials userCredentials = new UserCredentials(username,password);
		String databaseName = System.getenv("OPENSHIFT_APP_NAME");
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials);
		return mongoDbFactory;
	}
}
