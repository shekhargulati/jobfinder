package com.openshift.jobfinder.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openshift.jobfinder.domain.Account;
import com.openshift.jobfinder.domain.Job;
import com.openshift.jobfinder.googleapis.DistanceResponse;
import com.openshift.jobfinder.googleapis.GoogleDistanceClient;
import com.openshift.jobfinder.jdbc.repository.AccountRepository;
import com.openshift.jobfinder.service.CoordinateFinder;
import com.openshift.jobfinder.service.JobFinderService;
import com.openshift.jobfinder.utils.SecurityUtils;

@Controller
public class JobController {

	@Inject
	private JobFinderService jobFinderService;

	@Inject
	private GoogleDistanceClient googleDistanceClient;

	@Inject
	private CoordinateFinder coordinateFinder;

	@Inject
	private AccountRepository accountRepository;

	@RequestMapping("/jobs")
	public ResponseEntity<String> allJobs() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Job> jobs = jobFinderService.findAllJobs();
		return new ResponseEntity<String>(Job.toJsonArray(jobs), headers,
				HttpStatus.OK);
	}

	@RequestMapping("/jobs/{jobId}")
	public ResponseEntity<String> oneJob(@PathVariable("jobId") String jobId) {
		Job job = jobFinderService.findOneJob(jobId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (job == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(job.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.POST)
	public ResponseEntity<String> createNewJob(@Valid Job job) {
		jobFinderService.saveJob(job);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(job.toJson(), headers,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteJob(@PathVariable("jobId") String jobId) {
		Job job = jobFinderService.findOneJob(jobId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (job == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jobFinderService.deleteJob(job);
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping("/jobs/near")
	@ResponseBody
	public List<JobDistanceVo> allJobsNearToLatitudeAndLongitude(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Job> jobs = jobFinderService.findAllJobsNear(latitude, longitude);
		List<JobDistanceVo> jobsVO = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			jobsVO.add(linkedinJobWithDistance);
		}

		return jobsVO;
	}

	@RequestMapping("/jobs/near/{skills}")
	@ResponseBody
	public List<JobDistanceVo> allJobsNearLatitideAndLongitudeWithSkill(
			@PathVariable("skills") String[] skills,
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JobDistanceVo> jobs = findJobs(skills, latitude, longitude);
		return jobs;
	}

	@RequestMapping("/jobs/near/{location}/{skills}")
	@ResponseBody
	public List<JobDistanceVo> allJobsNearToLocationWithSkill(
			@PathVariable("location") String location,
			@PathVariable("skills") String[] skills, Model model)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		double[] coordinates = coordinateFinder.find(location);
		if (ArrayUtils.isEmpty(coordinates)) {
			return new ArrayList<JobDistanceVo>();
		}

		double latitude = coordinates[0];
		double longitude = coordinates[1];
		List<JobDistanceVo> jobs = findJobs(skills, latitude, longitude);
		return jobs;
	}

	@RequestMapping("/jobsforme")
	public String allJobsForMe(Model model) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Account account = accountRepository.findAccountByUsername(SecurityUtils
				.getCurrentLoggedInUsername());
		double[] coordinates = coordinateFinder.find(account.getAddress());
		if (ArrayUtils.isEmpty(coordinates)) {
			return "redirect:/myprofile";
		}

		double latitude = coordinates[0];
		double longitude = coordinates[1];
		List<JobDistanceVo> jobsWithDistance = findJobsWithLocation(latitude,
				longitude);
		model.addAttribute("jobs", jobsWithDistance);
		return "jobs";
	}

	@RequestMapping(value = "/jobs/apply/{jobId}", method = RequestMethod.POST)
	public String applyJob(@PathVariable("jobId") String jobId) {
		String username = SecurityUtils.getCurrentLoggedInUsername();
		jobFinderService.appyJob(jobId, username);
		return "redirect:/home";
	}

	private List<JobDistanceVo> findJobs(String[] skills, double latitude,
			double longitude) {
		List<Job> jobs = jobFinderService.findAllJobsNearWithSkill(latitude,
				longitude, skills, SecurityUtils.getCurrentLoggedInUsername());
		List<JobDistanceVo> locaJobsWithDistance = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			locaJobsWithDistance.add(linkedinJobWithDistance);
		}
		return locaJobsWithDistance;
	}

	private List<JobDistanceVo> findJobsWithLocation(double latitude,
			double longitude) {
		List<Job> jobs = jobFinderService.findAllJobsNear(latitude, longitude);
		List<JobDistanceVo> locaJobsWithDistance = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			locaJobsWithDistance.add(linkedinJobWithDistance);
		}
		return locaJobsWithDistance;
	}
}