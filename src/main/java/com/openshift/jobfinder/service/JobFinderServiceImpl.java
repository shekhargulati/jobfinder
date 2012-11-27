package com.openshift.jobfinder.service;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.openshift.jobfinder.domain.Job;
import com.openshift.jobfinder.mongodb.repository.JobRepository;

@Service
public class JobFinderServiceImpl implements JobFinderService {

	private MongoTemplate mongoTemplate;

	@Inject
	private JobRepository jobRepository;
	
	@Inject
	public JobFinderServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<Job> findAllJobs() {
		Query query = new Query().limit(10);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public Job findOneJob(String jobId) {
		Query query = Query.query(Criteria.where("_id").is(jobId));
		return mongoTemplate.findOne(query, Job.class);
	}

	@Override
	public List<Job> findAllJobsNear(double latitude, double longitude) {
		Query query = Query
				.query(Criteria.where("location").near(
						new Point(latitude, longitude))).limit(5);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public List<Job> findAllJobsNearWithSkill(double latitude, double longitude,
			String[] skills,String username) {
		Query query = Query.query(
				Criteria.where("location").near(new Point(latitude, longitude))
				.and("skills").in(Arrays.asList(skills)).and("appliedBy").nin(username)).limit(5);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public Job saveJob(Job job){
		jobRepository.save(job);
		return job;
	}
	@Override
	public long totalNumberOfJob(){
		return jobRepository.count();
	}
	
	@Override
	public void deleteJob(Job job){
		jobRepository.delete(job);
	}

	@Override
	public void appyJob(String jobId, String username) {
		Query query = Query.query(Criteria.where("_id").is(jobId));
		Update update = new Update().addToSet("appliedBy", username);
		mongoTemplate.updateFirst(query, update, Job.class);
		
	}

	@Override
	public List<Job> recommendJobs(double latitude, double longitude,
			String[] skills, String username) {
		Query query = Query.query(
				Criteria.where("location").near(new Point(latitude, longitude))
						.and("skills").in(Arrays.asList(skills)).and("appliedBy").nin(username)).limit(5);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public List<Job> appliedJobs(String user) {
		Query query = Query.query(Criteria.where("appliedBy").in(user));
		return mongoTemplate.find(query, Job.class);
	}
	
	
}
