package com.openshift.jobfinder.mongodb.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.openshift.jobfinder.domain.Job;

@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, String> {

	List<Job> findAll();
	
}
