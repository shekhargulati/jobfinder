package com.openshift.jobfinder.account;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.openshift.jobfinder.config.DevMainConfig;
import com.openshift.jobfinder.config.SecurityConfig;
import com.openshift.jobfinder.config.SocialConfig;
import com.openshift.jobfinder.domain.Account;
import com.openshift.jobfinder.jdbc.repository.AccountRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DevMainConfig.class,SecurityConfig.class, SocialConfig.class})
@ActiveProfiles("dev")
public class JdbcAccountRepositoryTest {

	@Inject
	AccountRepository accountRepository;
	
	@Test
	public void shouldCreateNewAccountAndFindUserByUserName() throws Exception{
		Account account = new Account("test", "password", "test", "test", "test address","java,mongodb");
		accountRepository.createAccount(account);
		
		Account persistedAccount = accountRepository.findAccountByUsername("test");
		assertEquals(account, persistedAccount);
	}

}
