package com.openshift.jobfinder.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

import com.mongodb.Mongo;
import com.openshift.jobfinder.domain.Job;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.UserTempNaming;

public class JobFinderServiceImplTest {

	private static final String MONGODB_HOST = System
			.getenv("OPENSHIFT_INTERNAL_IP") == null ? "localhost" : System
			.getenv("OPENSHIFT_INTERNAL_IP");
	private static final int MONGODB_PORT = 15000;
	private static final String DB_NAME = "jobfinder";

	private static MongodProcess mongoProcess;
	private static Mongo mongo;

	private MongoTemplate mongoTemplate;

	private JobFinderService jobFinderService;

	@BeforeClass
	public static void initializeDB() throws IOException {

		RuntimeConfig config = new RuntimeConfig();
		config.setExecutableNaming(new UserTempNaming());

		MongodStarter starter = MongodStarter.getInstance(config);

		MongodExecutable mongoExecutable = starter.prepare(new MongodConfig(
				Version.V2_2_1, MONGODB_PORT, false));
		mongoProcess = mongoExecutable.start();

		mongo = new Mongo(MONGODB_HOST, MONGODB_PORT);
		mongo.getDB(DB_NAME);
	}

	@AfterClass
	public static void shutdownDB() throws InterruptedException {
		mongo.close();
		mongoProcess.stop();
	}

	@Before
	public void setUp() throws Exception {
		mongoTemplate = new MongoTemplate(mongo, DB_NAME);
		jobFinderService = new JobFinderServiceImpl(mongoTemplate);

		List<Job> jobs = jobsTestData();
		mongoTemplate.insertAll(jobs);
		mongoTemplate.indexOps(Job.class).ensureIndex(new GeospatialIndex("location"));
	}

	private List<Job> jobsTestData() {
		List<Job> jobs = new ArrayList<Job>();
		Job job1 = new Job("1","Job Title1",
				new String[] { "java", "mongodb" },new double[]{33.978622, -118.404471});
		job1.setAppliedBy(new String[]{"test"});
		jobs.add(job1);
		
		jobs.add(new Job("2","Job Title2",
				new String[] { "ruby", "mongodb" },new double[]{33.978622, -118.404471}));
		
		jobs.add(new Job("3","Job Title 3",
				new String[] { "clojure", "redis" },new double[]{34.978622, -119.404471}));
		
		jobs.add(new Job("4","Job Title 4",
				new String[] { "scala", "mongodb" },new double[]{35.978622, -120.404471}));
		
		jobs.add(new Job("5","Job Title 5",
				new String[] { "c#", "mysql" },new double[]{35.978622, -120.404471}));
		return jobs;
	}

	@After
	public void tearDown() throws Exception {
		mongoTemplate.dropCollection(Job.class);
	}

	@Test
	public void testFindAllJobs() {
		List<Job> allJobs = jobFinderService.findAllJobs();
		assertEquals(5, allJobs.size());
	}

	@Test
	public void testFindOneLocalJob() {
		Job job1 = new Job("1","Job Title1",
				new String[] { "java", "mongodb" },new double[]{33.978622, -118.404471});
		
		Job job = jobFinderService.findOneJob("1");
		assertEquals(job1, job);
	}

	@Test
	public void shouldFindJobsNearToLocation() {
		Job job1 = new Job("1","Job Title1",
				new String[] { "java", "mongodb" },new double[]{33.978622, -118.404471});
		List<Job> allJobsNear = jobFinderService.findAllJobsNear(33.978622, -118.404471);
		assertEquals(5, allJobsNear.size());
		assertEquals(job1, allJobsNear.get(0));
	}

	@Test
	public void shouldFindJobsNearToLocationWithASkill() {
		List<Job> allJobsNearWithSkill = jobFinderService.findAllJobsNearWithSkill(33.978622, -118.404471, new String[]{"java"},"test1");
		assertEquals(1, allJobsNearWithSkill.size());
	}
	
	@Test
	public void shouldRecommendJobsBasedOnSkillsAndLocation() throws Exception{
		List<Job> recommededJobs = jobFinderService.recommendJobs(33.978622, -118.404471, new String[]{"java","mongodb"}, "test");
		assertEquals(2, recommededJobs.size());
		
		recommededJobs = jobFinderService.recommendJobs(33.978622, -118.404471, new String[]{"scala","redis"}, "test");
		assertEquals(2, recommededJobs.size());
	}

}
