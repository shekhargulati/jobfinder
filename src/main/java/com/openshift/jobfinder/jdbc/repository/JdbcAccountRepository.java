package com.openshift.jobfinder.jdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openshift.jobfinder.domain.Account;

@Repository
public class JdbcAccountRepository implements AccountRepository {

	private final JdbcTemplate jdbcTemplate;

	private final PasswordEncoder passwordEncoder;

	@Inject
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void createAccount(Account user) throws UsernameAlreadyInUseException {
		try {
			jdbcTemplate.update(
					"INSERT INTO Account (firstName, lastName, username, password,address,skills) VALUES (?, ?, ?, ?,?,?)",
					user.getFirstName(), user.getLastName(), user.getUsername(),
					passwordEncoder.encode(user.getPassword()),user.getAddress(),user.getSkills());
		} catch (DuplicateKeyException e) {
			throw new UsernameAlreadyInUseException(user.getUsername());
		}
	}

	public Account findAccountByUsername(String username) {
		return jdbcTemplate.queryForObject("SELECT username, password,firstName, lastName, address,skills from Account where username = ?",
				new RowMapper<Account>() {
					public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Account(rs.getString("username"), rs.getString("password"), rs.getString("firstName"), rs
								.getString("lastName"),rs.getString("address"),rs.getString("skills"));
					}
				}, username);
	}

}
