package com.openshift.jobfinder.config;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DevMainConfig.class, SecurityConfig.class, SocialConfig.class })
@ActiveProfiles("dev")
public class DevMainConfigTest {

	@Inject
	private DataSource dataSource;

	@Inject
	private PlatformTransactionManager transactionManager;

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Inject
	private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;

	@Inject
	private MongoTemplate mongoTemplate;

	@Test
	public void testDataSource() {
		assertNotNull(dataSource);
	}

	@Test
	public void testTransactionManager() {
		assertNotNull(transactionManager);
	}

	@Test
	public void testJdbcTemplate() {
		assertNotNull(jdbcTemplate);
	}

	@Test
	public void testPropertyPlaceHolderConfigurer() {
		assertNotNull(propertySourcesPlaceholderConfigurer);
	}

	@Test
	public void testMongoTemplate() {
		assertNotNull(mongoTemplate);
	}

}
