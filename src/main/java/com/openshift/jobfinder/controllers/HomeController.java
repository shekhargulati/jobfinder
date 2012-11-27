package com.openshift.jobfinder.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openshift.jobfinder.domain.Account;
import com.openshift.jobfinder.domain.Job;
import com.openshift.jobfinder.googleapis.DistanceResponse;
import com.openshift.jobfinder.googleapis.GoogleDistanceClient;
import com.openshift.jobfinder.jdbc.repository.AccountRepository;
import com.openshift.jobfinder.service.CoordinateFinder;
import com.openshift.jobfinder.service.JobFinderService;
import com.openshift.jobfinder.utils.SecurityUtils;

@Controller
public class HomeController {

	private final Provider<ConnectionRepository> connectionRepositoryProvider;

	private final AccountRepository accountRepository;
	
	private JobFinderService jobFinderService;
	
	@Inject
	private GoogleDistanceClient googleDistanceClient;

	@Inject
	private CoordinateFinder coordinateFinder;

	@Inject
	public HomeController(
			Provider<ConnectionRepository> connectionRepositoryProvider,
			AccountRepository accountRepository,JobFinderService jobFinderService) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
		this.accountRepository = accountRepository;
		this.jobFinderService = jobFinderService;
	}

	@RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
	public String home(Principal currentUser, Model model)  throws Exception{
		model.addAttribute("connectionsToProviders", getConnectionRepository()
				.findAllConnections());
		model.addAttribute(accountRepository.findAccountByUsername(currentUser
				.getName()));
		
		Account account = accountRepository.findAccountByUsername(SecurityUtils
				.getCurrentLoggedInUsername());
		
		double[] coordinates = coordinateFinder.find(account.getAddress());
		double latitude = coordinates[0];
		double longitude = coordinates[1];
		
		List<JobDistanceVo> recommendedJobs = recommendedJobs(latitude,longitude,account.getSkills().split(","));
		
		model.addAttribute("recommendedJobs", recommendedJobs);
		
		List<JobDistanceVo> appliedJobs = appliedJobs(latitude,longitude,SecurityUtils.getCurrentLoggedInUsername());
		model.addAttribute("appliedJobs", appliedJobs);
		return "home";
	}

	private List<JobDistanceVo> appliedJobs(double latitude, double longitude, String user) {
		List<Job> jobs = jobFinderService.appliedJobs(user);
		return toJobDistanceVo(latitude, longitude, jobs);
	}

	private List<JobDistanceVo> recommendedJobs(double latitude,double longitude,String[] skills) throws Exception{
		
		List<Job> jobs = jobFinderService.recommendJobs(latitude, longitude, skills, SecurityUtils
				.getCurrentLoggedInUsername());
		return toJobDistanceVo(latitude, longitude, jobs);
	}

	private List<JobDistanceVo> toJobDistanceVo(double latitude,
			double longitude, List<Job> jobs) {
		List<JobDistanceVo> jobsDistanceVo = new ArrayList<JobDistanceVo>();
		for (Job job : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					job.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo vo = new JobDistanceVo(job,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			jobsDistanceVo.add(vo);
		}
		return jobsDistanceVo;
	}
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
	
}
